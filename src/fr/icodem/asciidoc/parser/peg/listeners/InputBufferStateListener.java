package fr.icodem.asciidoc.parser.peg.listeners;

public interface InputBufferStateListener {
    /**
     *
     * @param position absolute position in input
     * @param c
     */
    void visitNextChar(int position, char c);

    void visitExtract(char[] chars, int start, int end);

    void visitReset(int marker, int position, int offset);

    /**
     *
     * @param event
     * @param data
     * @param numberOfCharacters
     * @param position relative position in data
     * @param offset
     */
    void visitData(String event, char[] data, int numberOfCharacters, int position, int offset);
}
