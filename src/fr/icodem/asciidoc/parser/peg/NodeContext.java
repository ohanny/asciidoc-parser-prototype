package fr.icodem.asciidoc.parser.peg;

public class NodeContext {

    private String nodeName;
    private MatcherContext context;

    public NodeContext(String nodeName, MatcherContext context) {
        this.nodeName = nodeName;
        this.context = context;
    }

    public void include(Object source) {
        context.include(source);
    }

    public String getNodeName() {
        return nodeName;
    }

    public boolean getBooleanAttribute(String name) {
        return context.getBooleanAttribute(name);
    }

    public int getIntAttribute(String name, int defaultValue) {
        return context.getIntAttribute(name, defaultValue);
    }

    public boolean isAttributePresent(String name) {
        return context.isAttributePresent(name);
    }
}
