package fr.icodem.asciidoc.parser.peg;

import java.util.Arrays;

import static fr.icodem.asciidoc.parser.peg.Chars.*;

/**
 * A buffer holding the input text to be parsed.
 */
public class InputBuffer {

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

    private int[] markers;
    private int lastMarker;

    /**
     * Constructs an input buffer given an input text.
     * @param text the input text to be parsed
     */
    public InputBuffer(String text) {
        data = new char[1024];
        System.arraycopy(text.toCharArray(), 0, data, 0, text.length());

        numberOfCharacters = text.length();

        markers = new int[10];
        Arrays.fill(markers, -1);
        lastMarker = -1;
    }

    /**
     * Gets next character from the buffer.
     * @return the next character in the buffer,
     *         or EOI if end of input has been reached
     */
    public char getNextChar() {
        if (position < numberOfCharacters) {
            //System.out.println("POS = " + position + " => " + data[position]);
            return data[position++];
        }

        return EOI;
    }

    public int mark() {
        markers[++lastMarker] = position;

        return lastMarker;
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

        return Arrays.copyOfRange(data, start, end + 1);
    }

    /**
     * Position of the next character comes back to the index
     * corresponding to the marker.
     * @param marker the marker used to reset the position
     */
    public void reset(int marker) {
        if (marker < 0 || marker > lastMarker)
            throw new IllegalArgumentException("Wrong marker : " + marker + " (last marker was " + lastMarker + ")");

        position = markers[marker];
        //System.out.println("RESET => " + position);
        lastMarker = marker - 1;// ???
//        for (int i = marker; i < markers.length; i++) {
//            if (markers[i] == -1) break;
//            //markers[i] = -1;
//        }
    }

    public void release(int marker) {// ajouter des tests
        markers[marker] = NULL;
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
