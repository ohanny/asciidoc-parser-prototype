package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.MatcherContext;

public class NodeMatcher implements Matcher {

    private String name;
    private String label;
    private Matcher delegate;
    private boolean skipText;

    public NodeMatcher(String name, Matcher delegate, boolean skipText) {
        this.name = name;
        this.label = "node [" + name + "]";
        this.delegate = delegate;
        this.skipText = skipText;
    }

    @Override
    public boolean match(MatcherContext context) {
        context.setNodeName(name);
        context.setSkipText(skipText);
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

    @Override
    public String getLabel() {
        return label;
    }
}
