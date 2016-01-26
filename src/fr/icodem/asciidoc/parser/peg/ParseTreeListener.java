package fr.icodem.asciidoc.parser.peg;

public interface ParseTreeListener {
    void characters(char[] characters, int startIndex, int endIndex);
    void enterNode(String nodeName);
    void exitNode(String nodeName);
}
