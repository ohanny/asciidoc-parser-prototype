package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ElementType;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.Text;

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
