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

    private int firstLineNumber;
    private int currentLineNumber;
    private boolean firstRow;

    private Deque<TableColumnBuilder> columns;
    private Deque<TableRowBuilder> rows;

    public static TableBuilder newBuilder(AttributeList attList, int firstLineNumber) {
        TableBuilder builder = new TableBuilder();
        builder.firstLineNumber = firstLineNumber;
        builder.currentLineNumber = firstLineNumber;
        builder.firstRow = true;
        builder.columns = new LinkedList<>();
        builder.rows = new LinkedList<>();

        return builder;
    }

    @Override
    public Table build() {
        computeColumnWidth();

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
        if (isNewRow(lineNumber)) {
            addRow();
        }

        TableRowBuilder row = getLastRow();
        row.addCell();

        if (rows.size() == 1) {
            addColumn();
        }

        this.currentLineNumber = lineNumber;

    }

    private boolean isNewRow(int lineNumber) {
        return lineNumber > currentLineNumber + 1;
    }

    private void addRow() {
        rows.addLast(TableRowBuilder.newBuilder());
    }

    private TableRowBuilder getLastRow() {
        return rows.peekLast();
    }

    private void addColumn() {
        columns.addLast(TableColumnBuilder.newBuilder());
    }

    private void computeColumnWidth() {
        if (attList == null || !attList.hasOption("autowidth")) {
            double width = 100.0 / columns.size();
            columns.forEach(c -> c.setWidth(width));
        }
    }

    public void setContent(String content) {
        getLastRow().setContent(content);
    }

}
