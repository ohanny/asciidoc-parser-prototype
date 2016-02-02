package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.peg.listeners.ToStringAnalysisBuilder;
import fr.icodem.asciidoc.parser.peg.rules.Rule;
import fr.icodem.asciidoc.parser.peg.rules.RulesFactory;
import fr.icodem.asciidoc.parser.peg.rules.SpyingRulesFactory;

/**
 * Base class for parsers. Defines the basic rule creation methods.
 */
public class BaseParser {
    /**
     * Rules instantiation is delegated to a factory
     */
    private RulesFactory factory = new SpyingRulesFactory(new ToStringAnalysisBuilder());

    /**
     * Creates a named rule.
     * @see RulesFactory#named(String, Rule)
     */
    protected Rule named(String name, Rule delegate) {
        return factory.named(name, delegate);
    }

    /**
     * Creates a node rule.
     * @see RulesFactory#node(String, Rule)
     */
    protected Rule node(String name, Rule delegate) {
        return factory.node(name, delegate);
    }

    /**
     * Creates a proxy rule.
     * @see RulesFactory#proxy(String)
     */
    protected Rule proxy(String name) {
        return factory.proxy(name);
    }

    /**
     * Creates a char rule.
     * @see RulesFactory#ch(char)
     */
    protected Rule ch(char c) {
        return factory.ch(c);
    }

    /**
     * Creates an EOI rule.
     */
    protected Rule eoi() {
        return ch(Chars.EOI);
    }

    /**
     * Creates a 'char range' rule.
     * @see RulesFactory#charRange(char, char)
     */
    protected Rule charRange(char cLow, char cHigh) {
        return factory.charRange(cLow, cHigh);
    }

    /**
     * Creates an 'any of' rule.
     * @see RulesFactory#anyOf(char...)
     */
    protected Rule anyOf(char... charSet) {
        return factory.anyOf(charSet);
    }

    /**
     * Creates a string rule.
     * @see RulesFactory#string(String)
     */
    protected Rule string(String string) {
        return factory.string(string);
    }

    /**
     * Creates an 'any of string' rule.
     * @see RulesFactory#anyOfString(String...)
     */
    protected Rule anyOfString(String... stringSet) {
        return factory.anyOfString(stringSet);
    }

    /**
     * Creates a first of rule.
     * @see RulesFactory#firstOf(Rule...)
     */
    protected Rule firstOf(Rule... rules) {
        return factory.firstOf(rules);
    }

    /**
     * Creates a one or more rule.
     * @see RulesFactory#oneOreMore(Rule)
     */
    protected Rule oneOrMore(Rule rule) {
        return factory.oneOreMore(rule);
    }

    /**
     * Creates a sequence rule.
     * @see RulesFactory#sequence(Rule...)
     */
    protected Rule sequence(Rule... rules) {
        return factory.sequence(rules);
    }

    /**
     * Creates a test rule.
     * @see RulesFactory#test(Rule)
     */
    protected Rule test(Rule rule) {
        return factory.test(rule);
    }

    /**
     * Creates a test not rule.
     * @see RulesFactory#testNot(Rule)
     */
    protected Rule testNot(Rule rule) {
        return factory.testNot(rule);
    }

    /**
     * Creates an optional rule.
     * @see RulesFactory#optional(Rule)
     */
    protected Rule optional(Rule rule) {
        return factory.optional(rule);
    }

    /**
     * Creates a zero or more rule.
     * @see RulesFactory#zeroOrMore(Rule)
     */
    protected Rule zeroOrMore(Rule rule) {
        return factory.zeroOrMore(rule);
    }
}
