package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

public class AttributeEntry {
    private String name;
    private String value;
    private boolean disabled;

    public static AttributeEntry empty() {
        return new AttributeEntry();
    }

    public static AttributeEntry of(String name, String value) {
        return of(name, value, false);
    }

    public static AttributeEntry of(String name, String value, boolean disabled) {
        AttributeEntry att = new AttributeEntry();
        att.setName(name);
        att.setValue(value);
        att.setDisabled(disabled);
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

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
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
