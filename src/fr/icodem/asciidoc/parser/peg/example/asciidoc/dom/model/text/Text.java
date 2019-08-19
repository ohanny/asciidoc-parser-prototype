package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text;

public class Text {
    private String source;

    public static Text of(String source) {
        Text text = new Text();
        text.source = source;

        return text;
    }

    public String getSource() {
        return source;
    }
}
