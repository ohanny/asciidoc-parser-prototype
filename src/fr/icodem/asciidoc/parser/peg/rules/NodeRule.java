package fr.icodem.asciidoc.parser.peg.rules;

import fr.icodem.asciidoc.parser.peg.matchers.Matcher;
import fr.icodem.asciidoc.parser.peg.matchers.NodeMatcher;

/**
 * A wrapper rule that allows a delegate to be a node rule.
 * A node rule is necessarily named.
 */
public class NodeRule extends NamedRule {

    public NodeRule(String name, Rule delegate) {
        super(name, delegate);
    }

    @Override
    public Matcher getMatcher() {
        if (matcher == null) {
            this.matcher = new NodeMatcher(name, super.getMatcher());
        }

        return matcher;
    }

    /**
     * @see Rule#isNode()
     */
    @Override
    public boolean isNode() {
        return true;
    }
}
