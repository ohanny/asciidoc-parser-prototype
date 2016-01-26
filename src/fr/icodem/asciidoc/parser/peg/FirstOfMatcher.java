package fr.icodem.asciidoc.parser.peg;

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

        boolean shouldReset = false;
        for (int i = 0; i < rules.length; i++) {
            Matcher matcher = getSubMatcher(i);

            if (shouldReset) { // if last match fails, reset will be done by parent
                context.reset();
            }
            if (matcher.match(context.getSubContext())) {
                return true;
            }

            context.removeLastSubContext();
            shouldReset = true;
        }


/*
        for (Rule rule : rules) {
            Matcher matcher = rule.getMatcher();
            if (shouldReset) { // if last match fails, reset will be done by parent
                context.reset();
            }
            if (matcher.match(context.getSubContext())) {
                return true;
            }

            shouldReset = true;
        }
*/

        context.release();

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
