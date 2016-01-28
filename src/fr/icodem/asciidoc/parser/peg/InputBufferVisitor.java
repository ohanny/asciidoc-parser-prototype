package fr.icodem.asciidoc.parser.peg;

/**
 * A visitor is notified of internal state of the input buffer
 * used for parsing. Notifications are done at various stage of
 * the parsing. It is mainly used for test purpose.
 */
public interface InputBufferVisitor {
    void visitNextChar(int position, char c);

    void visitExtract(char[] chars);

    void visitReset(int position, int marker);

    void visitRelease(int marker, int[] markers);
}
