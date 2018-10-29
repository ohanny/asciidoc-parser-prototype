package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import fr.icodem.asciidoc.parser.peg.NodeContext;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.HighlightRules;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.TextRules;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing.HighlightListener;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing.HighlightParameter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing.ListingProcessor;
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner;
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;

import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

import static fr.icodem.asciidoc.parser.peg.rules.RulesFactory.defaultRulesFactory;
import static java.lang.Math.min;

public class BlockListenerDelegate extends AbstractDelegate {

    private DeferredHandler deferredHandler;
    private AsciidocHandler directHandler;
    private AsciidocHandler handler;
    private ListingProcessor listingProcessor;

    private static class AuthorContext {// TODO move class ?

        private AuthorContext(int position) {
            this.position = position;
        }

        public static AuthorContext withPosition(int position) {
            return new AuthorContext(position);
        }

        int position;
        String name;
        String address;
        String addressLabel;
    }

    private Deque<AuthorContext> authors;

    private ParagraphContext currentParagraph;
    private static class ParagraphContext {
        String admonition;
        AttributeList attributeList;
        boolean quoted;

        public ParagraphContext(String admonition, AttributeList attributeList) {
            this.admonition = admonition;
            this.attributeList = attributeList;
        }

        static ParagraphContext of(String admonition, AttributeList attributeList) {
            return new ParagraphContext(admonition, attributeList);
        }
    }

    private QuoteContext currentQuote;
    private static class QuoteContext {
        String attribution;
        String citationTitle;

        public QuoteContext(String attribution, String citationTitle) {
            this.attribution = attribution;
            this.citationTitle = citationTitle;
        }

        public static QuoteContext of(String attribution, String citationTitle) {
            return new QuoteContext(attribution, citationTitle);
        }
    }

    private enum ListType {Ordered, Unordered}
    private static class ListContext {
        int level;
        int bullets;
        int itemCount;
        ListType type;
        ListContext parent;
        ListContext root;
        AttributeList attList;
        String title;

        static ListContext empty() {
            ListContext ctx = new ListContext();
            ctx.level = 1;
            ctx.root = ctx;
            return ctx;
        }

        static ListContext withParent(ListContext parent) {
            ListContext context = new ListContext();
            if (parent != null) {
                context.parent = parent;
                context.level = parent.level + 1;
            } else {
                context.level = 1;
            }
            return context;
        }
    }
    private ListContext currentList;

    private static class CellContext {
        String text;
        CellContext next;

        static CellContext empty() {
            CellContext cell = new CellContext();
            return cell;
        }

        static CellContext withParent(CellContext parent) {
            CellContext cell = CellContext.empty();
            parent.next = cell;
            return cell;
        }
    }

    private static class ColumnContext {
        ColumnContext next;
        double width;

        static ColumnContext empty() {
            return new ColumnContext();
        }

        static ColumnContext withParent(ColumnContext parent) {
            final ColumnContext column = new ColumnContext();
            parent.next = column;
            return column;
        }
    }

    private static class ColumnsContext {
        AttributeList attList;

        int count;
        int lineNumberStart;

        ColumnContext rootColumn;
        ColumnContext currentColumn;
        CellContext rootCell;
        CellContext currentCell;

        enum Header {Undefined, NoHeader, Header}
        Header header;

        static ColumnsContext empty(AttributeList attList) {
            ColumnsContext columns = new ColumnsContext();
            columns.attList = attList;
            columns.header = Header.Undefined;
            return columns;
        }

        void addCell(int lineNumber) { // line number relative to table
            checkImplicitHeader(lineNumber);

            // add cell
            if (rootCell == null) {
                rootCell = CellContext.empty();
                currentCell = rootCell;
                lineNumberStart = lineNumber;
            } else {
                currentCell = CellContext.withParent(currentCell);
            }

            // add column
            if (lineNumber == lineNumberStart) {
                count++;
                if (rootColumn == null) {
                    rootColumn = ColumnContext.empty();
                    currentColumn = rootColumn;
                } else {
                    currentColumn = ColumnContext.withParent(currentColumn);
                }
            }
        }

