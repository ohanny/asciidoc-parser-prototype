package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.MatcherContext;
import fr.icodem.asciidoc.parser.peg.rules.Rule;

public class WrapperMatcher implements Matcher {

    private Rule before;
    private Rule inner;
    private Rule after;
    private Matcher matcherBefore; // cache
    private Matcher matcherInner; // cache
    private Matcher matcherAfter; // cache

    public WrapperMatcher(Rule before, Rule inner, Rule after) {
        this.before = before;
        this.inner = inner;
        this.after = after;
    }

    @Override
    public boolean match(MatcherContext context) {

        boolean matched = getMatcherBefore().match(context)
                && getMatcherInner().match(context) && getMatcherAfter().match(context);

        return matched;
    }

    public Matcher getMatcherBefore() {
        if (matcherBefore == null) {
            matcherBefore = before.getMatcher();
        }
        return matcherBefore;
    }

    public Matcher getMatcherInner() {
        if (matcherInner == null) {
            matcherInner = inner.getMatcher();
        }
        return matcherInner;
    }

    public Matcher getMatcherAfter() {
        if (matcherAfter == null) {
            matcherAfter = after.getMatcher();
        }
        return matcherAfter;
    }

    @Override
    public boolean isOptional() {
        return getMatcherBefore().isOptional() && getMatcherInner().isOptional()
                && getMatcherAfter().isOptional();
    }

}
