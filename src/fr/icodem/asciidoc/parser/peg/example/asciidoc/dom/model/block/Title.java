package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block;

public class Title {
    private String text;

    public static Title of(String text) {
        if (text == null) return null;

        Title title = new Title();
        title.text = text;
        return title;
    }

    public String getText() {
        return text;
    }
}