        void checkImplicitHeader(int lineNumber) {
            if (!header.equals(Header.Undefined)) return;

            if (lineNumberStart > 1 || lineNumber == 2) {
                header = Header.NoHeader;
            } else if (lineNumber > 2) {
                header = Header.Header;
            }
        }

        void commit() {
            if (attList == null || !attList.hasOption("autowidth")) {
                double width = 100.0 / count;
                ColumnContext col = rootColumn;
                for (int i = 0; i < count; i++) {
                    col.width = width;
                    col = col.next;
                }
            }
        }

    }

    private static class TableContext {
        AttributeList attList;
        int lineNumber; // absolute line number in input source

        ColumnsContext columns;

        AsciidocHandler handler;

        void addCell(int lineNumber) {
            columns.addCell(lineNumber - this.lineNumber);
        }

        static TableContext of(AsciidocHandler handler, AttributeList attList, int lineNumber) {
            TableContext ctx = new TableContext();
            ctx.handler = handler;
            ctx.attList = attList;
            ctx.columns = ColumnsContext.empty(attList);
            ctx.lineNumber = lineNumber;
            return ctx;
        }

        void flush() {
            columns.commit();
            handler.startTable(attList);

            handler.startColumnGroup();
            ColumnContext col = columns.rootColumn;
            for (int i = 0; i < columns.count; i++) {
                handler.column(attList, col.width);
                col = col.next;
            }
            handler.endColumnGroup();

            int fill = 0;
            CellContext cell = columns.rootCell;
            if (columns.header.equals(ColumnsContext.Header.Header)) {
                handler.startTableHeader();
                handler.startTableRow();
                while (cell != null && fill < columns.count) {
                    fill++;
                    handler.startTableHeaderCell();
                    handler.writeTableHeaderCellContent(cell.text);
                    handler.endTableHeaderCell();

                    cell = cell.next;
                }
                handler.endTableRow();
                handler.endTableHeader();

                fill = 0;
            }

            handler.startTableBody();
            while (cell != null) {
                if (fill == 0) {
                    handler.startTableRow();
                }

                fill++;
                handler.startTableCell();
                handler.writeTableCellContent(cell.text);
                handler.endTableCell();

                if (fill == columns.count) {
                    handler.endTableRow();
                    fill = 0;
                }

                cell = cell.next;
            }

            handler.endTableBody();
            handler.endTable();
        }
    }
    private TableContext currentTable;

    // computed refs : helps avoid duplicates
    private Map<String, Integer> refs = new HashMap<>();

    private SectionContext firstSection;
    private SectionContext currentSection;
    private static class SectionContext {
        int level;
        String title;
        String ref;
        AttributeList attList;

        SectionContext next;
        SectionContext previous;
        SectionContext parent;

        static SectionContext of(int level) {
            SectionContext section = new SectionContext();
            section.level = level;
            return section;
        }

        static SectionContext of(int level, SectionContext previous, SectionContext parent) {
            SectionContext section = new SectionContext();
            section.level = level;
            section.previous = previous;
            section.parent = parent;
            return section;
        }
    }

    private ExampleContext currentExample;
    private static class ExampleContext {
        String admonition;
        String icons;

        static ExampleContext of (String admonition, String icons) {
            ExampleContext example = new ExampleContext();
            example.admonition = admonition;
            example.icons = icons;

            return example;
        }
    }

    private AttributeEntry currentAttributeEntry;
    private String currentBlockTitle;

    public BlockListenerDelegate(AsciidocHandler handler, AttributeEntries attributeEntries) {
        super(attributeEntries);
        this.directHandler = handler;
        this.deferredHandler = DeferredHandler.of(handler);
        this.listingProcessor = ListingProcessor.newInstance();

        selectDirectHandler();

        //this.handler.attributeEntries(attributeEntries);

        selectDeferredHandler();
    }

