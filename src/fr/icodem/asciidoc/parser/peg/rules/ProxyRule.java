package fr.icodem.asciidoc.parser.peg.rules;

import fr.icodem.asciidoc.parser.peg.matchers.Matcher;

/**
 * A wrapper rule that delegates matching to another rule object.
 * The instantiation of the delegate is deferred. That allows
 * to create recursive rules without leading to a stack overflow.
 */
public class ProxyRule implements Rule {

    private String name;
    private Rule delegate;
    private RuleSupplier supplier;

    public ProxyRule(String name, RuleSupplier supplier) {
        this.name = name;
        this.supplier = supplier;

    }

    private Rule getDelegate() {
        if (delegate == null) {
            delegate = supplier.getRule();
        }
        return delegate;
    }

    @Override
    public Matcher getMatcher() {
        return getDelegate().getMatcher();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isNode() {
        return getDelegate().isNode();
    }
}
