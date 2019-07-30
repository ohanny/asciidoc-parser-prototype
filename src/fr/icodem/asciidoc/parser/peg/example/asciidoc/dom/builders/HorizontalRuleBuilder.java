package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.HorizontalRule;

public class HorizontalRuleBuilder implements BlockBuilder {

    public static HorizontalRuleBuilder newBuilder() {
        return new HorizontalRuleBuilder();
    }

    @Override
    public HorizontalRule build() {
        return HorizontalRule.newInstance();
    }
}
