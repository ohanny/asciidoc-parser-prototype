package fr.icodem.asciidoc.parser.peg.buffers;

public interface InputBuffer<T> {

//    static InputBuffer stringInputBuffer(String text) {
//        return new StringInputBuffer(text);
//    }

//    static ReaderInputBuffer readerInputBuffer(Reader reader) {
//        return new ReaderInputBuffer(reader);
//    }

//    default InputBuffer useListener(InputBufferStateListener listener) {
//        return this;
//    }

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
     * Perform an extract without marking the extraction
     * @param start
     * @param end
     * @return
     */
    char[] extractSilently(int start, int end);

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
    default void consume() {}

    default InputBuffer<T> include(T source) {
        return this;
    }
}
