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

        startColumns(table);
        writeColumns(table);
        endColumns(table);

        startHeader(table);
        writeHeader(table);
        endHeader(table);

        startBody(table);
        writeBody(table);
        endBody(table);

        startFooter(table);
        writeFooter(table);
        endFooter(table);

        endTable(table);
    }

    protected abstract void startTable(Table table);

    protected abstract void endTable(Table table);

    protected abstract void startColumns(Table table);

    private void writeColumns(Table table) {
        table.getColumns().forEach(this::writeColumn);
    }

    protected abstract void writeColumn(TableColumn column);

    protected abstract void endColumns(Table table);

    protected abstract void startHeader(Table table);

    private void writeHeader(Table table) {
        table.getHeader().forEach(this::writeRow);
    }

    protected abstract void endHeader(Table table);

    protected abstract void startBody(Table table);

    private void writeBody(Table table) {
        table.getBody().forEach(this::writeRow);
    }

    protected abstract void endBody(Table table);

    protected abstract void startFooter(Table table);

    private void writeFooter(Table table) {
        table.getFooter().forEach(this::writeRow);
    }

    protected abstract void endFooter(Table table);

    protected void writeRow(TableRow row) {
        getTableRowWriter().write(row);
    }

}
