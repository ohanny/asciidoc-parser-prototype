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
        if (matcher == null) {
            matcher = rule.getMatcher();
        }

        context.limitTo(limit);

        boolean result = false;

        try {
            result = matcher.match(context);
        } catch (LimitException le) {
            // match fails
        }

        return result;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public boolean isOptional() {
        return matcher.isOptional();
    }
}
