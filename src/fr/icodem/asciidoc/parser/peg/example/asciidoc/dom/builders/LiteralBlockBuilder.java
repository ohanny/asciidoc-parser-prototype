package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.LiteralBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Text;

public class LiteralBlockBuilder implements TextBlockBuilder {

    private String text;

    public static LiteralBlockBuilder newBuilder() {
        return new LiteralBlockBuilder();
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public LiteralBlock build() {
        return LiteralBlock.of(Text.of(text));
    }
}
