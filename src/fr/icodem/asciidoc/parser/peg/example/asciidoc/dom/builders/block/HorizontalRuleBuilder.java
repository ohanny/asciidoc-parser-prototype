package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.HorizontalRule;

public class HorizontalRuleBuilder implements BlockBuilder {

    public static HorizontalRuleBuilder newBuilder() {
        return new HorizontalRuleBuilder();
    }

    @Override
    public HorizontalRule build() {
        return HorizontalRule.newInstance();
    }
}
