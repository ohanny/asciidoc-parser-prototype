package fr.icodem.asciidoc.parser.elements;

@Deprecated
public class Title extends Element {
    private String text;

    public Title(String text) {
        super(null);
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
