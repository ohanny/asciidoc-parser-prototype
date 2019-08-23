package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.LiteralBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.inline.Text;

public class LiteralBlockBuilder implements BlockBuilder, TextContainer {

    private AttributeList attList;
    private TitleBuilder title;
    private String text;

    public static LiteralBlockBuilder newBuilder(BlockBuildState state, AttributeList attList) {
        LiteralBlockBuilder literal = new LiteralBlockBuilder();
        literal.attList = attList;
        literal.title = state.consumeBlockTitle();

        return literal;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public LiteralBlock build() {
        return LiteralBlock.of(attList, buildTitle(title), Text.of(text));
    }
}
