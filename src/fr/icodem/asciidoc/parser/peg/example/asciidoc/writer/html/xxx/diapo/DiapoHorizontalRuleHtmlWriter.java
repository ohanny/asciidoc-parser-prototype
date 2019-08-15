package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.HorizontalRule;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.HorizontalRuleHtmlWriter;

import java.io.IOException;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.HR;

public class DiapoHorizontalRuleHtmlWriter extends HorizontalRuleHtmlWriter {

    public DiapoHorizontalRuleHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    public void write(HorizontalRule hr) throws IOException {
        indent().append(HR.tag()).nl();
    }
}