    private void selectDirectHandler() {
        deferredHandler.flush();
        this.handler = directHandler;
    }

    private void selectDeferredHandler() {
        this.handler = deferredHandler.getProxy();
    }

    private String textToRef(String text) {
        String ref = text.toLowerCase().replaceAll("\\s+", "_");
        int count = refs.getOrDefault(ref, 0);
        refs.put(ref, ++count);
        if (count > 1) {
            ref = ref + "_" + count;
        }
        return ref;
    }

    public void postProcess() {

        // process TOC
        Toc toc = null;

        if (!getAttributeEntry("toc").isDisabled() && firstSection != null) {
            TocItem root = TocItem.of(firstSection.level - 1, "Table of Contents", null);

            Deque<TocItem> parents = new LinkedList<>();
            parents.push(root);

            SectionContext item = firstSection;
            item.previous = SectionContext.of(root.getLevel());
            while (item != null) {
                if (item.level < item.previous.level) {
                    while (true) {
                        if (parents.peek().getLevel() >= item.level) {
                            parents.pop();
                        } else {
                            break;
                        }
                    }
                }

                TocItem ti = TocItem.of(item.level, item.title, item.ref);

                TocItem parent = parents.peek();
                parent.getChildren().add(ti);

                if (item.next != null && item.level < item.next.level) {
                    parents.push(ti);
                }

                item = item.next;
            }

            toc = Toc.of(root);
        }

        handler.postProcess(toc);
    }

    public void formattedText(char[] chars) {// TODO optimize this code by using singletons
        //System.out.println("formattedText() => " + new String(chars));
        TextRules rules = new TextRules(attributeEntries);// TODO inject rules
        rules.withFactory(defaultRulesFactory());
        ParsingResult result = new ParseRunner(rules, rules::formattedText)
                //.trace()
                .parse(new StringReader(new String(chars)), new TextListener(handler, attributeEntries), null, null);
        // TODO optimize new StringReader(new String(chars))

    }

    public void blockTitleValue(String text) {// TODO not yet tested
        currentBlockTitle = text;
    }

    private String consumeBlockTitle() {
        String title = currentBlockTitle;
        currentBlockTitle = null;
        return title;
    }

    // attributes
    public void enterAttributeEntry() {
        currentAttributeEntry = AttributeEntry.empty();
    }

    public void attributeEntry(String delim) {
        if (delim.contains("!")) {
            currentAttributeEntry.setDisabled(true);
        }
    }

    public void exitAttributeEntry() {
        attributeEntries.addAttribute(currentAttributeEntry);
        currentAttributeEntry = null;
    }


    public void attributeEntryName(String name) {
        currentAttributeEntry.setName(name);
    }

    public void attributeEntryValuePart(String value) {
        if (currentAttributeEntry.getValue() != null) {
            value = currentAttributeEntry.getValue() + value;
        }
        currentAttributeEntry.setValue(value);
    }


    // document and header methods
    public void enterDocument() {
        handler.startDocument();
    }

    public void exitDocument() {
        checkExitSection(-1);
        handler.endDocument();
    }

    public void enterHeader() {
        handler.startHeader();
    }

    public void exitHeader() {
        handler.endHeader();
        selectDirectHandler();
    }

    public void enterDocumentTitle() {
        handler.startDocumentTitle();
    }

    public void documentTitle(String text) {
        handler.writeDocumentTitle(text);
    }

    public void exitDocumentTitle() {
        handler.endDocumentTitle();
    }

    public void enterAuthors() {
        authors = new LinkedList<>();
    }

    public void exitAuthors() {
        List<Author> authors = this.authors
                .stream()
                .map(a -> Author.of(a.position, a.name, a.address, a.addressLabel))
                .collect(Collectors.toList());
        handler.writeAuthors(authors);
    }

    public void enterAuthor() {
        authors.add(AuthorContext.withPosition(authors.size() + 1));
    }

