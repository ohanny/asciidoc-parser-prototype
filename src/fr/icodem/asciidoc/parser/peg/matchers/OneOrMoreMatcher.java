package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.MatcherContext;
import fr.icodem.asciidoc.parser.peg.rules.Rule;

/**
 * A {@link Matcher matcher} that repeatedly tries its submatcher
 * against the input. Succeeds if its submatcher succeeds at least once.
 */
public class OneOrMoreMatcher implements Matcher {

    private Rule rule;
    private Matcher matcher; // cache

    public OneOrMoreMatcher(Rule rule) {
        this.rule = rule;
    }

    @Override
    public boolean match(MatcherContext context) {

        if (matcher == null) {
            matcher = rule.getMatcher();
        }

        context.mark();

        if (!matcher.match(context.getSubContext())) {
            context.reset();
            return false;
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

//    private void checkCanFlush(MatcherContext context, int childIndex) {
//        if (context.isCanStartFlushing()) return; // already set
//
//        for (int i = childIndex + 1; i < rules.length; i++) {
//            Matcher matcher = getSubMatcher(i);
//            if (!matcher.isOptional()) return;
//        }
//
//        context.canStartFlushing();
//    }


}
