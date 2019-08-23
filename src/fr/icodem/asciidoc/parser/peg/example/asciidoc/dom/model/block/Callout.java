package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.Text;

public class Callout extends Block {
    private int number;
    private Text text;

    public static Callout of(int number, Text text) {
        Callout callout = new Callout();
        callout.number = number;
        callout.text = text;

        return callout;
    }

    public int getNumber() {
        return number;
    }

    public Text getText() {
        return text;
    }
}
