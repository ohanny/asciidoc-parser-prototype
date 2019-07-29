package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

public class LiteralBlock extends TextBlock {

    public static LiteralBlock of(Text text) {
        LiteralBlock block = new LiteralBlock();
        block.text = text;

        return block;
    }
}
