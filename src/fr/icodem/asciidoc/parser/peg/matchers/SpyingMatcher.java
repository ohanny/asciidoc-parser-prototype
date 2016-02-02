package fr.icodem.asciidoc.parser.peg.matchers;

import fr.icodem.asciidoc.parser.peg.MatcherContext;
import fr.icodem.asciidoc.parser.peg.listeners.ParsingProcessListener;

public class SpyingMatcher implements Matcher {

    private Matcher delegate; // delegate

    public SpyingMatcher(Matcher delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean match(MatcherContext context) {
        ParsingProcessListener listener = context.getParsingProcessListener();
        listener.matcherStart(delegate);
        boolean match = delegate.match(context);
        listener.matcherEnd(delegate, match);

        return match;
    }

    @Override
    public boolean isOptional() {
        return delegate.isOptional();
    }
}
