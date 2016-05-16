package fr.icodem.asciidoc.parser.peg.action;

import fr.icodem.asciidoc.parser.peg.MatcherContext;

import java.util.Map;

public class ActionContext {
    private MatcherContext context;
    private MatcherContext delegateContext;

    private int start;
    private int end;

    public ActionContext(MatcherContext context, MatcherContext delegateContext) {
        this.context = context;
        this.delegateContext = delegateContext;
    }

    public void markStart() {
        start =  context.getPosition() + 1;
    }

    public void markEnd() {
        end =  context.getPosition();
    }

    public char[] extract() {
        return context.extract(start, end);
    }

    public void include(Object source) {
        context.include(source);
    }

    public void exportAttributesToParentNode(String prefix) {
        String realPrefix = (prefix == null || prefix.isEmpty())?"":prefix + ".";
        MatcherContext parentContext = context.findParentContextNode();
        if (parentContext != null) {
            Map<String, Object> delegateAtts = delegateContext.getAttributes();
            delegateAtts.entrySet()
                    .stream()
                    .forEach(entry -> parentContext.setAttribute(realPrefix + entry.getKey(), entry.getValue()));
        }
    }

    public void setAttribute(String name, Object value) {
        delegateContext.setAttribute(name, value);
    }

}
