package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Callout;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.InlineNode;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.Text;

public class CalloutBuilder implements BlockBuilder, TextContainer {
    private int number;
    private String text;
    private InlineNode inline;

    public static CalloutBuilder newBuilder() {
        return new CalloutBuilder();
    }

    @Override
    public Callout build() {
        return Callout.of(number, Text.of(text, inline));
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public void setText(String text) {
        this.text = text.trim();
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
