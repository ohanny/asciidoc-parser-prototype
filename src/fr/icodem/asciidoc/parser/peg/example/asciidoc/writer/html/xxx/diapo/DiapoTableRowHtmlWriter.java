package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.TableRow;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.TableRowHtmlWriter;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.TR;

public class DiapoTableRowHtmlWriter extends TableRowHtmlWriter {


    public DiapoTableRowHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    protected void startRow(TableRow row) {
        indent().append(TR.start()).nl()
          .incIndent()
        ;
    }

    @Override
    protected void endRow(TableRow row) {
        decIndent().
          indent().append(TR.end()).nl()
        ;
    }

}
