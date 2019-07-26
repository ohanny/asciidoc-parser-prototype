package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

public class Attribute {
    private enum Type {Id, Role, Option, Positional, Named}

    private String name;
    private String value;
    private Type type;

    public static Attribute idAttribute(String name) {
        Attribute att = new Attribute();
        att.type = Type.Id;
        att.setName(name);

        return att;
    }

    public static Attribute roleAttribute(String name) {
        Attribute att = new Attribute();
        att.type = Type.Role;
        att.setName(name);

        return att;
    }

    public static Attribute optionAttribute(String name) {
        Attribute att = new Attribute();
        att.type = Type.Option;
        att.setName(name);

        return att;
    }

    public static Attribute positionalAttribute(String value) {
        Attribute att = new Attribute();
        att.type = Type.Positional;
        att.setValue(value);

        return att;
    }

    public static Attribute namedAttribute(String name, String value) {
        Attribute att = new Attribute();
        att.type = Type.Named;
        att.setName(name);
        att.setValue(value);

        return att;
    }

    public boolean isIdAttribute() {
        return type == Type.Id;
    }

    public boolean isRoleAttribute() {
        return type == Type.Role;
    }

    public boolean isOptionAttribute() {
        return type == Type.Option;
    }

    public boolean isPositionalAttribute() {
        return type == Type.Positional;
    }

    public boolean isNamedAttribute() {
        return type == Type.Named;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", type=" + type +
                '}';
    }
}
