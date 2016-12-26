package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import fr.icodem.asciidoc.parser.peg.NodeContext;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.TextRules;
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner;
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;

import java.io.Reader;
import java.io.StringReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import static fr.icodem.asciidoc.parser.peg.rules.RulesFactory.defaultRulesFactory;
import static java.lang.Math.min;

public class BlockListenerDelegate extends AbstractDelegate {

    private final static String DOCUMENT_TITLE = "DOCUMENT_TITLE";

    private AsciidocHandler handler;

    private Deque<String> nodes; // TODO rename variable

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

    private TocContext toc;
    private static class TocContext {
        TocItemContext root;
        TocItemContext currentItem;

        static TocContext empty() {
            TocContext toc = new TocContext();
            toc.root = TocItemContext.of(-1, "Table of Contents");
            toc.currentItem = toc.root;

            return toc;
        }

        void addItem(int level, String text) {
            this.currentItem.next = TocItemContext.of(level, text);
            this.currentItem.next.previous = this.currentItem;
            this.currentItem = this.currentItem.next;
        }
    }

    private static class TocItemContext {
        int level;
        String text;

        TocItemContext previous;
        TocItemContext next;

        static TocItemContext of(int level, String text) {
            TocItemContext item = new TocItemContext();
            item.level = level;
            item.text = text;

            return item;
        }
    }



    public BlockListenerDelegate(AsciidocHandler handler) {
        super();
        this.handler = handler;
        this.nodes = new LinkedList<>();
        this.nodes.add("");
    }

    public void postProcess() {
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

    public void text(String text) {
        switch (nodes.peekLast()) {
            case DOCUMENT_TITLE:
                handler.writeDocumentTitle(text);
                break;

            default:
                handler.writeText(text);
        }
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
    }

    public void enterDocumentTitle() {
        handler.startDocumentTitle();
        nodes.addLast(DOCUMENT_TITLE);
    }

    public void exitDocumentTitle() {
        handler.endDocumentTitle();
        nodes.removeLast();
    }

    public void enterAuthors() {
        handler.startAuthors();
    }

    public void exitAuthors() {
        handler.endAuthors();
    }

    public void enterAuthor() {
        handler.startAuthor();
    }

    public void authorName(String name) {
        handler.writeAuthorName(name);
    }

    public void authorAddress(String address) {
        handler.writeAuthorAddress(address);
    }

    public void authorAddressLabel(String label) {
        handler.writeAuthorAddressLabel(label);
    }

    public void exitAuthor() {
        handler.endAuthor();
    }

    public void enterPreamble() {
        handler.startPreamble();
    }

    public void exitPreamble() {
        handler.endPreamble();
    }

    // content and sections methods
    public void enterContent() {
        handler.startContent();

        toc = TocContext.empty();
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
        handler.startSectionTitle(level);

        toc.addItem(level, "OLIV");
    }

    public void exitSectionTitle(NodeContext context) {
        int level = min(context.getIntAttribute("eqs.count", -1), 6);
        handler.endSectionTitle(level);
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


}
