package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

public class AttributeEntry {
    private String name;
    private String value;
    private boolean disabled;

    public static AttributeEntry of(String name, String value, boolean disabled) {
        AttributeEntry att = new AttributeEntry();
        att.name = name;
        att.value = value;
        att.disabled = disabled;

        return att;
    }

    public static AttributeEntry of(String name, String value) {
        return of(name, value, false);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean isDisabled() {
        return disabled;
    }
}
