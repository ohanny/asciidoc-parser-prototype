package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Title;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.InlineNode;

public class TitleBuilder implements TextContainer {
    private String text;
    private InlineNode inline;

    public static TitleBuilder newBuilder(String text) {
        TitleBuilder builder = new TitleBuilder();
        builder.text = text;

        return builder;
    }

    public Title build() {
        return Title.of(text, inline);
    }

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
