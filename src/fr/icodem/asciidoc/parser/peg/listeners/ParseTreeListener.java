package fr.icodem.asciidoc.parser.peg.listeners;

import fr.icodem.asciidoc.parser.peg.NodeContext;

public interface ParseTreeListener {
    void characters(NodeContext context, char[] chars, int startIndex, int endIndex);
    void enterNode(NodeContext context);
    void exitNode(NodeContext context);
}
