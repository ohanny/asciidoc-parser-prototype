package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Quote;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Text;

public class QuoteBuilder implements BlockBuilder, TextContainer {
    private String attribution;
    private String citationTitle;
    private String text;
    private AttributeList attributeList;

    public static QuoteBuilder of(String attribution, String citationTitle, AttributeList attList) {
        QuoteBuilder builder = new QuoteBuilder();
        builder.attribution = attribution;
        builder.citationTitle = citationTitle;
        builder.attributeList = attList;

        return builder;
    }

    @Override
    public Quote build() {
        return Quote.of(attributeList, Text.of(text), attribution, citationTitle);
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }
}
