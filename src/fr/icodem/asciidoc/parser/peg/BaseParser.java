package fr.icodem.asciidoc.parser.peg;

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
    private RulesFactory factory = new SpyingRulesFactory();

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
     * Creates a cached rule.
     * @see RulesFactory#cached(String, Rule)
     */
    protected Rule cached(String name, Rule delegate) {
        return factory.cached(name, delegate);
    }

    protected Rule cached(String name) {
        return factory.cached(name);
    }

    protected boolean isCached(String name) {
        return cached(name) != null;
    }

    protected Rule wrap(Rule before, Rule inner) {
        return wrap(before, inner, null);
    }

    protected Rule wrap(Rule before, Rule inner, Rule after) {
        return factory.wrap(before, inner, after);
    }

    protected Rule empty() {
        return factory.empty();
    }

    protected Rule any() {
        return factory.any();
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
     * Creates an 'any of' rule with a string argument.
     */
    protected Rule anyOf(String charSet) {
        return anyOf(charSet.toCharArray());
    }

    /**
     * Creates a 'none of' rule.
     * @see RulesFactory#noneOf(char...)
     */
    protected Rule noneOf(char... charSet) {
        return factory.noneOf(charSet);
    }

    /**
     * Creates a 'none of' rule with a string argument.
     */
    protected Rule noneOf(String charSet) {
        return noneOf(charSet.toCharArray());
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
     * Creates a 'first of' rule.
     * @see RulesFactory#firstOf(Rule...)
     */
    protected Rule firstOf(Rule... rules) {
        return factory.firstOf(rules);
    }

    /**
     * Creates a 'first of' rule with characters.
     */
    protected Rule firstOf(char... chars) {
        return firstOf(toRule(chars));
    }

    /**
     * Creates a 'first of' rule with string.
     */
    protected Rule firstOf(String chars) {
        return firstOf(toRule(chars.toCharArray()));
    }

    /**
     * Creates a 'one or more' rule.
     * @see RulesFactory#oneOrMore(Rule)
     */
    protected Rule oneOrMore(Rule rule) {
        return factory.oneOrMore(rule);
    }

    protected Rule oneOrMore(char c) {
        return oneOrMore(ch(c));
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
     * Creates a 'test not' rule.
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
     * Creates an optional rule with character.
     */
    protected Rule optional(char c) {
        return optional(ch(c));
    }

    /**
     * Creates a 'zero or more' rule.
     * @see RulesFactory#zeroOrMore(Rule)
     */
    protected Rule zeroOrMore(Rule rule) {
        return factory.zeroOrMore(rule);
    }

    private Rule[] toRule(char[] chars) {
        Rule[] rules = new Rule[chars.length];
        for (int i = 0; i < chars.length; i++) {
            rules[i] = ch(chars[i]);
        }
        return rules;
    }

}
