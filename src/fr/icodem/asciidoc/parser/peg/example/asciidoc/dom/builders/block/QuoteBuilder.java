package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Quote;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Title;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.Text;

public class QuoteBuilder implements BlockBuilder, TextContainer {
    private AttributeList attributeList;
    private String title;
    private String attribution;
    private String citationTitle;
    private String text;

    public static QuoteBuilder of(AttributeList attList, String title, String attribution, String citationTitle) {
        QuoteBuilder builder = new QuoteBuilder();
        builder.attributeList = attList;
        builder.title = title;
        builder.attribution = attribution;
        builder.citationTitle = citationTitle;

        return builder;
    }

    @Override
    public Quote build() {
        return Quote.of(attributeList, Title.of(title), Text.of(text), attribution, citationTitle);
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }
}
