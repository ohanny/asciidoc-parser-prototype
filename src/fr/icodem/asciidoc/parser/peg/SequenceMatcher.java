package fr.icodem.asciidoc.parser.peg;

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
/*            Matcher matcher = matchers[i];
            if (matcher == null) {
                matcher = rules[i].getMatcher();
                matchers[i] = matcher;
            }*/
            if (!matcher.match(context.getSubContext())) {
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


}
