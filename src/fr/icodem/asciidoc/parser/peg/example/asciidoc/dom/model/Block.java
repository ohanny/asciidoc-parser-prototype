package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

public abstract class Block extends Element {
    protected Title title;
    protected AttributeList attributes;

    public Title getTitle() {
        return title;
    }

    public AttributeList getAttributes() {
        return attributes;
    }

}
