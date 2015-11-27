package fr.icodem.asciidoc.parser.elements;

public class Paragraph extends Block {
    private String text;

    public Paragraph(AttributeList attList, String text) {
        super(attList);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
