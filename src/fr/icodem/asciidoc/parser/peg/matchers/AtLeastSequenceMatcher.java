package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.MatcherContext;
import fr.icodem.asciidoc.parser.peg.rules.Rule;

/**
 * A {@link Matcher matcher} that executes all of its submatchers
 * in sequence and succeeds only if at least 'min' submatchers succeed.
 */
public class AtLeastSequenceMatcher implements Matcher {

    private Rule[] rules;
    private int min;
    private Matcher[] matchers;// cache

    public AtLeastSequenceMatcher(int min, Rule[] rules) {
        this.min = min;
        this.rules = rules;
        this.matchers = new Matcher[rules.length];
    }

    @Override
    public boolean match(MatcherContext context) {
        context.mark();
        int count = 0;

        for (int i = 0; i < rules.length; i++) {
            Matcher matcher = getSubMatcher(i);
            checkCanFlush(context, i);

            if (!matcher.match(context.getSubContext())) {
                context.reset();
            } else {
                context.mark();
                count++;
            }

            if (context.isCanStartFlushing()) {
                context.requestFlushing();
            }
        }

        return count >= min;
    }

    private Matcher getSubMatcher(int i) {
        Matcher matcher = matchers[i];
        if (matcher == null) {
            matcher = rules[i].getMatcher();
            matchers[i] = matcher;
        }
        return matcher;
    }

    private void checkCanFlush(MatcherContext context, int childIndex) {
        if (context.isCanStartFlushing()) return; // already set

        for (int i = childIndex + 1; i < rules.length; i++) {
            Matcher matcher = getSubMatcher(i);
            if (!matcher.isOptional()) return;
        }

        context.canStartFlushing();
    }

    @Override
    public String getLabel() {
        return "atLeastSequence";
    }
}
