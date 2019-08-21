package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ElementType;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.Text;

public class Quote extends TextBlock {
    private String attribution;
    private String citationTitle;

    public static Quote of(AttributeList attList, Title title, Text text, String attribution, String citationTitle) {
        Quote quote = new Quote();
        quote.type = ElementType.Quote;
        quote.attributes = attList;
        quote.title = title;
        quote.attribution = attribution;
        quote.citationTitle = citationTitle;
        quote.text = text;

        return quote;
    }

    public String getAttribution() {
        return attribution;
    }

    public String getCitationTitle() {
        return citationTitle;
    }
}
