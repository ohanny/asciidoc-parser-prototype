package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.peg.rules.Rule;
import fr.icodem.asciidoc.parser.peg.rules.RuleFactory;

/**
 * Base class for parsers. Defines the basic rule creation methods.
 */
public class BaseParser {
    /**
     * Rules instantiation is delegated to a factory
     */
    private RuleFactory factory = new RuleFactory();

    /**
     * Creates a named rule.
     * @see RuleFactory#named(String, Rule)
     */
    protected Rule named(String name, Rule delegate) {
        return factory.named(name, delegate);
    }

    /**
     * Creates a node rule.
     * @see RuleFactory#node(String, Rule)
     */
    protected Rule node(String name, Rule delegate) {
        return factory.node(name, delegate);
    }

    /**
     * Creates a proxy rule.
     * @see RuleFactory#proxy(String)
     */
    protected Rule proxy(String name) {
        return factory.proxy(name);
    }

    /**
     * Creates a char rule.
     * @see RuleFactory#ch(char)
     */
    protected Rule ch(char c) {
        return factory.ch(c);
    }

    /**
     * Creates a first of rule.
     * @see RuleFactory#firstOf(Rule...)
     */
    protected Rule firstOf(Rule... rules) {
        return factory.firstOf(rules);
    }

    /**
     * Creates a one or more rule.
     * @see RuleFactory#oneOreMore(Rule)
     */
    protected Rule oneOrMore(Rule rule) {
        return factory.oneOreMore(rule);
    }

    /**
     * Creates a sequence rule.
     * @see RuleFactory#sequence(Rule...)
     */
    protected Rule sequence(Rule... rules) {
        return factory.sequence(rules);
    }

    /**
     * Creates an optional rule.
     * @see RuleFactory#optional(Rule)
     */
    protected Rule optional(Rule rule) {
        return factory.optional(rule);
    }

    /**
     * Creates a zero or more rule.
     * @see RuleFactory#zeroOrMore(Rule)
     */
    protected Rule zeroOrMore(Rule rule) {
        return factory.zeroOrMore(rule);
    }
}
