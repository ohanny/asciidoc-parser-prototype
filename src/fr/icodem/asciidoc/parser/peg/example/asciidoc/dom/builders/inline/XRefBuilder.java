package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.inline;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.InlineNode;

public class XRefBuilder extends InlineNodeBuilder {
    private String label;
    private String value;
    private boolean internal;

    public static XRefBuilder newBuilder() {
        return new XRefBuilder();
    }

    @Override
    public InlineNode build() {
        System.out.println(label + " / " + value + " / " + internal);
        return null;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }
}
