package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text;

public class Text {
    private String source;
    private InlineNode inline;

    public static Text of(String source) {
        Text text = new Text();
        text.source = source;

        return text;
    }

    public static Text of(String source, InlineNode inline) {
        Text text = of(source);
        text.inline = inline;

        return text;
    }

    public String getSource() {
        return source;
    }

    public InlineNode getInline() {
        return inline;
    }
}
