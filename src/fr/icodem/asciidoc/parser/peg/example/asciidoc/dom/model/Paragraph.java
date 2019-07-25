package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

public class Paragraph extends TextBlock {

    public static Paragraph of(Text text) {
        Paragraph p = new Paragraph();
        p.type = ElementType.Paragraph;
        p.text = text;

        return p;
    }

}
