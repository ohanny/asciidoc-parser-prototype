package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.MatcherContext;
import fr.icodem.asciidoc.parser.peg.Rule;

/**
 * A {@link Matcher matcher} that executes all of its submatchers
 * in sequence and succeeds only if all submatchers succeed.
 */
public class SequenceMatcher implements Matcher {

    private Rule[] rules;
    private Matcher[] matchers;// cache

    public SequenceMatcher(Rule[] rules) {
        this.rules = rules;
        this.matchers = new Matcher[rules.length];
    }

    @Override
    public boolean match(MatcherContext context) {

        for (int i = 0; i < rules.length; i++) {
            Matcher matcher = getSubMatcher(i);
            checkCanFlush(context, i);

            if (!matcher.match(context.getSubContext())) {
                context.removeLastSubContext();
                return false;
            }
        }

        return true;
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

}
