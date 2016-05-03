package fr.icodem.asciidoc.parser.peg.action;

import fr.icodem.asciidoc.parser.peg.MatcherContext;

public class ActionContext {
    private MatcherContext matcherContext;

    public ActionContext(MatcherContext context) {
        this.matcherContext = context;
    }

    public char[] extract() {
        return matcherContext.extract();
    }

    public void include(Object source) {
        matcherContext.include(source);
    }

}
