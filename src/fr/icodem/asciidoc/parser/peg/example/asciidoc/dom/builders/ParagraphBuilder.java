package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Paragraph;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Text;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Title;

public class ParagraphBuilder implements BlockBuilder, TextContainer {
    private String admonition;
    private AttributeList attributeList;
    private String text;
    private String title;

    public static ParagraphBuilder of(AttributeList attList, String title, String admonition) {
        ParagraphBuilder builder = new ParagraphBuilder();
        builder.attributeList = attList;
        builder.title = title;
        builder.admonition = admonition;

        return builder;
    }

    @Override
    public Paragraph build() {
        return Paragraph.of(attributeList, Title.of(title), Text.of(text), admonition);
    }

    @Override
    public void setText(String text) {
        this.text = text;
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
