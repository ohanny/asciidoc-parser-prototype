package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.LiteralBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Text;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Title;

public class LiteralBlockBuilder implements BlockBuilder, TextContainer {

    private AttributeList attList;
    private String title;
    private String text;

    public static LiteralBlockBuilder newBuilder(AttributeList attList, String title) {
        LiteralBlockBuilder literal = new LiteralBlockBuilder();
        literal.attList = attList;
        literal.title = title;

        return literal;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public LiteralBlock build() {
        return LiteralBlock.of(attList, Title.of(title), Text.of(text));
    }
}
