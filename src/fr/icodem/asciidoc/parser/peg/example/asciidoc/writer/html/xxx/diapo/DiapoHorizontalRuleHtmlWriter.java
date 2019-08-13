package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.HorizontalRule;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.HorizontalRuleHtmlWriter;

import java.io.IOException;

public class DiapoHorizontalRuleHtmlWriter extends HorizontalRuleHtmlWriter {

    public DiapoHorizontalRuleHtmlWriter(Outputter outputter, WriterSet writers) {
        super(outputter, writers);
    }

    @Override
    public void write(HorizontalRule hr) throws IOException {

    }
}
