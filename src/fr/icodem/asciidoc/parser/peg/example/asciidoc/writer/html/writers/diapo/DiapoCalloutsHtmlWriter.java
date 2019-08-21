package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.CalloutsHtmlWriter;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.DIV;
import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.TABLE;

public class DiapoCalloutsHtmlWriter extends CalloutsHtmlWriter {
    public DiapoCalloutsHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    protected void startCallouts() {
        indent().append(DIV.start("class", "colist arabic")).nl()
          .incIndent()
            .indent().append(TABLE.start()).nl()
            .incIndent()
        ;
    }

    @Override
    protected void endCallouts() {
        decIndent()
          .indent().append(TABLE.end()).nl()
          .decIndent()
          .indent().append(DIV.end()).nl()
        ;
    }


}
