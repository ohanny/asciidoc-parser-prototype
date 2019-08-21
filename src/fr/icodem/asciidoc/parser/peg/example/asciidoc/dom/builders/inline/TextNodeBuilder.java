package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.inline;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.InlineNode;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.TextNode;

public class TextNodeBuilder extends InlineNodeBuilder {
    private String text;

    public static TextNodeBuilder newBuilder(String text) {
        TextNodeBuilder builder = new TextNodeBuilder();
        builder.text = text;

        return builder;
    }

    @Override
    public InlineNode build() {
        return TextNode.of(text);
    }

}
