package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

public class XRef {
    private String value;
    private String label;
    private boolean internal;

    public static XRef of(String value, String label, boolean internal) {
        return new XRef(value, label, internal);
    }

    private XRef(String value, String label, boolean internal) {
        this.value = value;
        this.label = (label != null)?label:value;
        this.internal = internal;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public boolean isInternal() {
        return internal;
    }
}
