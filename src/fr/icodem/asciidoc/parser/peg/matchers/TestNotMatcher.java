package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.MatcherContext;
import fr.icodem.asciidoc.parser.peg.rules.Rule;

/**
 * A special {@link Matcher matcher} not actually matching any input but rather
 * trying its submatcher against the current input position. Succeeds if the
 * submatcher would fail.
 */
public class TestNotMatcher implements Matcher {

    private Rule rule;
    private Matcher matcher; // cache

    public TestNotMatcher(Rule rule) {
        this.rule = rule;
    }

    @Override
    public boolean match(MatcherContext context) {

        if (matcher == null) {
            matcher = rule.getMatcher();
        }
        context.mark();

        boolean matched = !matcher.match(context.getSubContext());

        context.reset();

        return matched;
    }

    @Override
    public String getLabel() {
        return "testNot";
    }
}
