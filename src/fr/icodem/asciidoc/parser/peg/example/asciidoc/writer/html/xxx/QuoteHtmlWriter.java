package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Quote;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

import java.io.IOException;

public abstract class QuoteHtmlWriter extends ModelHtmlWriter<QuoteHtmlWriter> {

    public QuoteHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(Quote quote) throws IOException {
        startQuote(quote);
        writeContent(quote);
        endQuote(quote);
    }


    protected abstract void startQuote(Quote quote);

    private void writeContent(Quote quote) throws IOException {
        getTextWriter().write(quote.getText());
    }

    protected abstract void endQuote(Quote quote);

}
