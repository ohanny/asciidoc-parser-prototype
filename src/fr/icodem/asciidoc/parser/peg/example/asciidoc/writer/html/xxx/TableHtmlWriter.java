package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Table;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.TableColumn;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.TableRow;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

public abstract class TableHtmlWriter extends ModelHtmlWriter<TableHtmlWriter> {

    public TableHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(Table table) {
        startTable(table);

        includeColumns(table);
        includeHeader(table);
        includeBody(table);
        includeFooter(table);

        endTable(table);
    }

    protected abstract void startTable(Table table);

    protected abstract void endTable(Table table);

    private void includeColumns(Table table) {
        startColumns(table);
        writeColumns(table);
        endColumns(table);
    }

    protected abstract void startColumns(Table table);

    private void writeColumns(Table table) {
        table.getColumns().forEach(this::writeColumn);
    }

    protected abstract void writeColumn(TableColumn column);

    protected abstract void endColumns(Table table);

    private void includeHeader(Table table) {
        if (table.getHeader().isEmpty()) return;

        startHeader(table);
        writeHeader(table);
        endHeader(table);
    }

    protected abstract void startHeader(Table table);

    private void writeHeader(Table table) {
        table.getHeader().forEach(this::writeRow);
    }

    protected abstract void endHeader(Table table);

    private void includeBody(Table table) {
        if (table.getBody().isEmpty()) return;

        startBody(table);
        writeBody(table);
        endBody(table);
    }

    protected abstract void startBody(Table table);

    private void writeBody(Table table) {
        table.getBody().forEach(this::writeRow);
    }

    protected abstract void endBody(Table table);

    private void includeFooter(Table table) {
        if (table.getFooter().isEmpty()) return;

        startFooter(table);
        writeFooter(table);
        endFooter(table);
    }

    protected abstract void startFooter(Table table);

    private void writeFooter(Table table) {
        table.getFooter().forEach(this::writeRow);
    }

    protected abstract void endFooter(Table table);

    protected void writeRow(TableRow row) {
        getTableRowWriter().write(row);
    }

}
