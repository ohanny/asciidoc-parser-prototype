package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Quote;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.Text;

public class QuoteBuilder implements BlockBuilder, TextContainer {
    private AttributeList attributeList;
    private TitleBuilder title;
    private String attribution;
    private String citationTitle;
    private String text;

    public static QuoteBuilder of(BlockBuildState state, AttributeList attList, String attribution, String citationTitle) {
        QuoteBuilder builder = new QuoteBuilder();
        builder.attributeList = attList;
        builder.title = state.consumeBlockTitle();
        builder.attribution = attribution;
        builder.citationTitle = citationTitle;

        return builder;
    }

    @Override
    public Quote build() {
        return Quote.of(attributeList, buildTitle(title), Text.of(text), attribution, citationTitle);
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }
}
