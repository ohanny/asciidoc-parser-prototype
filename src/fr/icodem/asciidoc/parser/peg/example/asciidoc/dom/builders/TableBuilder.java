package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Table;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.TableColumn;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.TableRow;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class TableBuilder implements BlockBuilder {

    private AttributeList attList;

    private int tableLineNumber;
    private int firstLineNumber;
    private int currentLineNumber;
    private boolean firstRow;

    private int cellCount;
    private int columnsCount;
    private boolean columnsCountInitialized;
    private Deque<TableColumnBuilder> columns;
    private Deque<TableRowBuilder> rows;

    public static TableBuilder newBuilder(AttributeList attList, int tableLineNumber) {
        TableBuilder builder = new TableBuilder();
        builder.attList = attList;
        builder.tableLineNumber = tableLineNumber;
        builder.firstLineNumber = -1;
        builder.currentLineNumber = -1;
        builder.firstRow = true;
        builder.columns = new LinkedList<>();
        builder.rows = new LinkedList<>();

        builder.checkColsAttribute();

        return builder;
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

    @Override
    public Table build() {
        List<TableRow> rows = this.rows
                .stream()
                .map(TableRowBuilder::build)
                .collect(Collectors.toList());

        List<TableColumn> columns = this.columns
                .stream()
                .map(TableColumnBuilder::build)
                .collect(Collectors.toList());

        return Table.of(columns, null, null, rows);
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

        if (isFirstLine() && !columnsCountInitialized) {
            columnsCount++;
        }

        this.currentLineNumber = lineNumber;

    }

    // return true if new line
    private boolean updateLineNumber(int lineNumber) {
        if (this.firstLineNumber == -1) {
            this.firstLineNumber = lineNumber;
        }

        boolean newLine = false;
        if (lineNumber > this.currentLineNumber) {
            this.currentLineNumber = lineNumber;
            newLine = true;
        }

        return newLine;
    }

    private boolean isFirstLine() {
        return currentLineNumber == firstLineNumber;
    }

    private void checkColumnCount() {
        if (columnsCountInitialized) return;

        if (!isFirstLine()) {
            columnsCountInitialized = true;
        }
    }

    private boolean isNewRow() {
        if (columnsCountInitialized) {
            return cellCount == columnsCount;
        }

        return rows.isEmpty() || !isFirstLine();
    }

    private void addRow() {
        rows.addLast(TableRowBuilder.newBuilder());
    }

    private TableRowBuilder getLastRow() {
        return rows.peekLast();
    }

    public void setContent(String content) {
        getLastRow().setContent(content);
    }

    public void tableEnd() {
        initColumns();
    }

}
