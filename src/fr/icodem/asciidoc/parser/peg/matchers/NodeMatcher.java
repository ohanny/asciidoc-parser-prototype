package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.MatcherContext;

public class NodeMatcher implements Matcher {

    private String name;
    private Matcher delegate;

    public NodeMatcher(String name, Matcher delegate) {
        this.name = name;
        this.delegate = delegate;
    }

    @Override
    public boolean match(MatcherContext context) {
        context.setNodeName(name);
        context.canStartFlushing();

        boolean matched = delegate.match(context.getSubContext());

        if (matched) {
            context.matched();
            context.requestFlushing();
        }

        return matched;
    }

    @Override
    public boolean isOptional() {
        return delegate.isOptional();
    }
}