package fr.icodem.asciidoc.parser.peg.buffers;

import fr.icodem.asciidoc.parser.peg.listeners.InputBufferStateListener;

import java.io.Reader;

public interface InputBuffer {

    static InputBuffer stringInputBuffer(String text) {
        return new StringInputBuffer(text);
    }

    static ReaderInputBuffer readerInputBuffer(Reader reader) {
        return new ReaderInputBuffer(reader);
    }

    default InputBuffer useListener(InputBufferStateListener listener) {
        return this;
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
     * Characters from index 0 up to limit are removed from the buffer.
     * Characters starting at next position are shifted at index 0.
     */
    default void consume(int limit) {}

    default void chain(Reader reader) {};
}
