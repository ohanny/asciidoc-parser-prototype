package fr.icodem.asciidoc.parser.elements;

public class Block extends Element {
    protected AttributeList attributeList;

    public Block(AttributeList attList) {
        super(attList == null?null:attList.getId());
        this.attributeList = attList;
    }

    public AttributeList getAttributeList() {
        return attributeList;
    }

    public String getFirstPositionalAttribute() {
        return (attributeList == null)?null:attributeList.getFirstPositionalAttribute();
    }

    public Attribute getAttribute(String name) {
        return attributeList.getAttribute(name);
    }

    public boolean existsAttribute(String name) {
        return attributeList.existsAttribute(name);
    }
}
