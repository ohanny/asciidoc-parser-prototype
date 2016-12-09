package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.Chars;
import fr.icodem.asciidoc.parser.peg.MatcherContext;

/**
 * A {@link Matcher matcher} matching a single character at least n times
 */
public class AtLeastMatcher implements Matcher {

    private char character;
    private int times;
    private String label;

    public AtLeastMatcher(char c, int times) {
        this.character = c;
        this.times = times;
        this.label = "atLeast [" + Chars.toString(character) + ", " + times + "]";
    }

    @Override
    public boolean match(MatcherContext context) {

        for (int i = 0; i < times; i++) {
            char currentChar = context.getNextChar();
            if (currentChar != character) {
                return false;
            }
        }

        while (true) {
            context.mark();
            char currentChar = context.getNextChar();
            if (currentChar != character) {
                context.reset();
                break;
            }
        }

        return true;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
