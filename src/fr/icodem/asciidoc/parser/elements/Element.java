package fr.icodem.asciidoc.parser.elements;

public class Element {
    protected String id;

    public Element(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
