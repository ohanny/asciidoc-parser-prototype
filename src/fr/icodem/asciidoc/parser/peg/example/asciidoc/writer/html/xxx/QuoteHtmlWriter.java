package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Quote;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

public abstract class QuoteHtmlWriter extends ModelHtmlWriter<QuoteHtmlWriter> {

    public QuoteHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(Quote quote) {
        startQuote(quote);
        writeContent(quote);
        endQuote(quote);
    }


    protected abstract void startQuote(Quote quote);

    private void writeContent(Quote quote) {
        getTextWriter().write(quote.getText());
    }

    protected abstract void endQuote(Quote quote);

}
