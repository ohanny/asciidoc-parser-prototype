package fr.icodem.asciidoc.parser.peg.rules;

import fr.icodem.asciidoc.parser.peg.matchers.*;

/**
 * Created by Olivier on 23/03/2016.
 */
public interface RulesFactory {

    // factory methods
    public static RulesFactory defaultRulesFactory() {
        return new DefaultRulesFactory();
    }

    public static RulesFactory spyingRulesFactory() {
        return new SpyingRulesFactory();
    }

    /**
     * Stores a rule in cache
     * @param name the name of the rule
     * @param rule the rule object to be cached
     * @return the cached rule
     */
    Rule cached(String name, Rule rule);

    Rule cached(String name);

    /**
     * Creates and store a {@link NamedRule named rule}
     * @param name the name of the rule
     * @param delegate the delegate object that provides the matcher
     * @return the named rule
     */
    Rule named(String name, Rule delegate);

    /**
     * Creates and store a {@link NodeRule node rule}
     * @param name the name of the rule
     * @param delegate the delegate object that provides the matcher
     * @return the node rule
     */
    Rule node(String name, Rule delegate);

    /**
     * Creates and store a {@link ProxyRule proxy rule}
     * @param name the name of the rule
     * @return the proxy rule
     */
    Rule proxy(String name);

    Rule wrap(Rule before, Rule inner, Rule after);

    Rule empty();

    Rule any();

    /**
     * Creates a rule that supplies a {@link CharMatcher char matcher}
     * @param c the character to be matched
     * @return the char rule
     */
    Rule ch(char c);

    /**
     * Creates a rule that supplies a {@link CharRangeMatcher 'char range' matcher}
     * @param cLow the lower bound character
     * @param cHigh the upper bound character
     * @return the 'char range' rule
     */
    Rule charRange(char cLow, char cHigh);

    /**
     * Creates a rule that supplies a {@link AnyOfMatcher 'any of' matcher}
     * @param charSet the current char must be in this char set
     * @return the char in set rule
     */
    Rule anyOf(char... charSet);

    /**
     * Creates a rule that supplies a {@link NoneOfMatcher 'none of' matcher}
     * @param charSet the current char must be out of this char set
     * @return the char in set rule
     */
    Rule noneOf(char... charSet);

    /**
     * Creates a rule that supplies a {@link StringMatcher string matcher}
     * @param string the string to be matched
     * @return the string rule
     */
    Rule string(String string);

    /**
     * Creates a rule that supplies a {@link AnyOfStringMatcher 'any of string' matcher}
     * @param stringSet one of the string in the set should be matched
     * @return the 'any of string' rule
     */
    Rule anyOfString(String... stringSet);

    /**
     * Creates a rule that supplies a {@link FirstOfMatcher 'first of' matcher}
     * @param rules the rules to be matched
     * @return the first of rule
     */
    Rule firstOf(Rule... rules);

    /**
     * Creates a rule that supplies a {@link OneOrMoreMatcher 'one or more' matcher}
     * @param rule the rule to be matched
     * @return the one or more rule
     */
    Rule oneOrMore(Rule rule);

    /**
     * Creates a rule that supplies a {@link SequenceMatcher sequence matcher}
     * @param rules the rules to be matched
     * @return the sequence rule
     */
    Rule sequence(Rule... rules);

    /**
     * Creates a rule that supplies a {@link TestMatcher test matcher}
     * @param rule the rule to be matched
     * @return the test rule
     */
    Rule test(Rule rule);

    /**
     * Creates a rule that supplies a {@link TestNotMatcher 'test not' matcher}
     * @param rule the rule to be not matched
     * @return the test rule
     */
    Rule testNot(Rule rule);

    /**
     * Creates a rule that supplies an {@link OptionalMatcher optional matcher}
     * @param rule the rule to be matched
     * @return the zero or more rule
     */
    Rule optional(Rule rule);

    /**
     * Creates a rule that supplies a {@link ZeroOrMoreMatcher 'zero or more' matcher}
     * @param rule the rule to be matched
     * @return the zero or more rule
     */
    Rule zeroOrMore(Rule rule);
}
