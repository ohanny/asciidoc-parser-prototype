package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Callout;

public class CalloutBuilder implements BlockBuilder, TextContainer {
    private int number;
    private String text;

    public static CalloutBuilder newBuilder() {
        return new CalloutBuilder();
    }

    @Override
    public Callout build() {
        return Callout.of(number, text);
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }
}
