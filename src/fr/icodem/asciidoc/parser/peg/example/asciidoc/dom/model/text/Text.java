package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text;

public class Text {
    private String content;

    public static Text of(String content) {
        Text text = new Text();
        text.content = content;

        return text;
    }

    public String getContent() {
        return content;
    }
}