    public void authorName(String name) {
        authors.peekLast().name = name;
    }

    public void authorAddress(String address) {
        authors.peekLast().address = address;
    }

    public void authorAddressLabel(String label) {
        authors.peekLast().addressLabel = label;
    }

    public void enterPreamble() {
        handler.startPreamble();
    }

    public void exitPreamble() {
        handler.endPreamble();
    }

    // content and sections methods
    public void enterContent() {
        selectDirectHandler();
        handler.startContent();
    }

    public void exitContent() {
        handler.endContent();
    }

    public void enterSection(NodeContext context) {
        int level = min(context.getIntAttribute("level", -1), 6);

        // new section
        if (firstSection != null) {
            // close parents and get new section parent
            SectionContext parent = checkExitSection(level);

            SectionContext previous = currentSection;
            currentSection = SectionContext.of(level, previous, parent);
            previous.next = currentSection;
        } else {
            firstSection = SectionContext.of(level);
            currentSection = firstSection;
        }

        currentSection.attList = consumeAttList();
        handler.startSection(level, currentSection.attList);
    }

    public void sectionTitle(String title) {
        currentSection.title = title;
        currentSection.ref = textToRef(title);
        int level = currentSection.level;
        String ref = currentSection.ref;
        AttributeList attList = currentSection.attList;
        handler.writeSectionTitle(level, title, ref, attList);
    }


    /**
     *
     * @param newSectionLevel new section level; -1 if not a new section, but the end of document
     * @return the parent level
     */
    private SectionContext checkExitSection(int newSectionLevel) {
        if (currentSection == null) return null;

        SectionContext parent = null;
        if (newSectionLevel == currentSection.level) {
            handler.endSection(currentSection.level);
            parent = currentSection.parent;
        } else if (newSectionLevel < currentSection.level) {
            handler.endSection(currentSection.level);
            SectionContext p = currentSection.parent;
            while (p != null) {
                if (p.level > newSectionLevel) {
                    handler.endSection(p.level);
                    p = p.parent;
                } else if (p.level == newSectionLevel) {
                    handler.endSection(p.level);
                    parent = p.parent;
                    break;
                } else {
                    parent = p;
                    break;
                }
            }
        } else {
            parent = currentSection;
        }

        return parent;
    }


    // blocks methods
    public void horizontalRule() {
        handler.horizontalRule();
    }

    @Override
    protected void image(ImageMacro macro) {
        macro.setTitle(consumeBlockTitle());
        handler.writeImage(macro);
    }

    @Override
    protected void video(VideoMacro macro) {
        handler.writeVideo(macro);
    }

    public void enterParagraph(String admonition) {
        admonition = admonition == null?null:admonition.toLowerCase();
        AttributeList attList = consumeAttList();

        currentParagraph = ParagraphContext.of(admonition, attList);
        if (attList != null && "quote".equals(attList.getFirstPositionalAttribute())) {
            currentParagraph.quoted = true;
        }

        if (!currentParagraph.quoted) {
            String icons = getAttributeEntry("icons").getValue();
            handler.startParagraph(admonition, icons, currentParagraph.attributeList);
        } else {
            String attribution = attList.getSecondPositionalAttribute();
            String citationTitle = attList.getThirdPositionalAttribute();
            currentQuote = QuoteContext.of(attribution, citationTitle);
            handler.startQuote(attribution, citationTitle);
        }
    }

    public void exitParagraph() {
        if (!currentParagraph.quoted) {
            handler.endParagraph(currentParagraph.admonition);
        } else {
            handler.endQuote(currentQuote.attribution, currentQuote.citationTitle);
            currentQuote = null;
        }

        currentParagraph = null;
    }

    public void enterList() {
        currentList = ListContext.empty();
        currentList.title = consumeBlockTitle();
        handler.startList();
    }

