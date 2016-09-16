package fr.icodem.asciidoc.parser.peg.listeners;

import fr.icodem.asciidoc.parser.peg.NodeContext;

public class DefaultParseTreeListener implements ParseTreeListener {
    @Override
    public void characters(NodeContext context, char[] chars, int startIndex, int endIndex) {}

    @Override
    public void enterNode(NodeContext context) {}

    @Override
    public void exitNode(NodeContext context) {}
}
