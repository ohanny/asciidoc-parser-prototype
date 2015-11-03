package fr.icodem.asciidoc.parser.elements;

public class DocumentTitle extends Element {
    private String text;

    public DocumentTitle(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
