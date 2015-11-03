package fr.icodem.asciidoc.parser.elements;

public class AttributeEntry {
    private String name;
    private String value;

    public AttributeEntry(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

}
