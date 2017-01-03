package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.Chars;
import fr.icodem.asciidoc.parser.peg.MatcherContext;

/**
 * A {@link Matcher matcher} matching a single character n times
 */
public class TimesMatcher implements Matcher {

    private char character;
    private int times;
    private String label;

    public TimesMatcher(char c, int times) {
        this.character = c;
        this.times = times;
        this.label = "times [" + Chars.toString(character) + ", " + times + "]";
    }

    @Override
    public boolean match(MatcherContext context) {

        for (int i = 0; i < times; i++) {
            char currentChar = context.getNextChar();
            if (currentChar != character) {
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
