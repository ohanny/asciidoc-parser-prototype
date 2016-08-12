package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.MatcherContext;
import fr.icodem.asciidoc.parser.peg.exception.LimitException;
import fr.icodem.asciidoc.parser.peg.rules.Rule;

public class LimitToMatcher implements Matcher {

    private int limit;
    private String label;
    private Rule rule;
    private Matcher matcher; // cache

    public LimitToMatcher(Rule rule, int limit) {
        this.rule = rule;
        this.limit = limit;
        this.label = "limitTo [" + limit + "]";
    }

    @Override
    public boolean match(MatcherContext context) {
        context.limitTo(limit);

        boolean result = false;

        try {
            result = getMatcher().match(context);
        } catch (LimitException le) {
            // match fails
        }

        return result;
    }

    private Matcher getMatcher() { // TODO add a compile phase so that matcher is instanciated before use
        if (matcher == null) {
            matcher = rule.getMatcher();
        }
        return matcher;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public boolean isOptional() {
        return getMatcher().isOptional();
    }
}
