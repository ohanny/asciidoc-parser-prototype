package fr.icodem.asciidoc.parser.peg;

import java.util.Arrays;

import static fr.icodem.asciidoc.parser.peg.Chars.*;

/**
 * A buffer holding the input text to be parsed.
 */
public class InputBuffer {

    /**
     * A {@link InputBufferVisitor visitor} is notified of the internal
     * state of the input buffer at various stage of the parsing.
     * Mainly used for test purpose.
     */
    private InputBufferVisitor visitor;

    /**
     * A moving window buffer of the data being scanned. We keep adding
     * to the buffer while there's data in the input source.
     * When {@link #consume consume()} occurs, characters starting from
     * next position are shifted to index 0.
     */
    private char[] data;

    /**
     * Index into {@link #data data} of the next character.
     */
    private int position;

    /**
     * The number of character currently in {@link #data data}.
     */
    private int numberOfCharacters;

    /**
     * Constructs an input buffer given an input text.
     * @param text the input text to be parsed
     */
    public InputBuffer(String text) {
        this(text, null);
    }

    /**
     * Constructs an input buffer given an input text.
     * @param text the input text to be parsed
     * @param visitor the visitor notified of internal state of the buffer
     */
    public InputBuffer(String text, InputBufferVisitor visitor) {
        data = new char[1024];
        System.arraycopy(text.toCharArray(), 0, data, 0, text.length());

        numberOfCharacters = text.length();

        this.visitor = visitor;
    }

    /**
     * Gets next character from the buffer.
     * @return the next character in the buffer,
     *         or EOI if end of input has been reached
     */
    public char getNextChar() {
        if (position < numberOfCharacters) {
            if (visitor != null) {
                visitor.visitNextChar(position, data[position]);
            }
            //System.out.println("POS = " + position + " => " + data[position]);
            return data[position++];
        }

        return EOI;
    }

    public int getPosition() {
        return position;
    }

    public int getLastReadPosition() {
        return position - 1;
    }

    public char[] extract(int start, int end) {
        // TODO prendre en compte offset en plus
        if (end < start) return null;

        if (visitor != null) {
            visitor.visitExtract(Arrays.copyOfRange(data, start, end + 1));
        }

        return Arrays.copyOfRange(data, start, end + 1);
    }

    /**
     * Position of the next character comes back to the index
     * corresponding to the marker.
     * @param marker the marker used to reset the position
     */
    public void reset(int marker) {
        position = marker;
    }

    /**
     * Characters from index 0 up to current position are removed from the buffer.
     * Characters starting at next position are shifted at index 0.
     */
    public void consume() {
        System.arraycopy(data, position, data, 0, numberOfCharacters - position);
        numberOfCharacters -= position;
        position = 0;
//        System.out.println("POS=" + position + ", nb="+numberOfCharacters);
//        System.out.println(new String(Arrays.copyOf(data, numberOfCharacters)));
    }
}
