package fr.icodem.asciidoc.parser.peg.listeners;

import fr.icodem.asciidoc.parser.peg.Chars;

public class ToStringTreeBuilder implements ParseTreeListener {

    private StringBuilder sb = new StringBuilder();

    @Override
    public void characters(char[] characters, int startIndex, int endIndex) {
        for (char c: characters) {
            sb.append(' ');
            Chars.append(c, sb);
        }
    }

    @Override
    public void enterNode(String nodeName) {
        if (sb.length() > 0) sb.append(' ');
        sb.append("(").append(nodeName);
    }

    @Override
    public void exitNode(String nodeName) {
        sb.append(")");
    }

    public String getStringTree() {
        return sb.toString();
    }
}
