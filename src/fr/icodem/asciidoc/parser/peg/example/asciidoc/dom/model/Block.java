package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

public abstract class Block extends Element {
    protected Title title;
    protected AttributeList attributeList;

    public Title getTitle() {
        return title;
    }

    public AttributeList getAttributeList() {
        return attributeList;
    }

}
