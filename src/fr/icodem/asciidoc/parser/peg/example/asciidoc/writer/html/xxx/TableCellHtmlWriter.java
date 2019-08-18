package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.TableCell;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

public abstract class TableCellHtmlWriter extends ModelHtmlWriter<TableCellHtmlWriter> {

    public TableCellHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(TableCell cell) {
        startCell(cell);
        writeContent(cell);
        endCell(cell);
    }

    protected abstract void startCell(TableCell cell);

    private void writeContent(TableCell cell) {
        if (cell.getText() != null) {
            startText(cell);
            getTextWriter().write(cell.getText());
            endText(cell);
        }
        if (cell.getBlocks() != null) {
            writeBlocks(cell.getBlocks());
        }
    }

    protected void startText(TableCell cell) {}

    protected void endText(TableCell cell) {}

    protected abstract void endCell(TableCell cell);

}
