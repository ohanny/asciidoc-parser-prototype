package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.MatcherContext;

public class NamedMatcher implements Matcher {

    private String name;
    private Matcher delegate;

    public NamedMatcher(String name, Matcher delegate) {
        this.name = name;
        this.delegate = delegate;
    }

    @Override
    public boolean match(MatcherContext context) {
        return delegate.match(context);
    }

    @Override
    public boolean isOptional() {
        return delegate.isOptional();
    }

    @Override
    public String getLabel() {
        return name;
    }
}
