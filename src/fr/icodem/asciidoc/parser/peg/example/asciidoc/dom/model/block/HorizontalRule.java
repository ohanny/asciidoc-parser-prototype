package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ElementType;

public class HorizontalRule extends Block {

    public static HorizontalRule newInstance() {
        HorizontalRule rule = new HorizontalRule();
        rule.type = ElementType.HorizontalRule;

        return rule;
    }
}
