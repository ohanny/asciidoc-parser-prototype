package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeEntry;

public class AttributeEntryBuilder {
    private String name;
    private String value;
    private boolean disabled;

    public static AttributeEntryBuilder newBuilder() {
        return new AttributeEntryBuilder();
    }

    public AttributeEntry build() {
        return AttributeEntry.of(name, value, disabled);
    }

    public void clear() {
        this.name = null;
        this.value = null;
        this.disabled = false;
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
}
