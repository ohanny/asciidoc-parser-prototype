package fr.icodem.asciidoc.parser.peg;

/**
 * A {@link Matcher matcher} that repeatedly tries its
 * submatcher against the input. Always succeeds.
 */
public class ZeroOrMoreMatcher implements Matcher {

    private Rule rule;
    private Matcher matcher; // cache

    public ZeroOrMoreMatcher(Rule rule) {
        this.rule = rule;
    }

    @Override
    public boolean match(MatcherContext context) {

        context.shouldResetIfDirty();

        if (matcher == null) {
            matcher = rule.getMatcher();
        }

        while (true) {
            context.mark();
            if (!matcher.match(context.getSubContext())) {
                break;
            }
        }

        context.reset();
        context.release();

        return true;
    }

}
