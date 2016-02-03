package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.Chars;
import fr.icodem.asciidoc.parser.peg.MatcherContext;

import java.util.Arrays;

/**
 * A {@link Matcher matcher} matching all characters
 * except the ones in the given char array and EOI.
 */
public class NoneOfMatcher implements Matcher {

    private char[] charSet;
    private String label;

    public NoneOfMatcher(char... charSet) {
        this.charSet = Arrays.copyOf(charSet, charSet.length + 1);
        this.charSet[charSet.length - 1] = Chars.EOI;
        this.label = "noneOf [" + Chars.toString(charSet) + "]";
    }

    @Override
    public boolean match(MatcherContext context) {

        char currentChar = context.getNextChar();
        for (int i = 0; i < charSet.length; i++) {
            if (currentChar == charSet[i]) {
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
