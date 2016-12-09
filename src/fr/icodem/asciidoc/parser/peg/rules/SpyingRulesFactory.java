package fr.icodem.asciidoc.parser.peg.rules;

import fr.icodem.asciidoc.parser.peg.action.Action;
import fr.icodem.asciidoc.parser.peg.matchers.Matcher;
import fr.icodem.asciidoc.parser.peg.matchers.SpyingMatcher;

public class SpyingRulesFactory extends DefaultRulesFactory {

    public SpyingRulesFactory() {}

    private Rule getSpyingRule(Rule rule) {
        return () -> {
            Matcher matcher = rule.getMatcher();
            if (matcher instanceof SpyingMatcher) {
                return matcher;
            }
            return new SpyingMatcher(rule.getMatcher());
        };
    }

    /**
     * The spying rule factory promotes cached instance to named rule
     * @param name the name of the rule
     * @param rule the rule object to be cached
     * @return the spying rule wrapping the rule argument
     */
    @Override
    public Rule cached(String name, Rule rule) {
        return named(name, rule);
    }

    @Override
    public Rule named(String name, Rule delegate) {
        return getSpyingRule(super.named(name, delegate));
    }

    @Override
    public Rule node(String name, Rule delegate) {
        return getSpyingRule(super.node(name, delegate));
    }

    @Override
    public Rule node(String name, boolean skipText, Rule delegate) {
        return getSpyingRule(super.node(name, skipText, delegate));
    }

    @Override
    public Rule node(String name, String nameInCache, Rule delegate) {
        return getSpyingRule(super.node(name, nameInCache, delegate));
    }

    @Override
    public Rule proxy(String name) {
        return getSpyingRule(super.proxy(name));
    }

    @Override
    public Rule action(Rule rule, Action action) {
        return getSpyingRule(new ActionRule(rule, action));
    }

    @Override
    public Rule wrap(Rule before, Rule inner, Rule after) {
        return getSpyingRule(super.wrap(before, inner, after));
    }

    @Override
    public Rule empty() {
        return getSpyingRule(super.empty());
    }

    @Override
    public Rule any() {
        return getSpyingRule(super.any());
    }

    @Override
    public Rule ch(char c) {
        return getSpyingRule(super.ch(c));
    }

    @Override
    public Rule charRange(char cLow, char cHigh) {
        return getSpyingRule(super.charRange(cLow, cHigh));
    }

    @Override
    public Rule anyOf(char... charSet) {
        return getSpyingRule(super.anyOf(charSet));
    }

    @Override
    public Rule noneOf(char... charSet) {
        return getSpyingRule(super.noneOf(charSet));
    }

    @Override
    public Rule atLeast(char c, int times) {
        return getSpyingRule(super.atLeast(c, times));
    }

    @Override
    public Rule string(String string) {
        return getSpyingRule(super.string(string));
    }

    @Override
    public Rule anyOfString(String... stringSet) {
        return getSpyingRule(super.anyOfString(stringSet));
    }

    @Override
    public Rule firstOf(Rule... rules) {
        return getSpyingRule(super.firstOf(rules));
    }

    @Override
    public Rule oneOrMore(Rule rule) {
        return getSpyingRule(super.oneOrMore(rule));
    }

    @Override
    public Rule sequence(Rule... rules) {
        return getSpyingRule(super.sequence(rules));
    }

    @Override
    public Rule test(Rule rule) {
        return getSpyingRule(super.test(rule));
    }

    @Override
    public Rule testNot(Rule rule) {
        return getSpyingRule(super.testNot(rule));
    }

    @Override
    public Rule optional(Rule rule) {
        return getSpyingRule(super.optional(rule));
    }

    @Override
    public Rule zeroOrMore(Rule rule) {
        return getSpyingRule(super.zeroOrMore(rule));
    }

    @Override
    public Rule limitTo(Rule rule, int limit) {
        return getSpyingRule(super.limitTo(rule, limit));
    }
}
