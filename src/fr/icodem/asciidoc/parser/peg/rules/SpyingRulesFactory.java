package fr.icodem.asciidoc.parser.peg.rules;

import fr.icodem.asciidoc.parser.peg.matchers.Matcher;
import fr.icodem.asciidoc.parser.peg.matchers.SpyingMatcher;

public class SpyingRulesFactory extends RulesFactory {

    private Rule getSpyingRule(Rule rule) {
        return () -> {
            Matcher matcher = rule.getMatcher();
            if (matcher instanceof SpyingMatcher) {
                return matcher;
            }
            return new SpyingMatcher(rule.getMatcher());
        };
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
    public Rule proxy(String name) {
        return getSpyingRule(super.proxy(name));
    }

    @Override
    public Rule ch(char c) {
        return getSpyingRule(super.ch(c));
    }

    @Override
    public Rule charInRange(char cLow, char cHigh) {
        return getSpyingRule(super.charInRange(cLow, cHigh));
    }

    @Override
    public Rule charInSet(char... charSet) {
        return getSpyingRule(super.charInSet(charSet));
    }

    @Override
    public Rule string(String string) {
        return getSpyingRule(super.string(string));
    }

    @Override
    public Rule stringInSet(String... stringSet) {
        return getSpyingRule(super.stringInSet(stringSet));
    }

    @Override
    public Rule firstOf(Rule... rules) {
        return getSpyingRule(super.firstOf(rules));
    }

    @Override
    public Rule oneOreMore(Rule rule) {
        return getSpyingRule(super.oneOreMore(rule));
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
}
