package fr.icodem.asciidoc.parser.peg.listeners;

public interface ParseTreeListener {
    void characters(char[] chars, int startIndex, int endIndex);
    void enterNode(String nodeName);
    void exitNode(String nodeName);
}
