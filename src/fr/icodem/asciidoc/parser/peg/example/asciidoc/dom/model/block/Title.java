package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.InlineNode;

public class Title {
    private String text;
    private InlineNode inline;

    public static Title of(String text) {
        if (text == null) return null;

        Title title = new Title();
        title.text = text;
        return title;
    }

    public static Title of(String text, InlineNode inline) {
        if (text == null) return null;

        Title title = of(text);
        title.inline = inline;

        return title;
    }

    public String getText() {
        return text;
    }

    public InlineNode getInline() {
        return inline;
    }
}
