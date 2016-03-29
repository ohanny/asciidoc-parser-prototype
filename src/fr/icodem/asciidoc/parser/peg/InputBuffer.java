package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.peg.listeners.InputBufferStateListener;

import java.io.Reader;

public interface InputBuffer {

    static InputBuffer stringInputBuffer(String text, InputBufferStateListener listener) {
        return new StringInputBuffer(text, listener);
    }

    static InputBuffer readerInputBuffer(Reader reader, InputBufferStateListener listener) {
        return new ReaderInputBuffer(reader, listener);
    }

    /**
     * Gets next character from the buffer.
     * @return the next character in the buffer,
     *         or EOI if end of input has been reached
     */
    char getNextChar();

    int getPositionInLine();

    int getPosition();

    char[] extract(int start, int end);

    /**
     * Position of the next character comes back to the index
     * corresponding to the marker.
     * @param marker the marker used to reset the position
     */
    void reset(int marker);

    /**
     * Characters from index 0 up to current position are removed from the buffer.
     * Characters starting at next position are shifted at index 0.
     */
    void consume();
}
