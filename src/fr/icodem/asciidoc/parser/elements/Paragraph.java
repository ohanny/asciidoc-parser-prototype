package fr.icodem.asciidoc.parser.elements;

import fr.icodem.asciidoc.parser.Text;

public class Paragraph extends Block {
    private Text text;

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
}
