package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

public abstract class TextBlock extends Block {
    protected Text text;

    public Text getText() {
        return text;
    }
}
