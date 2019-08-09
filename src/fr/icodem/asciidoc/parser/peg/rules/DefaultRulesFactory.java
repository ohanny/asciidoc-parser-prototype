package fr.icodem.asciidoc.parser.peg.rules;

import fr.icodem.asciidoc.parser.peg.action.Action;
import fr.icodem.asciidoc.parser.peg.matchers.*;

/**
 * A factory that creates {@link Rule rule objects}. Named rules are stored in cache.
 */
public class DefaultRulesFactory implements RulesFactory {

    private RulesCache cache = new RulesCache();

    @Override
    public Rule cached(String name, Rule rule) {
        return cachedInternal(name, rule);
    }

    private Rule cachedInternal(String name, Rule rule) {
        return cache.get(name, () -> rule);
    }

    @Override
    public Rule cached(String name) {
        return cache.get(name);
    }

    @Override
    public Rule named(String name, Rule delegate) {
        return cache.get(name, () -> new NamedRule(name, delegate));
    }

    @Override
    public Rule node(String name, Rule delegate) {// TODO throw exception if name is empty or already used
        return cache.get(name, () -> new NodeRule(name, delegate));
    }

    @Override
    public Rule node(String name, boolean skipText, Rule delegate) {// TODO throw exception if name is empty or already used
        return cache.get(name, () -> new NodeRule(name, skipText, delegate));
    }

    @Override
    public Rule node(String name, String nameInCache, Rule delegate) {// TODO throw exception if name is empty or already used
        return cache.get(nameInCache, () -> new NodeRule(name, delegate));
    }


    @Override
    public Rule proxy(String name) {
        String proxyName = "ProxyRule." + name;
        return cache.get(proxyName, () -> new ProxyRule(name, () -> cache.get(name)));
    }

    @Override
    public Rule action(Rule rule, Action action) {
        return new ActionRule(rule, action);
    }

    @Override
    public Rule wrap(Rule before, Rule inner, Rule after) {
        Rule ruleBefore = (before == null)?empty():before;
        Rule ruleAfter = (after == null)?empty():after;
        return () -> new WrapperMatcher(ruleBefore, inner, ruleAfter);
    }

    @Override
    public Rule empty() {
        return cachedInternal("EmptyRule", () -> new EmptyMatcher());
    }

    @Override
    public Rule any() {
        return cachedInternal("AnyRule", () -> new AnyMatcher());
    }

    @Override
    public Rule ch(char c) {
        String name = "CharRule." + c;
        return cachedInternal(name, () -> new CharMatcher(c));
    }

    @Override
    public Rule charRange(char cLow, char cHigh) {
        String name = "CharRangeRule." + cLow + cHigh;
        return cachedInternal(name, () -> new CharRangeMatcher(cLow, cHigh));
    }

    @Override
    public Rule anyOf(char... charSet) {
        String name = "AnyOfRule.";
        for (char c : charSet) {
            name += c;
        }
        return cachedInternal(name, () -> new AnyOfMatcher(charSet));
    }

    @Override
    public Rule noneOf(char... charSet) {
        String name = "NoneOfRule.";
        for (char c : charSet) {
            name += c;
        }
        return cachedInternal(name, () -> new NoneOfMatcher(charSet));
    }

    @Override
    public Rule atLeast(char c, int times) {
        String name = "AtLeastRule." + c + "." + times;
        return cachedInternal(name, () -> new AtLeastMatcher(c, times));
    }

    @Override
    public Rule times(char c, int times) {
        String name = "TimesRule." + c + "." + times;
        return cachedInternal(name, () -> new TimesMatcher(c, times));
    }

    @Override
    public Rule times(Rule rule, int times) {
        String name = "TimesRule." + rule.getName() + "." + times;
        return cachedInternal(name, () -> new TimesMatcher(rule.getMatcher(), times));
    }

    @Override
    public Rule string(String string) {
        String name = "String." + string;
        return cachedInternal(name, () -> new StringMatcher(string));
    }

    @Override
    public Rule anyOfString(String... stringSet) {
        String name = "AnyOfStringRule.";
        for (String string : stringSet) {
            name += string;
        }
        return cachedInternal(name, () -> new AnyOfStringMatcher(stringSet));
    }

    @Override
    public Rule firstOf(Rule... rules) {
        return () -> new FirstOfMatcher(rules);
    }

    @Override
    public Rule oneOrMore(Rule rule) {
        return () -> new OneOrMoreMatcher(rule);
    }

    @Override
    public Rule sequence(Rule... rules) {
        return () -> new SequenceMatcher(rules);
    }

    @Override
    public Rule atLeastSequence(int min, Rule... rules) {
        return () -> new AtLeastSequenceMatcher(min, rules);
    }

    @Override
    public Rule test(Rule rule) {
        return () -> new TestMatcher(rule);
    }

    @Override
    public Rule testNot(Rule rule) {
        return () -> new TestNotMatcher(rule);
    }

    @Override
    public Rule optional(Rule rule) {
        return () -> new OptionalMatcher(rule);
    }

    @Override
    public Rule zeroOrMore(Rule rule) {
        return () -> new ZeroOrMoreMatcher(rule);
    }

    @Override
    public Rule limitTo(Rule rule, int limit) {
        return () -> new LimitToMatcher(rule, limit);
    }

}
