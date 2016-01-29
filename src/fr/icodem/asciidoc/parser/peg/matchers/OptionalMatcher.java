package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.MatcherContext;
import fr.icodem.asciidoc.parser.peg.rules.Rule;

public class OptionalMatcher implements Matcher {

    private Rule rule;
    private Matcher matcher; // cache

    public OptionalMatcher(Rule rule) {
        this.rule = rule;
    }

    @Override
    public boolean match(MatcherContext context) {

        if (matcher == null) {
            matcher = rule.getMatcher();
        }

        context.mark();
        if (!matcher.match(context.getSubContext())) {
            context.removeLastSubContext();
            context.reset();
        }

        return true;
    }

    @Override
    public boolean isOptional() {
        return true;
    }
}
