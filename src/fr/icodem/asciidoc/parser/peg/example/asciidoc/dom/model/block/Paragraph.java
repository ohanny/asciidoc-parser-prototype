package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ElementType;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.Text;

public class Paragraph extends TextBlock {
    private String admonition;

    public static Paragraph of(AttributeList attList, Title title, Text text, String admonition) {
        Paragraph p = new Paragraph();
        p.type = ElementType.Paragraph;
        p.attributes = attList;
        p.title = title;
        p.text = text;
        p.admonition = admonition;

        return p;
    }

    public String getAdmonition() {
        return admonition;
    }
}
