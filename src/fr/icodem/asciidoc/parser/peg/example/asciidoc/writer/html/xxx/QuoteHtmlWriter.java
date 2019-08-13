package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Quote;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;

import java.io.IOException;

public abstract class QuoteHtmlWriter extends ModelHtmlWriter<QuoteHtmlWriter> {

    public QuoteHtmlWriter(Outputter outputter, WriterSet writers) {
        super(outputter, writers);
    }

    public abstract void write(Quote quote) throws IOException;
}
