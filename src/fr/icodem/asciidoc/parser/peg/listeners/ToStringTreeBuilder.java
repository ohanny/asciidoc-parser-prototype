package fr.icodem.asciidoc.parser.peg.listeners;

import fr.icodem.asciidoc.parser.peg.Chars;
import fr.icodem.asciidoc.parser.peg.NodeContext;

public class ToStringTreeBuilder implements ParseTreeListener {

    private StringBuilder sb = new StringBuilder();

    @Override
    public void characters(NodeContext context, char[] characters, int startIndex, int endIndex) {
        for (char c: characters) {
            sb.append(' ');
            Chars.append(c, sb);
        }
    }

    @Override
    public void enterNode(NodeContext context) {
        if (sb.length() > 0) sb.append(' ');
        sb.append("(").append(context.getNodeName());
    }

    @Override
    public void exitNode(NodeContext context) {
        sb.append(")");
    }

    public String getStringTree() {
        return sb.toString();
    }
}
