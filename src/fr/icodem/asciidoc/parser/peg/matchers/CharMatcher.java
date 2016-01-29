package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.MatcherContext;

/**
 * A {@link Matcher matcher} matching a single character
 */
public class CharMatcher implements Matcher {

    private char character;

    public CharMatcher(char c) {
        this.character = c;
    }

    @Override
    public boolean match(MatcherContext context) {

        char currentChar = context.getNextChar();
        if (currentChar == character) {
            return true;
        }

        return false;
    }
}
