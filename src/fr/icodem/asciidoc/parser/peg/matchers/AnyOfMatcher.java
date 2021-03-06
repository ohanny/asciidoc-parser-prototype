package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.MatcherContext;

/**
 * A {@link Matcher matcher} matching a single character
 * out of a given set of characters.
 */
public class AnyOfMatcher implements Matcher {

    private char[] charSet;
    private String label;

    public AnyOfMatcher(char... charSet) {
        this.charSet = charSet;
        this.label = "anyOf [" + new String(charSet) + "]";
    }

    @Override
    public boolean match(MatcherContext context) {

        char currentChar = context.getNextChar();
        for (int i = 0; i < charSet.length; i++) {
            if (currentChar == charSet[i]) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getLabel() {
        return label;
    }

}
