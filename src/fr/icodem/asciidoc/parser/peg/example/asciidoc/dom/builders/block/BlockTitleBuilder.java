package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.InlineNode;

public class BlockTitleBuilder implements TextContainer {
    private String text;
    private InlineNode inline;

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setInline(InlineNode inline) {
        this.inline = inline;
    }
}