    public void exitList() {
        while (currentList != null) {
            if (currentList.type == ListType.Unordered) {
                handler.endUnorderedList(currentList.level);
            } else if (currentList.type == ListType.Ordered) {
                handler.endOrderedList(currentList.level);
            }
            currentList = currentList.parent;
        }
        handler.endList();
    }

    public void enterListItem(NodeContext context) {
        int times, dots = 0;
        if ((times = context.getIntAttribute("times.count", -1)) > 0) {
            if (currentList.type == ListType.Unordered) {
                if (times == currentList.bullets) {

                } else if (times > currentList.bullets) {
                    currentList = ListContext.withParent(currentList);
                    currentList.bullets = times;
                } else if (times < currentList.bullets) {
                    while (times < currentList.bullets  && currentList.level > 1) {
                        handler.endUnorderedList(currentList.level);
                        currentList = currentList.parent;
                    }
                }
            } else if (currentList.type == ListType.Ordered) {
                if (times > currentList.bullets) {
                    currentList = ListContext.withParent(currentList);
                    currentList.bullets = times;
                } else {
                    // find parent with same type and level
                    ListContext ancestorWithSameLevel = findParentListWithTypeAndLevel(currentList, ListType.Unordered, times);
                    if (ancestorWithSameLevel == null) {
                        currentList = ListContext.withParent(currentList);
                        currentList.bullets = times;
                    } else {
                        ListContext parent = currentList.parent;
                        while (currentList != parent) {
                            handler.endUnorderedList(currentList.level);
                            currentList = currentList.parent;
                        }
                    }
                }
            }
        } else if ((dots = context.getIntAttribute("dots.count", -1)) > 0) {
            if (currentList.type == ListType.Ordered) {
                if (dots == currentList.bullets) {

                } else if (dots > currentList.bullets) {
                    currentList = ListContext.withParent(currentList);
                    currentList.bullets = dots;
                } else if (dots < currentList.bullets) {
                    while (dots < currentList.bullets && currentList.level > 1) {
                        handler.endOrderedList(currentList.level);
                        currentList = currentList.parent;
                    }
                }
            } else if (currentList.type == ListType.Unordered) {
                // find parent with same type and level
                ListContext ancestorWithSameLevel = findParentListWithTypeAndLevel(currentList, ListType.Ordered, dots);
                if (ancestorWithSameLevel == null) {
                    currentList = ListContext.withParent(currentList);
                    currentList.bullets = dots;
                } else {
                    ListContext parent = currentList.parent;
                    while (currentList != parent) {
                        handler.endUnorderedList(currentList.level);
                        currentList = currentList.parent;
                    }
                }
            }
        }


        if (currentList.type == null) {
            if (times > 0) {
                currentList.type = ListType.Unordered;
                currentList.bullets = times;
                currentList.attList = consumeAttList();
                handler.startUnorderedList(currentList.level, currentList.attList, currentList.title);
            } else if (dots > 0) {
                currentList.type = ListType.Ordered;
                currentList.bullets = dots;
                currentList.attList = consumeAttList();
                handler.startOrderedList(currentList.level, currentList.attList);
            }
        }
        handler.startListItem(currentList.level, ++currentList.itemCount, currentList.attList);
        clearAttList();
    }

    private ListContext findParentListWithTypeAndLevel(ListContext list, ListType type, int level) {
        ListContext parent = list.parent;
        while (parent != null) {
            if (parent.type.equals(type) && parent.level == level) {
                return parent;
            }
        }

        return null;
    }

    public void exitListItem() {
        handler.endListItem(currentList.level);
    }

    public void enterListItemValue() {
        handler.startListItemValue();
    }

    public void exitListItemValue() {
        handler.endListItemValue();
    }

    public void enterLabeledList() {
        handler.startLabeledList();
    }

    public void exitLabeledList() {
        handler.endLabeledList();
    }

    public void enterLabeledListTitle() {
        handler.startLabeledListItemTitle();
    }

    public void exitLabeledListTitle() {
        handler.endLabeledListItemTitle();
    }

