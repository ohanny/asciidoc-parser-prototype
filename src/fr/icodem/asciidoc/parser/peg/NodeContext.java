package fr.icodem.asciidoc.parser.peg;

import java.io.Reader;

public class NodeContext {

    private String nodeName;
    private MatcherContext matcherContext;

    public NodeContext(String nodeName, MatcherContext matcherContext) {
        this.nodeName = nodeName;
        this.matcherContext = matcherContext;
    }

    public void chain(Reader reader) {
        matcherContext.chain(reader);
    }

    public String getNodeName() {
        return nodeName;
    }
}
