package fr.icodem.asciidoc.parser.elements;

public class Paragraph extends Element {
    private String text;

    public Paragraph(String text) {
        super(null);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
