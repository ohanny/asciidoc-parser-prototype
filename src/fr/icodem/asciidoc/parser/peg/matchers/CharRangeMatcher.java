package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.MatcherContext;

/**
 * A {@link Matcher matcher} matching a single character
 * out of a given range of characters.
 */
public class CharRangeMatcher implements Matcher {

    private char cLow;
    private char cHigh;
    private String label;

    public CharRangeMatcher(char cLow, char cHigh) {
        this.cLow = cLow;
        this.cHigh = cHigh;
        this.label = "charRange [" + cLow + ".." + cHigh + "]";
    }

    @Override
    public boolean match(MatcherContext context) {

        char currentChar = context.getNextChar();
        if (currentChar < cLow || currentChar > cHigh) {
            return false;
        }

        return true;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
