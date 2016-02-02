package fr.icodem.asciidoc.parser.peg.listeners;

public interface InputBufferStateListener {
    void visitNextChar(int position, char c);

    void visitExtract(char[] chars, int start, int end);

    void visitReset(int position, int marker);
}
