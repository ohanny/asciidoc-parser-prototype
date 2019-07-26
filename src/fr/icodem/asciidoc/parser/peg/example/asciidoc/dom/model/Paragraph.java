package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

public class Paragraph extends TextBlock {
    private String admonition;

    public static Paragraph of(Text text, String admonition) {
        Paragraph p = new Paragraph();
        p.type = ElementType.Paragraph;
        p.text = text;
        p.admonition = admonition;

        return p;
    }

    public String getAdmonition() {
        return admonition;
    }
}
