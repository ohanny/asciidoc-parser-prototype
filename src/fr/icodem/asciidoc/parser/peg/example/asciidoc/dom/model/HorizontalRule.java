package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

public class HorizontalRule extends Block {

    public static HorizontalRule newInstance() {
        HorizontalRule rule = new HorizontalRule();
        rule.type = ElementType.HorizontalRule;

        return rule;
    }
}
