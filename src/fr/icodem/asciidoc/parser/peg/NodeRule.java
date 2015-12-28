package fr.icodem.asciidoc.parser.peg;

/**
 * A wrapper rule that allows a delegate to be a node rule.
 * A node rule is necessarily named.
 */
public class NodeRule extends NamedRule {

    public NodeRule(String name, Rule delegate) {
        super(name, delegate);
    }

    /**
     * @see Rule#isNode()
     */
    @Override
    public boolean isNode() {
        return true;
    }
}
