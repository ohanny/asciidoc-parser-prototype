package fr.icodem.asciidoc.parser.peg;

import java.util.Arrays;

import static fr.icodem.asciidoc.parser.peg.Chars.*;

public class InputBuffer {

    /**
     * A moving window buffer of the data being scanned. While there's a marker,
     * we keep adding to buffer. Otherwise, {@link #consume consume()} resets so
     * we start filling at index 0 again.
     */
    private char[] data;

    /**
     * Index into {@link #data data} of the next character.
     */
    private int position;

    /**
     * The number of character currently in {@link #data data}
     */
    private int numberOfCharacters;

    private int[] markers;
    private int lastMarker;

    public InputBuffer(String text) {
        data = new char[1024];
        System.arraycopy(text.toCharArray(), 0, data, 0, text.length());

        numberOfCharacters = text.length();

        markers = new int[10];
        Arrays.fill(markers, -1);
        lastMarker = -1;
    }

    public char getNextChar() {
        if (position < numberOfCharacters) {
            return data[position++];
        }

        return EOI;
    }

    public int mark() {
        markers[++lastMarker] = position;

        return lastMarker;
    }

    public void reset(int marker) {
        if (marker < 0 || marker > lastMarker)
            throw new IllegalArgumentException("Wrong marker : " + marker + " (last marker was " + lastMarker + ")");

        position = markers[marker];
        lastMarker = marker - 1;// ???
//        for (int i = marker; i < markers.length; i++) {
//            if (markers[i] == -1) break;
//            //markers[i] = -1;
//        }
    }

    public void release(int marker) {// ajouter des tests
        markers[marker] = NULL;
    }

    public void consume() {
        System.arraycopy(data, position, data, 0, numberOfCharacters - position);
        numberOfCharacters -= position;
        position = 0;
//        System.out.println("POS=" + position + ", nb="+numberOfCharacters);
//        System.out.println(new String(Arrays.copyOf(data, numberOfCharacters)));
    }
}
