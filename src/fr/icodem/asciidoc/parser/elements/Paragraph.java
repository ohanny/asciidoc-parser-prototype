package fr.icodem.asciidoc.parser.elements;

@Deprecated
public class Paragraph extends Block {
    private Text text; // TODO revoir

    public Paragraph(AttributeList attList, String text) {
        super(attList);
        this.text = Text.withValue(text);
    }

    public Paragraph(AttributeList attList, Text text) {
        super(attList);
        this.text = text;
    }

    public String getText() {
        return text.getValue();
    }

    public Text.FormattedText getFormattedText() {// TODO oliv
        return (Text.FormattedText) text;
    }
}
