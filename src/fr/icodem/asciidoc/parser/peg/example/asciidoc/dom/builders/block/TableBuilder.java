package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Table;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.TableColumn;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.TableRow;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Title;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TableBuilder implements BlockBuilder {

    private AttributeList attList;
    private String title;

    private int tableLineNumber;
    private int firstLineNumber;
    private int currentLineNumber;
    private int previousLineNumber;
    private boolean firstRowOnFirstLine;
    private boolean headerChecked;

    private int cellCount;
    private int columnsCount;
    private boolean columnsCountInitialized;
    private Deque<TableColumnBuilder> columns;
    private Deque<TableRowBuilder> rows;

    public static TableBuilder newBuilder(AttributeList attList, String title, int tableLineNumber) {
        TableBuilder builder = new TableBuilder();
        builder.attList = attList;
        builder.title = title;
        builder.tableLineNumber = tableLineNumber;
        builder.firstLineNumber = -1;
        builder.currentLineNumber = -1;
        builder.columns = new LinkedList<>();
        builder.rows = new LinkedList<>();

        builder.checkColsAttribute();

        return builder;
    }

    @Override
    public Table build() {
        List<TableRow> header = this.rows
                .stream()
                .filter(TableRowBuilder::isHeader)
                .map(TableRowBuilder::build)
                .collect(Collectors.toList());

        List<TableRow> footer = this.rows
                .stream()
                .filter(TableRowBuilder::isFooter)
                .map(TableRowBuilder::build)
                .collect(Collectors.toList());

        List<TableRow> body = this.rows
                .stream()
                .filter(TableRowBuilder::isBody)
                .map(TableRowBuilder::build)
                .collect(Collectors.toList());

        List<TableColumn> columns = this.columns
                .stream()
                .map(TableColumnBuilder::build)
                .collect(Collectors.toList());

        Table table = Table.of(attList, Title.of(title), columns, header, footer, body);

        table.getColumns().forEach(c -> c.setTable(table));

        return table;
    }

    private String getAttributeValue(String name) {
        return (attList == null)?null : attList.getStringValue("cols");
    }

    // columns are initialized if 'cols' attribute is present
    private void checkColsAttribute() {
        String cols = getAttributeValue("cols");
        if (cols != null) {
            columnsCount = Integer.parseInt(cols);
            columnsCountInitialized = true;
            initColumns();
        }
    }

    private void initColumns() {
        for (int i = 0; i < columnsCount; i++) {
            columns.addLast(TableColumnBuilder.newBuilder());
        }

        computeColumnWidth();
    }

    private void computeColumnWidth() {
        if (attList == null || !attList.hasOption("autowidth")) {
            double width = 100.0 / columns.size();
            columns.forEach(c -> c.setWidth(width));
        }
    }

    public void addCell(int lineNumber) {
        boolean newLine = updateLineNumber(lineNumber);

        if (isNewRow()) {
            addRow();
            checkColumnCount();
            cellCount = 1;
        } else {
            cellCount++;
        }

        getLastRow().addCell();

        if (isFirstNonBlankLine() && !columnsCountInitialized) {
            columnsCount++;
        }

    }

    // return true if new line
    private boolean updateLineNumber(int lineNumber) {
        if (this.firstLineNumber == -1) {
            this.firstLineNumber = lineNumber;
        }

        boolean newLine = false;
        if (lineNumber > this.currentLineNumber) {
            this.previousLineNumber = this.currentLineNumber;
            this.currentLineNumber = lineNumber;
            newLine = true;
        }

        return newLine;
    }

    private boolean isFirstLine() {
        return currentLineNumber == tableLineNumber + 1;
    }

    private boolean isFirstNonBlankLine() {
        return currentLineNumber == firstLineNumber;
    }

    private void checkColumnCount() {
        if (columnsCountInitialized) return;

        if (!isFirstNonBlankLine()) {
            columnsCountInitialized = true;
        }
    }

    private boolean isNewRow() {
        if (columnsCountInitialized) {
            return cellCount == columnsCount;
        }

        return rows.isEmpty() || !isFirstNonBlankLine();
    }

    private void addRow() {
        checkHeader();

        rows.addLast(TableRowBuilder.newBuilder());

        if (rows.size() == 1 && isFirstLine()) {
            firstRowOnFirstLine = true;
        }
    }

    private void checkHeader() {
        if (headerChecked || previousLineNumber == -1) return;

        if (currentLineNumber > previousLineNumber + 1) {
            headerChecked = true;
            if (firstRowOnFirstLine) {
                rows.forEach(row -> row.setHeader(true));
            }
        }

    }

    private TableRowBuilder getLastRow() {
        return rows.peekLast();
    }

    public void setContent(String content) {
        getLastRow().setContent(content);
    }

    public void tableEnd() {
        checkFooter();
        initColumns();
    }

    private void checkFooter() {
        if (attList != null && attList.hasOption("footer")) {
            getLastRow().setFooter(true);
        }
    }

}
