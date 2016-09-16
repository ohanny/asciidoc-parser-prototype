package fr.icodem.asciidoc.parser.peg.rules;

import fr.icodem.asciidoc.parser.peg.matchers.Matcher;
import fr.icodem.asciidoc.parser.peg.matchers.NodeMatcher;

/**
 * A wrapper rule that allows a delegate to be a node rule.
 * A node rule is necessarily named.
 */
public class NodeRule extends NamedRule {

    private boolean skipText;

    public NodeRule(String name, Rule delegate) {
        this(name, false, delegate);
    }

    public NodeRule(String name, boolean skipText, Rule delegate) {
        super(name, delegate);
        this.skipText = skipText;
    }

    @Override
    public Matcher getMatcher() {
        if (matcher == null) {
            this.matcher = new NodeMatcher(name, super.getMatcher(), skipText);
        }

        return matcher;
    }

}
