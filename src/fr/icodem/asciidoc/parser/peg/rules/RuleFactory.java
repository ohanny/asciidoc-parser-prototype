package fr.icodem.asciidoc.parser.peg.rules;

import fr.icodem.asciidoc.parser.peg.matchers.*;

/**
 * A factory that creates {@link Rule rule objects}. Named rules are stored in cache.
 */
public class RuleFactory {

    private RulesCache cache = new RulesCache();

    /**
     * Creates and store a {@link NamedRule named rule}
     * @param name the name of the rule
     * @param delegate the delegate object that provides the matcher
     * @return the named rule
     */
    public Rule named(String name, Rule delegate) {
        return cache.get(name, () -> new NamedRule(name, delegate));
    }

    /**
     * Creates and store a {@link NodeRule node rule}
     * @param name the name of the rule
     * @param delegate the delegate object that provides the matcher
     * @return the node rule
     */
    public Rule node(String name, Rule delegate) {
        return cache.get(name, () -> new NodeRule(name, delegate));
    }

    /**
     * Creates and store a {@link ProxyRule proxy rule}
     * @param name the name of the rule
     * @return the proxy rule
     */
    public Rule proxy(String name) {
        String proxyName = "ProxyRule." + name;
        return cache.get(proxyName, () -> new ProxyRule(name, () -> cache.get(name)));
    }

    /**
     * Creates a rule that supplies a {@link CharMatcher char matcher}
     * @param c the character to be matched
     * @return the char rule
     */
    public Rule ch(char c) {
        String name = "CharRule." + c;
        return named(name, () -> new CharMatcher(c));
    }

    /**
     * Creates a rule that supplies a {@link CharInRangeMatcher char in range matcher}
     * @param cLow the lower bound character
     * @param cHigh the upper bound character
     * @return the char in range rule
     */
    public Rule charInRange(char cLow, char cHigh) {
        String name = "CharInRangeRule." + cLow + cHigh;
        return named(name, () -> new CharInRangeMatcher(cLow, cHigh));
    }

    /**
     * Creates a rule that supplies a {@link CharInSetMatcher char in set matcher}
     * @param charSet the current char must be in this char set
     * @return the char in set rule
     */
    public Rule charInSet(char... charSet) {
        String name = "CharInSetRule.";
        for (char c : charSet) {
            name += c;
        }
        return named(name, () -> new CharInSetMatcher(charSet));
    }

    /**
     * Creates a rule that supplies a {@link FirstOfMatcher first of matcher}
     * @param rules the rules to be matched
     * @return the first of rule
     */
    public Rule firstOf(Rule... rules) {
        return () -> new FirstOfMatcher(rules);
    }

    /**
     * Creates a rule that supplies a {@link OneOrMoreMatcher one or more matcher}
     * @param rule the rule to be matched
     * @return the one or more rule
     */
    public Rule oneOreMore(Rule rule) {
        return () -> new OneOrMoreMatcher(rule);
    }

    /**
     * Creates a rule that supplies a {@link SequenceMatcher sequence matcher}
     * @param rules the rules to be matched
     * @return the sequence rule
     */
    public Rule sequence(Rule... rules) {
        return () -> new SequenceMatcher(rules);
    }

    /**
     * Creates a rule that supplies a {@link TestMatcher test matcher}
     * @param rule the rule to be matched
     * @return the test rule
     */
    public Rule test(Rule rule) {
        return () -> new TestMatcher(rule);
    }

    /**
     * Creates a rule that supplies a {@link TestNotMatcher test not matcher}
     * @param rule the rule to be not matched
     * @return the test rule
     */
    public Rule testNot(Rule rule) {
        return () -> new TestNotMatcher(rule);
    }

    /**
     * Creates a rule that supplies an {@link OptionalMatcher optional matcher}
     * @param rule the rule to be matched
     * @return the zero or more rule
     */
    public Rule optional(Rule rule) {
        return () -> new OptionalMatcher(rule);
    }

    /**
     * Creates a rule that supplies a {@link ZeroOrMoreMatcher zero or more matcher}
     * @param rule the rule to be matched
     * @return the zero or more rule
     */
    public Rule zeroOrMore(Rule rule) {
        return () -> new ZeroOrMoreMatcher(rule);
    }

}
