package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.Chars;
import fr.icodem.asciidoc.parser.peg.MatcherContext;

/**
 * A {@link Matcher matcher} matching a rule n times
 */
public class TimesMatcher implements Matcher {

    private Matcher matcher;
    private int times;
    private String label;

    public TimesMatcher(char c, int times) {
        this.matcher = new CharMatcher(c);
        this.times = times;
        this.label = "times [" + Chars.toString(c) + ", " + times + "]";
    }

    public TimesMatcher(Matcher matcher, int times) {
        this.matcher = matcher;
        this.times = times;
        this.label = "times [" + matcher.getLabel() + ", " + times + "]";
    }

    @Override
    public boolean match(MatcherContext context) {

        for (int i = 0; i < times; i++) {
            if (!matcher.match(context)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
