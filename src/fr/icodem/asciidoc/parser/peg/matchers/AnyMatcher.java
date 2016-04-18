package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.Chars;
import fr.icodem.asciidoc.parser.peg.MatcherContext;

public class AnyMatcher implements Matcher {

    @Override
    public boolean match(MatcherContext context) {
        char c = context.getNextChar();
        if (c == Chars.EOI) {
            return false;
        }

        return true;
    }

    @Override
    public boolean isOptional() {
        return true;
    }

    @Override
    public String getLabel() {
        return "any";
    }
}
