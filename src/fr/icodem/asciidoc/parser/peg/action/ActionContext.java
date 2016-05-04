package fr.icodem.asciidoc.parser.peg.action;

import fr.icodem.asciidoc.parser.peg.MatcherContext;

public class ActionContext {
    private MatcherContext matcherContext;

    private int start;
    private int end;

    public ActionContext(MatcherContext context) {
        this.matcherContext = context;
    }

    public void markStart() {
        start =  matcherContext.getPosition() + 1;
    }

    public void markEnd() {
        end =  matcherContext.getPosition();
    }

    public char[] extract() {
        return matcherContext.extract(start, end);
    }

    public void include(Object source) {
        matcherContext.include(source);
    }

}
