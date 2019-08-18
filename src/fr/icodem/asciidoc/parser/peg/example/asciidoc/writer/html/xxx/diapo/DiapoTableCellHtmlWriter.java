package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.TableCell;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.TableCellHtmlWriter;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.P;
import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.TD;

public class DiapoTableCellHtmlWriter extends TableCellHtmlWriter {

    public DiapoTableCellHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    protected void startCell(TableCell cell) {
        indent().append(TD.start("class", "tableblock halign-left valign-top"))
          .appendIf(cell.hasBlocks(), () -> nl().incIndent())
        ;
    }

    @Override
    protected void startText(TableCell cell) {
        appendIf(cell.hasBlocks(), () -> indent().append(P.start()));
    }

    @Override
    protected void endText(TableCell cell) {
        appendIf(cell.hasBlocks(), () -> append(P.end()).nl());
    }

    @Override
    protected void endCell(TableCell cell) {
        appendIf(cell.hasBlocks(), () -> decIndent().indent())
          .append(TD.end()).nl();
    }

}
