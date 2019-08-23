package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Paragraph;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.InlineNode;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.Text;

public class ParagraphBuilder implements BlockBuilder, TextContainer {
    private BlockTitleBuilder title;
    private String admonition;
    private AttributeList attributeList;
    private String text;
    private InlineNode inline;

    public static ParagraphBuilder of(BlockBuildState state, AttributeList attList, String admonition) {
        ParagraphBuilder builder = new ParagraphBuilder();
        builder.attributeList = attList;
        builder.title = state.consumeBlockTitle();
        builder.admonition = admonition;

        return builder;
    }

    @Override
    public Paragraph build() {
        return Paragraph.of(attributeList, buildTitle(title), Text.of(text.trim(), inline), admonition);
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text.trim();
    }

    @Override
    public void setInline(InlineNode inline) {
        this.inline = inline;
    }

    public String getAdmonition() {
        return admonition;
    }

    public void setAdmonition(String admonition) {
        this.admonition = admonition;
    }

    public AttributeList getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(AttributeList attributeList) {
        this.attributeList = attributeList;
    }

}
