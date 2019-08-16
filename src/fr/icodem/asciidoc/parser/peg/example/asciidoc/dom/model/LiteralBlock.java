package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

public class LiteralBlock extends TextBlock {

    public static LiteralBlock of(AttributeList attributes, Title title, Text text) {
        LiteralBlock block = new LiteralBlock();
        block.type = ElementType.Literal;
        block.attributes = attributes;
        block.title = title;
        block.text = text;

        return block;
    }
}
