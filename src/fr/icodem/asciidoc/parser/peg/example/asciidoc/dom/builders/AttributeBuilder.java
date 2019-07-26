package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Attribute;

public class AttributeBuilder {

    private enum Type {Id, Role, Option, Positional, Named}

    private String name;
    private String value;
    private Type type;

    public static AttributeBuilder idAttributeBuilder() {
        AttributeBuilder builder = new AttributeBuilder();
        builder.type = Type.Id;

        return builder;
    }

    public static AttributeBuilder roleAttributeBuilder() {
        AttributeBuilder builder = new AttributeBuilder();
        builder.type = Type.Role;

        return builder;
    }

    public static AttributeBuilder optionAttributeBuilder() {
        AttributeBuilder builder = new AttributeBuilder();
        builder.type = Type.Option;

        return builder;
    }

    public static AttributeBuilder positionelAttributeBuilder() {
        AttributeBuilder builder = new AttributeBuilder();
        builder.type = Type.Positional;

        return builder;
    }

    public static AttributeBuilder namedAttributeBuilder() {
        AttributeBuilder builder = new AttributeBuilder();
        builder.type = Type.Named;

        return builder;
    }

    public Attribute build() {
        Attribute att = null;

        switch (type) {
            case Id:
                att = Attribute.idAttribute(name);
                break;
            case Role:
                att = Attribute.roleAttribute(name);
                break;
            case Option:
                att = Attribute.optionAttribute(name);
                break;
            case Positional:
                att = Attribute.positionalAttribute(name);
                break;
            case Named:
                att = Attribute.namedAttribute(name, value);
                break;
        }

        return att;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
