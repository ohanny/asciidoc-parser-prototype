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

    public String getStringAttribute(String parentName, String name, String defaultValue) {
        MatcherContext parent = context.findParentContextNode(parentName);
        return parent != null?context.getStringAttribute(name, defaultValue):defaultValue;
    }

    public int getIntAttribute(String name, int defaultValue) {
        return delegateContext.getIntAttribute(name, defaultValue);
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

    public void setAttributeOnParent(String parentName, String name, Object value) {
        context.setAttributeOnParent(parentName, name, value);
    }
}
