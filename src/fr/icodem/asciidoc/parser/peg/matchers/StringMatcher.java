package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.MatcherContext;

/**
 * A {@link Matcher matcher} matching a string.
 */
public class StringMatcher implements Matcher {

    private String string;

    public StringMatcher(String string) {
        this.string = string;
    }

    @Override
    public boolean match(MatcherContext context) {

        for (int i = 0; i < string.length(); i++) {
            char currentChar = context.getNextChar();
            if (currentChar != string.charAt(i)) {
                return false;
            }
        }

        return true;
    }
}
