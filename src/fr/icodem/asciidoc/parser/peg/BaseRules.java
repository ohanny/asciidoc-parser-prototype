package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.peg.action.Action;
import fr.icodem.asciidoc.parser.peg.rules.Rule;
import fr.icodem.asciidoc.parser.peg.rules.DefaultRulesFactory;
import fr.icodem.asciidoc.parser.peg.rules.RulesFactory;
import fr.icodem.asciidoc.parser.peg.rules.SpyingRulesFactory;

/**
 * Base class for parser rules definition. Defines the basic rule creation methods.
 */
public class BaseRules {
    /**
     * Rules instantiation is delegated to a factory
     */
    private RulesFactory factory;

    public void useFactory(RulesFactory factory) {
        this.factory = factory;
    }

    /**
     * Creates a named rule.
     * @see DefaultRulesFactory#named(String, Rule)
     */
    protected Rule named(String name, Rule delegate) {
        return factory.named(name, delegate);
    }

    /**
     * Creates a node rule.
     * @see DefaultRulesFactory#node(String, Rule)
     */
    protected Rule node(String name, Rule delegate) {
        return factory.node(name, delegate);
    }

    protected Rule node(String name, String nameInCache, Rule delegate) {
        return factory.node(name, nameInCache, delegate);
    }

    /**
     * Creates a cached rule.
     * @see DefaultRulesFactory#cached(String, Rule)
     */
    protected Rule cached(String name, Rule delegate) {
        return factory.cached(name, delegate);
    }

    protected Rule cached(String name) {// TODO same as named() ?
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
     * @see DefaultRulesFactory#proxy(String)
     */
    protected Rule proxy(String name) {
        return factory.proxy(name);
    }

    protected Rule action(Rule rule, Action action) {
        return factory.action(rule, action);
    }

    /**
     * Creates a char rule.
     * @see DefaultRulesFactory#ch(char)
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
     * @see DefaultRulesFactory#charRange(char, char)
     */
    protected Rule charRange(char cLow, char cHigh) {
        return factory.charRange(cLow, cHigh);
    }

    /**
     * Creates an 'any of' rule.
     * @see DefaultRulesFactory#anyOf(char...)
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
     * @see DefaultRulesFactory#noneOf(char...)
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
     * @see DefaultRulesFactory#string(String)
     */
    protected Rule string(String string) {
        return factory.string(string);
    }

    /**
     * Creates an 'any of string' rule.
     * @see DefaultRulesFactory#anyOfString(String...)
     */
    protected Rule anyOfString(String... stringSet) {
        return factory.anyOfString(stringSet);
    }

    /**
     * Creates a 'first of' rule.
     * @see DefaultRulesFactory#firstOf(Rule...)
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
     * @see DefaultRulesFactory#oneOrMore(Rule)
     */
    protected Rule oneOrMore(Rule rule) {
        return factory.oneOrMore(rule);
    }

    protected Rule oneOrMore(char c) {
        return oneOrMore(ch(c));
    }

    /**
     * Creates a sequence rule.
     * @see DefaultRulesFactory#sequence(Rule...)
     */
    protected Rule sequence(Rule... rules) {
        return factory.sequence(rules);
    }

    /**
     * Creates a test rule.
     * @see DefaultRulesFactory#test(Rule)
     */
    protected Rule test(Rule rule) {
        return factory.test(rule);
    }

    /**
     * Creates a 'test not' rule.
     * @see DefaultRulesFactory#testNot(Rule)
     */
    protected Rule testNot(Rule rule) {
        return factory.testNot(rule);
    }

    /**
     * Creates an optional rule.
     * @see DefaultRulesFactory#optional(Rule)
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
     * @see DefaultRulesFactory#zeroOrMore(Rule)
     */
    protected Rule zeroOrMore(Rule rule) {
        return factory.zeroOrMore(rule);
    }

    protected Rule zeroOrMore(char c) {
        return zeroOrMore(ch(c));
    }

    protected Rule limitTo(Rule rule, int limit) {
        return factory.limitTo(rule, limit);
    }

    protected Rule trace(String message) {
        String nameInCache = "trace [" + message +"]";
        if (isCached(nameInCache)) return cached(nameInCache);

        return cached(nameInCache,() -> ctx -> {
            System.out.println(message);
            return true;
        });
    }

    private Rule[] toRule(char[] chars) {
        Rule[] rules = new Rule[chars.length];
        for (int i = 0; i < chars.length; i++) {
            rules[i] = ch(chars[i]);
        }
        return rules;
    }

}
