package fr.icodem.asciidoc.parser.peg.listeners;

public class DefaultInputBufferStateListener implements InputBufferStateListener {
    @Override
    public void visitNextChar(int position, char c) {}

    @Override
    public void visitExtract(char[] chars, int start, int end) {}

    @Override
    public void visitReset(int marker, int position, int offset) {}

    @Override
    public void visitData(String event, char[] data, int numberOfCharacters, int position, int offset) {}
}
