package fr.icodem.asciidoc.parser.elements;

public class AttributeEntry {
    private String name;
    private String value;
    private boolean disabled;

    public AttributeEntry(String name, String value, boolean disabled) {
        this.name = name;
        this.value = value;
        this.disabled = disabled;
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

    @Override
    public String toString() {
        return "AttributeEntry{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", disabled=" + disabled +
                '}';
    }
}
