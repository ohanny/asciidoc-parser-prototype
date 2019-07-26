package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Paragraph;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Text;

public class ParagraphBuilder implements TextBlockBuilder {
    private String admonition;
    private AttributeList attributeList;
    private boolean quoted;
    private String text;

    public static ParagraphBuilder of(String admonition, AttributeList attList) {
        ParagraphBuilder builder = new ParagraphBuilder();
        builder.admonition = admonition;
        builder.attributeList = attList;

        return builder;
    }

    @Override
    public Paragraph build() {
        Paragraph p = Paragraph.of(Text.of(text), admonition);
        return p;
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

    public boolean isQuoted() {
        return quoted;
    }

    public void setQuoted(boolean quoted) {
        this.quoted = quoted;
    }
}
