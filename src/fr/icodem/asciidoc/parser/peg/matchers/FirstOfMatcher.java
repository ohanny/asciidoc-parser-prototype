package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.MatcherContext;
import fr.icodem.asciidoc.parser.peg.rules.Rule;

/**
 * A {@link Matcher matcher} trying all of its submatchers in sequence
 * and succeeding when the first submatcher succeeds
 */
public class FirstOfMatcher implements Matcher {

    private Rule[] rules;
    private Matcher[] matchers;// cache

    public FirstOfMatcher(Rule[] rules) {
        this.rules = rules;
        this.matchers = new Matcher[rules.length];
    }

    @Override
    public boolean match(MatcherContext context) {
        context.mark();

        for (int i = 0; i < rules.length; i++) {
            Matcher matcher = getSubMatcher(i);

            if (matcher.match(context.getSubContext())) {
                return true;
            }

            // last match fails, reset
            context.reset();
            context.removeLastSubContext();
        }

        return false;
    }

    private Matcher getSubMatcher(int i) {
        Matcher matcher = matchers[i];
        if (matcher == null) {
            matcher = rules[i].getMatcher();
            matchers[i] = matcher;
        }
        return matcher;
    }

}
