package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.MatcherContext;
import fr.icodem.asciidoc.parser.peg.rules.Rule;

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

        if (matcher == null) {
            matcher = rule.getMatcher();
        }

        context.canStartFlushing();

        while (true) {
            context.mark();
            if (!matcher.match(context.getSubContext())) {
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

    @Override
    public String getLabel() {
        return "zeroOrMore";
    }
}
