package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.MatcherContext;

public class EmptyMatcher implements Matcher {

    @Override
    public boolean match(MatcherContext context) {
        return true;
    }

    @Override
    public boolean isOptional() {
        return true;
    }

}
