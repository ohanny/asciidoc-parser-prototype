package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

public class XRef {
    private String value;
    private String label;

    public static XRef of(String value, String label) {
        return new XRef(value, label);
    }

    private XRef(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}
