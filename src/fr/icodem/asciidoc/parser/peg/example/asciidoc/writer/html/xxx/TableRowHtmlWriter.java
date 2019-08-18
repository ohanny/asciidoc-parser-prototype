package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.TableCell;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.TableRow;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

public abstract class TableRowHtmlWriter extends ModelHtmlWriter<TableRowHtmlWriter> {

    public TableRowHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(TableRow row) {
        startRow(row);
        writeCells(row);
        endRow(row);
    }

    protected abstract void startRow(TableRow row);

    private void writeCells(TableRow row) {
        row.getCells().forEach(this::writeCell);
    }

    protected abstract void endRow(TableRow row);

    protected void writeCell(TableCell cell) {
        getTableCellWriter().write(cell);
    }

}
