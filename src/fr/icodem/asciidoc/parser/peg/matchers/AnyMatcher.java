package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.MatcherContext;

public class AnyMatcher implements Matcher {

    @Override
    public boolean match(MatcherContext context) {
        context.getNextChar();

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
