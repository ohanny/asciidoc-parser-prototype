package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.Chars;
import fr.icodem.asciidoc.parser.peg.MatcherContext;

/**
 * A {@link Matcher matcher} matching a single character
 */
public class CharMatcher implements Matcher {

    private char character;
    private String label;

    public CharMatcher(char c) {
        this.character = c;
        this.label = "ch [" + Chars.toString(character) + "]";
    }

    @Override
    public boolean match(MatcherContext context) {

        char currentChar = context.getNextChar();
        if (currentChar == character) {
            return true;
        }

        return false;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
