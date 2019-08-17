package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block;

public class Callout extends Block {
    private int number;
    private String text;

    public static Callout of(int number, String text) {
        Callout callout = new Callout();
        callout.number = number;
        callout.text = text;

        return callout;
    }

    public int getNumber() {
        return number;
    }

    public String getText() {
        return text;
    }
}
