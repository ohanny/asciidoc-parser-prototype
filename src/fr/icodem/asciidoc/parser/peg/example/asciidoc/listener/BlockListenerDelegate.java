package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import fr.icodem.asciidoc.parser.peg.NodeContext;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.TextRules;
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
    private AttributeEntries attributeEntries;

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

    private enum ListType {Ordered, Unordered}
    private static class ListContext {
        int level;
        int bullets;
        ListType type;
        ListContext parent;
        ListContext root;

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

    private TocContext toc;
    private static class TocContext {
        TocItemContext root;
        TocItemContext currentItem;

        static TocContext empty() {
            TocContext toc = new TocContext();
            toc.root = TocItemContext.of(0, Text.of("Table of Contents"), null);
            toc.currentItem = toc.root;

            return toc;
        }

        void addItem(int level, Text text, Text ref) {
            this.currentItem.next = TocItemContext.of(level, text, ref);
            this.currentItem.next.previous = this.currentItem;
            this.currentItem = this.currentItem.next;
        }
    }

    private static class TocItemContext {
        int level;
        Text text;
        Text ref;

        TocItemContext previous;
        TocItemContext next;

        static TocItemContext of(int level, Text text, Text ref) {
            TocItemContext item = new TocItemContext();
            item.level = level;
            item.text = text;
            item.ref = ref;

            return item;
        }
    }

    private SectionContext currentSection;
    private static class SectionContext {
        int level;
        Text title;
        Text ref;

        static SectionContext of(int level, Text title, Text ref) {
            SectionContext section = new SectionContext();
            section.level = level;
            section.title = title;
            section.ref = ref;

            return section;
        }
    }

    private AttributeEntry currentAttributeEntry;

    public BlockListenerDelegate(AsciidocHandler handler) {
        super();
        this.directHandler = handler;
        this.deferredHandler = DeferredHandler.of(handler);

        selectDirectHandler();

        this.attributeEntries = AttributeEntries.newAttributeEntries();
        this.handler.attributeEntries(attributeEntries);

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

    private AttributeEntry getAttributeEntry(String name) {
        return attributeEntries.getAttribute(name);
    }

    public void postProcess() {

        // process TOC
        Toc toc = null;

        if (this.toc != null) {

            Deque<TocItem> parents = new LinkedList<>();
            TocItem root = TocItem.of(this.toc.root.level, this.toc.root.text.getValue(), null);
            parents.push(root);

            TocItemContext item = this.toc.root.next;
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

                TocItem ti = TocItem.of(item.level, item.text.getValue(), item.ref.getValue());

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
        TextRules rules = new TextRules();// TODO inject rules
        rules.withFactory(defaultRulesFactory());
        ParsingResult result = new ParseRunner(rules, rules::formattedText)
                //.trace()
                .parse(new StringReader(new String(chars)), new TextListener(handler), null, null);
        // TODO optimize new StringReader(new String(chars))

    }

    public void blockTitleValue(String text) {// TODO not yet tested
        handler.writeText(text);
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

    public void documentTitleValue(String text) {
        handler.writeDocumentTitle(text);
    }

    public void exitDocumentTitle() {
        handler.endDocumentTitle();
    }

    public void enterAuthors() {
        authors = new LinkedList<>();
        //handler.startAuthors();
    }

    public void exitAuthors() {
        //handler.endAuthors();
        List<Author> authors = this.authors
                .stream()
                .map(a -> Author.of(a.position, a.name, a.address, a.addressLabel))
                .collect(Collectors.toList());
        handler.writeAuthors(authors);
    }

    public void enterAuthor() {
        authors.add(AuthorContext.withPosition(authors.size() + 1));
        //handler.startAuthor();
    }

    public void authorName(String name) {
        //handler.writeAuthorName(name);
        authors.peekLast().name = name;
    }

    public void authorAddress(String address) {
        //handler.writeAuthorAddress(address);
        authors.peekLast().address = address;
    }

    public void authorAddressLabel(String label) {
        //handler.writeAuthorAddressLabel(label);
        authors.peekLast().addressLabel = label;
    }

    public void exitAuthor() {
//        handler.endAuthor();
//        AuthorContext author = authors.peekLast();
//        String index = author.position == 1?"":"" + author.position;
//
//        indent()
//                .append(SPAN.start("id", "author" + index, "class", "author"))
//                .append(SPAN.end())
//                .append(BR.tag())
//                .nl()
//                .runIf(author.address != null, () -> {
//                    String href = author.address;
//                    if (!(href.startsWith("http://") || href.startsWith("https://"))) {
//                        href = "mailto:" + href;
//                    }
//
//                    String label = author.addressLabel != null ? author.addressLabel : author.address;
//
//                    indent()
//                            .append(SPAN.start("id", "email" + index, "class", "email"))
//                            .append(A.start("href", href))
//                            .append(label)
//                            .append(A.end())
//                            .append(SPAN.end())
//                            .append(BR.tag())
//                            .nl();
//                });

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

        if (!getAttributeEntry("toc").isDisabled()) {
            toc = TocContext.empty();
        }
    }

    public void exitContent() {
        handler.endContent();
    }

    public void enterSection() {
        handler.startSection();
    }

    public void exitSection() {
        handler.endSection();
    }

    public void enterSectionTitle(NodeContext context) {
        int level = min(context.getIntAttribute("eqs.count", -1), 6);

        Text text = Text.empty();
        Text ref = Text.empty();
        textObjects.push(ref);
        textObjects.push(text);

        currentSection = SectionContext.of(level, text, ref);


        if (!getAttributeEntry("toc").isDisabled()) {
            toc.addItem(level, text, ref);
        }
    }

    public void sectionTitleValue(String text) {
        textObjects.pop()
                .setValue(text);
        textObjects.pop()
                .setValue(textToRef(text));
    }

    public void exitSectionTitle() {
        handler.writeSectionTitle(currentSection.level, currentSection.title.getValue(), currentSection.ref.getValue());
        currentSection = null;
    }

    // blocks methods
    public void horizontalRule() {
        handler.horizontalRule();
    }

    @Override
    protected void image(ImageMacro macro) {
        handler.writeImage(macro);
    }

    public void enterParagraph(String admonition) {
        handler.startParagraph(admonition);
    }

    public void exitParagraph(String admonition) {
        handler.endParagraph(admonition);
    }

    public void enterList() {
        currentList = ListContext.empty();
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
                if (currentList.bullets == times) {

                } else if (currentList.bullets < times) {
                    currentList = ListContext.withParent(currentList);
                    currentList.bullets = times;
                } else if (currentList.bullets > times) {
                    while (currentList.bullets > times && currentList.level > 1) {
                        handler.endUnorderedList(currentList.level);
                        currentList = currentList.parent;
                    }
                }
            } else if (currentList.type == ListType.Ordered) {
                currentList = ListContext.withParent(currentList);
                currentList.bullets = times;
            }
        } else if ((dots = context.getIntAttribute("dots.count", -1)) > 0) {
            if (currentList.type == ListType.Ordered) {
                if (currentList.bullets == dots) {

                } else if (currentList.bullets < dots) {
                    currentList = ListContext.withParent(currentList);
                    currentList.bullets = dots;
                } else if (currentList.bullets > dots) {
                    while (currentList.bullets > times && currentList.level > 1) {
                        handler.endOrderedList(currentList.level);
                        currentList = currentList.parent;
                    }
                }
            } else if (currentList.type == ListType.Unordered) {
                currentList = ListContext.withParent(currentList);
                currentList.bullets = dots;
            }
        }


        if (currentList.type == null) {
            if (times > 0) {
                currentList.type = ListType.Unordered;
                currentList.bullets = times;
                handler.startUnorderedList(currentList.level, consumeAttList());
            } else if (dots > 0) {
                currentList.type = ListType.Ordered;
                currentList.bullets = dots;
                handler.startOrderedList(currentList.level, consumeAttList());
            }
        }
        handler.startListItem(currentList.level);
        clearAttList();
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

    public void listingBlock(String listing) {
        String language = null;
        AttributeList attList = consumeAttList();
        if (attList != null && "source".equals(attList.getFirstPositionalAttribute())) {
            language = attList.getSecondPositionalAttribute();
            if (language != null) language = language.toLowerCase();
        }
        handler.writeListingBlock(listing, language);
    }


}
