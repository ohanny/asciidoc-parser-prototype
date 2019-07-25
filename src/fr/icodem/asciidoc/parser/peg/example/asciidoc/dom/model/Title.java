package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

public class Title {
    private String text;

    public static Title of(String text) {
        Title title = new Title();
        title.text = text;
        return title;
    }

    public String getText() {
        return text;
    }
}
