package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.MatcherContext;

/**
 * A {@link Matcher matcher} matching a single character
 * out of a given range of characters.
 */
public class CharInRangeMatcher implements Matcher {

    private char cLow;
    private char cHigh;

    public CharInRangeMatcher(char cLow, char cHigh) {
        this.cLow = cLow;
        this.cHigh = cHigh;
    }

    @Override
    public boolean match(MatcherContext context) {

        char currentChar = context.getNextChar();
        if (currentChar < cLow || currentChar > cHigh) {
            return false;
        }

        return true;
    }
}
