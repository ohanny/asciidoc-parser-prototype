package fr.icodem.asciidoc.parser.elements;

public class Title extends Element {
    private String text;

    public Title(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
