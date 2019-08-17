package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

public abstract class Element {
    protected String id;
    protected ElementType type;
    protected Element parent;

    public String getId() {
        return id;
    }

    public ElementType getType() {
        return type;
    }

    public <E extends Element> E getParent() {
        return (E)parent;
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }
}
