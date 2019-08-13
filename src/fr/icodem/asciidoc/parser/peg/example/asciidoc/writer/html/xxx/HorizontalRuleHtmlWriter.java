package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.HorizontalRule;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;

import java.io.IOException;

public abstract class HorizontalRuleHtmlWriter extends ModelHtmlWriter<HorizontalRuleHtmlWriter> {

    public HorizontalRuleHtmlWriter(Outputter outputter, WriterSet writers) {
        super(outputter, writers);
    }

    public abstract void write(HorizontalRule hr) throws IOException;
}
