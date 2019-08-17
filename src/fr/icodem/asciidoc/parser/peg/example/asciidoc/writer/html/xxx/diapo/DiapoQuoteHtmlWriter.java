package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Quote;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.QuoteHtmlWriter;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.*;

public class DiapoQuoteHtmlWriter extends QuoteHtmlWriter {

    public DiapoQuoteHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    protected void startQuote(Quote quote) {
        indent().append(DIV.start("class", "quote")).nl()
          .incIndent()
            .writeBlockTitle(quote)
            .indent().append(BLOCKQUOTE.start())
        ;
    }

    @Override
    protected void endQuote(Quote quote) {
        append(BLOCKQUOTE.end()).nl()
          .indent().append(DIV.start("class", "attribution")).nl()
          .incIndent()
            .indent().append("&#8212; ")
                .append(quote.getAttribution()).append(BR.tag()).nl()
                .appendIf(quote.getCitationTitle() != null, () ->
                  indent().append(CITE.start())
                          .append(replaceSpecialCharacters(quote.getCitationTitle()))
                          .append(CITE.end()).nl()
                )
              .decIndent()
              .indent().append(DIV.end()).nl()
            .decIndent()
            .indent().append(DIV.end()).nl()
        ;
    }

}