    public void enterLabeledListContent() {
        handler.startLabeledListItemContent();
    }

    public void exitLabeledListContent() {
        handler.endLabeledListItemContent();
    }

    public void enterLabeledListSimpleContent() {
        handler.startLabeledListItemSimpleContent();
    }

    public void exitLabeledListSimpleContent() {
        handler.endLabeledListItemSimpleContent();
    }

    public void enterTable(int lineNumber) {
        currentTable = TableContext.of(handler, consumeAttList(), lineNumber);
        //handler.startTable();
    }

    public void exitTable() {
        //handler.endTable();
        currentTable.flush();
    }

    public void enterTableRow() {
        //handler.startTableRow();
    }

    public void exitTableRow() {
        //handler.endTableRow();
    }

    public void enterTableCell(int lineNumber) {
        currentTable.addCell(lineNumber);
        //handler.startTableCell();
    }

    public void exitTableCell() {
        //handler.endTableCell();
    }

    public void tableBlock(String text) {
        currentTable.columns.currentCell.text = text;
    }

    public void listingBlock(char[] chars) {
        class HighlightParamsHolder {
            List<HighlightParameter> highlightParams = null;

        }
        HighlightParamsHolder paramsHolder = new HighlightParamsHolder();

        boolean source = false;
        String language = null;
        boolean linenums = false;
        boolean highlight = false;
        AttributeList attList = consumeAttList();
        if (attList != null) {
            if ("source".equals(attList.getFirstPositionalAttribute())) {
                source = true;
                language = attList.getSecondPositionalAttribute();
                if (language != null) language = language.toLowerCase();
                linenums = attList.hasPositionalAttributes("linenums");
                highlight = attList.hasOption("highlight");
            }

            Attribute attHighlightParams = attList.getAttribute("highlight");
            if (attHighlightParams != null) {
                HighlightRules rules = new HighlightRules(attributeEntries);// TODO inject rules
                rules.withFactory(defaultRulesFactory());
                ParsingResult result = new ParseRunner(rules, rules::highlights)
                        //.trace()
                        .parse(new StringReader((String)attHighlightParams.getValue()), new HighlightListener(params -> paramsHolder.highlightParams = params), null, null);

            }
        }


        Listing listing = listingProcessor.process(consumeBlockTitle(), chars, source, language, linenums, highlight, paramsHolder.highlightParams);
        handler.writeListingBlock(listing, attList);
    }

    public void exitListingBlock() {
        handler.endListingBlock();
    }

    public void enterCallouts() {
        handler.startCallouts();
    }

    public void exitCallouts() {
        handler.endCallouts();
    }

    public void enterCallout() {
        handler.startCallout();
    }

    public void calloutNumber(String nb) {
        handler.writeCalloutNumber(nb);
    }

    public void enterCalloutText() {
        handler.enterCalloutText();
    }

    public void exitCalloutText() {
        handler.exitCalloutText();
    }

    public void exitCallout() {
        handler.endCallout();
    }

    public void enterExample() {
        String icons = getAttributeEntry("icons").getValue();

        AttributeList attList = consumeAttList();
        String admonition = null;
        if (attList != null) {
            if (attList.hasPositionalAttributes("NOTE")) {
                admonition = "note";
            } else if (attList.hasPositionalAttributes("TIP")) {
                admonition = "tip";
            } else if (attList.hasPositionalAttributes("IMPORTANT")) {
                admonition = "important";
            } else if (attList.hasPositionalAttributes("WARNING")) {
                admonition = "warning";
            } else if (attList.hasPositionalAttributes("CAUTION")) {
                admonition = "caution";
            } else if (attList.hasPositionalAttributes("WARNING")) {
                admonition = "warning";
            }
        }

        currentExample = ExampleContext.of(admonition, icons);

        handler.startExample(admonition, icons, attList);
    }

    public void exitExample() {
        handler.endExample(currentExample.admonition);
        currentExample = null;
    }

}
