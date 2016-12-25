package fr.icodem.asciidoc.parser.peg;

public class NodeContext {

    private String nodeName;
    private MatcherContext context;

    public NodeContext(String nodeName, MatcherContext context) {
        this.nodeName = nodeName;
        this.context = context;
    }

    @Deprecated //???
    public void include(Object source) {
        context.include(source);
    }

    public String getNodeName() {
        return nodeName;
    }

    public boolean getBooleanAttribute(String name, boolean defaultValue) {
        return context.getBooleanAttribute(name, defaultValue);
    }

    public int getIntAttribute(String name, int defaultValue) {
        return context.getIntAttribute(name, defaultValue);
    }

    public String getStringAttribute(String name, String defaultValue) {
        return context.getStringAttribute(name, defaultValue);
    }

    public boolean isAttributePresent(String name) {
        return context.isAttributePresent(name);
    }

}
