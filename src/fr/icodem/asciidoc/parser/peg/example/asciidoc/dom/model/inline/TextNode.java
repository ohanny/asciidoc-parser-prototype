package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ElementType;

public class TextNode extends InlineNode {
    private String text;

    public static TextNode of(String text) {
        TextNode node = new TextNode();
        node.type = ElementType.TextNode;
        node.text = text;

        return node;
    }

    public String getText() {
        return text;
    }
}
