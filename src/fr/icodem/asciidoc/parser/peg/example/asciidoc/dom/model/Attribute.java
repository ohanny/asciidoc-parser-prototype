package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

public class Attribute {
    private String name;
    private String value;

    public static Attribute of(String name, String value) {
        Attribute att = new Attribute();
        att.setName(name);
        att.setValue(value);

        return att;
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
}
