package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.inline;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.InlineNode;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.XRefNode;

public class XRefNodeBuilder extends InlineNodeBuilder {
    private String label;
    private String value;
    private boolean internal;

    public static XRefNodeBuilder newBuilder() {
        return new XRefNodeBuilder();
    }

    @Override
    public InlineNode build() {
        return XRefNode.of(value, label, internal);
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
