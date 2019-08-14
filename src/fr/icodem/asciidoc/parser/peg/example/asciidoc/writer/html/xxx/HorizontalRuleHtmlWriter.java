package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.HorizontalRule;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

import java.io.IOException;

public abstract class HorizontalRuleHtmlWriter extends ModelHtmlWriter<HorizontalRuleHtmlWriter> {

    public HorizontalRuleHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public abstract void write(HorizontalRule hr) throws IOException;
}
