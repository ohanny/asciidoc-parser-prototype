package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.MatcherContext;
import fr.icodem.asciidoc.parser.peg.Rule;

/**
 * A {@link Matcher matcher} that repeatedly tries its
 * submatcher against the input. This matcher always succeeds.
 */
public class ZeroOrMoreMatcher implements Matcher {

    private Rule rule;
    private Matcher matcher; // cache

    public ZeroOrMoreMatcher(Rule rule) {
        this.rule = rule;
    }

    @Override
    public boolean match(MatcherContext context) {

        context.shouldResetIfDirty();

        if (matcher == null) {
            matcher = rule.getMatcher();
        }

        while (true) {
            context.mark();
            if (!matcher.match(context.getSubContext())) {
                context.removeLastSubContext();
                context.reset();

                break;
            }
        }

        return true;
    }

    @Override
    public boolean isOptional() {
        return true;
    }
}
