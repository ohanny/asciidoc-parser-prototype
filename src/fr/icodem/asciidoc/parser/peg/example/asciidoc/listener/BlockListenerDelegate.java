package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import fr.icodem.asciidoc.parser.peg.NodeContext;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.TextRules;
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner;
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;

import java.io.StringReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import static fr.icodem.asciidoc.parser.peg.rules.RulesFactory.defaultRulesFactory;
import static java.lang.Math.min;

public class BlockListenerDelegate {

    private final static String DOCUMENT_TITLE = "DOCUMENT_TITLE";

    private AsciidocHandler handler;

    private Deque<Text> textObjects;
    private Deque<String> nodes; // TODO rename variable
    private List<Attribute> attList;

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
        int count;
        int lineNumberStart;

        ColumnContext rootColumn;
        ColumnContext currentColumn;
        CellContext rootCell;
        CellContext currentCell;

        static ColumnsContext empty() {
            return new ColumnsContext();
        }

        void addCell(String text, int lineNumber) { // line number relative to table
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
    }

    private static class TableContext {
        AttributeList attList;
        int lineNumber; // absolute line number in input source

        ColumnsContext columns;

        AsciidocHandler handler;

        void addCell(String text, int lineNumber) {
            columns.addCell(text, lineNumber - this.lineNumber);
        }

        static TableContext of(AsciidocHandler handler, AttributeList attList) {
            TableContext ctx = new TableContext();
            ctx.handler = handler;
            ctx.attList = attList;
            ctx.columns = ColumnsContext.empty();
            return ctx;
        }

        void flush() {
            handler.startTable();
            int fill = 0;
            CellContext cell = columns.rootCell;
            while (cell != null) {
                if (fill == 0) {
                    handler.startTableRow();
                }

                fill++;
                handler.startTableCell();
                handler.writeTableBlock(cell.text);
                handler.endTableCell();

                if (fill == columns.count) {
                    handler.endTableRow();
                    fill = 0;
                }

                cell = cell.next;
            }

            handler.endTable();
        }
    }
    private TableContext currentTable;

    public BlockListenerDelegate(AsciidocHandler handler) {
        this.handler = handler;
        this.nodes = new LinkedList<>();
        this.nodes.add("");
        this.textObjects = new LinkedList<>();
        this.attList = new LinkedList<>();
    }

    public void formattedText(char[] chars) {
        //System.out.println("formattedText() => " + new String(chars));
        TextRules rules = new TextRules();// TODO inject rules
        rules.useFactory(defaultRulesFactory());
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
        }
    }

    private AttributeList consumeAttList() {
        if (this.attList.isEmpty()) return null;
        AttributeList attList = AttributeList.of(this.attList);
        clearAttList();
        return attList;
    }

    private void clearAttList() {
        attList.clear();
    }


    // attributes methods
    public void attributeValue(String value) {
        textObjects.pop()
                   .setValue(value);
    }

    public void enterIdAttribute() {
        Text text = Text.empty();
        attList.add(Attribute.of("id", text));
        textObjects.push(text);
    }

    public void enterRoleAttribute() {
        Text text = Text.empty();
        attList.add(Attribute.of("role", text));
        textObjects.push(text);
    }

    public void enterOptionAttribute() {
        Text text = Text.empty();
        attList.add(Attribute.of("options", text));
        textObjects.push(text);
    }

    public void enterPositionalAttribute() {
        Text value = Text.empty();
        attList.add(Attribute.of((String)null, value));
        textObjects.push(value);
    }

    public void enterNamedAttribute() {
        Text name = Text.empty();
        Text value = Text.empty();
        attList.add(Attribute.of(name, value));

        textObjects.push(value);
        textObjects.push(name);
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
    }

    public void exitSectionTitle(NodeContext context) {
        int level = min(context.getIntAttribute("eqs.count", -1), 6);
        handler.endSectionTitle(level);
    }

    // blocks methods
    public void horizontalRule() {
        handler.horizontalRule();
    }

    public void enterParagraph() {
        handler.startParagraph();
    }

    public void exitParagraph() {
        handler.endParagraph();
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

    public void enterTable(int lineNumber) {
        currentTable = TableContext.of(handler, consumeAttList());
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
        currentTable.addCell(null, lineNumber);
        //handler.startTableCell();
    }

    public void exitTableCell() {
        //handler.endTableCell();
    }

    public void tableBlock(String text) {
        currentTable.columns.currentCell.text = text;
    }



}
