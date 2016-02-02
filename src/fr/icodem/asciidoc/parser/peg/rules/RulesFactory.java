package fr.icodem.asciidoc.parser.peg.rules;

import fr.icodem.asciidoc.parser.peg.matchers.*;

/**
 * A factory that creates {@link Rule rule objects}. Named rules are stored in cache.
 */
public class RulesFactory {

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
     * Creates a rule that supplies a {@link CharRangeMatcher 'char range' matcher}
     * @param cLow the lower bound character
     * @param cHigh the upper bound character
     * @return the 'char range' rule
     */
    public Rule charRange(char cLow, char cHigh) {
        String name = "CharRangeRule." + cLow + cHigh;
        return named(name, () -> new CharRangeMatcher(cLow, cHigh));
    }

    /**
     * Creates a rule that supplies a {@link AnyOfMatcher 'any of' matcher}
     * @param charSet the current char must be in this char set
     * @return the char in set rule
     */
    public Rule anyOf(char... charSet) {
        String name = "AnyOfRule.";
        for (char c : charSet) {
            name += c;
        }
        return named(name, () -> new AnyOfMatcher(charSet));
    }

    /**
     * Creates a rule that supplies a {@link NoneOfMatcher 'none of' matcher}
     * @param charSet the current char must be out of this char set
     * @return the char in set rule
     */
    public Rule noneOf(char... charSet) {
        String name = "NoneOfRule.";
        for (char c : charSet) {
            name += c;
        }
        return named(name, () -> new NoneOfMatcher(charSet));
    }

    /**
     * Creates a rule that supplies a {@link StringMatcher string matcher}
     * @param string the string to be matched
     * @return the string rule
     */
    public Rule string(String string) {
        String name = "String." + string;
        return named(name, () -> new StringMatcher(string));
    }

    /**
     * Creates a rule that supplies a {@link AnyOfStringMatcher 'any of string' matcher}
     * @param stringSet one of the string in the set should be matched
     * @return the 'any of string' rule
     */
    public Rule anyOfString(String... stringSet) {
        String name = "AnyOfStringRule.";
        for (String string : stringSet) {
            name += string;
        }
        return named(name, () -> new AnyOfStringMatcher(stringSet));
    }

    /**
     * Creates a rule that supplies a {@link FirstOfMatcher 'first of' matcher}
     * @param rules the rules to be matched
     * @return the first of rule
     */
    public Rule firstOf(Rule... rules) {
        return () -> new FirstOfMatcher(rules);
    }

    /**
     * Creates a rule that supplies a {@link OneOrMoreMatcher 'one or more' matcher}
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
     * Creates a rule that supplies a {@link TestNotMatcher 'test not' matcher}
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
     * Creates a rule that supplies a {@link ZeroOrMoreMatcher 'zero or more' matcher}
     * @param rule the rule to be matched
     * @return the zero or more rule
     */
    public Rule zeroOrMore(Rule rule) {
        return () -> new ZeroOrMoreMatcher(rule);
    }

}
