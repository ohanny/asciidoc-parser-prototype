package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ElementType;

public class XRefNode extends InlineNode {
    private String value;
    private String label;
    private boolean internal;

    public static XRefNode of(String value, String label, boolean internal) {
        XRefNode node = new XRefNode();
        node.type = ElementType.XRefNode;
        node.value = value;
        node.label = label;
        node.internal = internal;

        return node;
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
