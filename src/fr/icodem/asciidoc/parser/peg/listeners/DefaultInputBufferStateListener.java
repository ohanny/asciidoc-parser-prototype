package fr.icodem.asciidoc.parser.peg.listeners;

public class DefaultInputBufferStateListener implements InputBufferStateListener {
    @Override
    public void visitNextChar(int position, char c) {}

    @Override
    public void visitExtract(char[] chars, int start, int end) {}

    @Override
    public void visitReset(int position, int marker) {}
}
