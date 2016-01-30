package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.MatcherContext;

/**
 * A {@link Matcher matcher} matching a string
 * out of a given set of strings.
 */
public class StringInSetMatcher implements Matcher {

    private String[] stringSet;

    public StringInSetMatcher(String... stringSet) {
        this.stringSet = stringSet;
    }

    @Override
    public boolean match(MatcherContext context) {

        main:
        for (String string : stringSet) {
            context.mark();
            for (int j = 0; j < string.length(); j++) {
                char currentChar = context.getNextChar();
                if (currentChar != string.charAt(j)) {
                    context.reset();
                    continue main;
                }
            }

            return true;
        }

        return false;
    }
}
