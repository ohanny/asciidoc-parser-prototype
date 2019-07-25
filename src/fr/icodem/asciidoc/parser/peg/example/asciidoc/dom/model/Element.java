package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

public abstract class Element {
    protected String id;
    protected ElementType type;

    public String getId() {
        return id;
    }

    public ElementType getType() {
        return type;
    }
}
