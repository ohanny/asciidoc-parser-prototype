package fr.icodem.asciidoc.parser.elements;

public class Block extends Element {
    protected AttributeList attributeList;

    public Block(AttributeList attList) {
        super(attList.getId());
        this.attributeList = attList;
    }

    public AttributeList getAttributeList() {
        return attributeList;
    }
}
