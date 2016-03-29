package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.peg.listeners.InputBufferStateListener;

import java.io.Reader;
import java.util.Arrays;

import static fr.icodem.asciidoc.parser.peg.Chars.EOI;

/**
 * A buffer holding the input text to be parsed.
 */
public class ReaderInputBuffer implements InputBuffer {

    /**
     * A {@link InputBufferStateListener listener} is notified of the internal
     * state of the input buffer at various stage of the parsing.
     * Mainly used for test purpose.
     */
    private InputBufferStateListener listener;

    private Reader reader;

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

    private int[] newLinePositions;
    private int lastNewLinePositionIndex;

    /**
     * Constructs an input buffer given an input reader.
     * @param reader the input text to be parsed
     * @param listener the listener notified of internal state of the buffer
     */
    ReaderInputBuffer(Reader reader, InputBufferStateListener listener) {
        this.reader = reader;
        data = new char[1024];
        position = -1;
        newLinePositions = new int[128];
        Arrays.fill(newLinePositions, -1);
        lastNewLinePositionIndex = -1;
        Arrays.fill(data, Chars.EOI);
//        System.arraycopy(text.toCharArray(), 0, data, 0, text.length());

//        numberOfCharacters = text.length();

        if (listener == null) {
            throw new IllegalArgumentException("Input buffer state listener must not be null");
        }
        this.listener = listener;
    }

    @Override
    public char getNextChar() {
        if (position < numberOfCharacters) {
            char c = data[++position];
            if (c == '\n') {
                addNewLinePosition(position);
            }
            listener.visitNextChar(position, c);
            return c;
        } else if (position == numberOfCharacters) {
            position++;
        }

        listener.visitNextChar(position, EOI);
        return EOI;
    }

    private void addNewLinePosition(int position) {
        newLinePositions[++lastNewLinePositionIndex] = position;
    }

    private void syncNewLinePositions() {
        for (int i = lastNewLinePositionIndex; i > -1 ; i--) {
            if (position >= newLinePositions[lastNewLinePositionIndex]) {
                break;
            }
            else {
                newLinePositions[lastNewLinePositionIndex--] = -1;
            }
        }
    }

    @Override
    public int getPositionInLine() {
        if (lastNewLinePositionIndex > -1) {
            if (newLinePositions[lastNewLinePositionIndex] == position) {
                if (lastNewLinePositionIndex > 0) {
                    return position - newLinePositions[lastNewLinePositionIndex - 1] - 1;
                }
                return position;
            }

            return position - newLinePositions[lastNewLinePositionIndex] - 1;
        }
        return position;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public char[] extract(int start, int end) {
        // TODO prendre en compte offset en plus
        if (end < start) return null;

        char[] chars = Arrays.copyOfRange(data, start, end + 1);

        listener.visitExtract(chars, start, end);

        return chars;
    }

    @Override
    public void reset(int marker) {// TODO add assert
        int oldPos = position;
        position = marker;
        syncNewLinePositions();

        listener.visitReset(oldPos, marker);
    }

    @Override
    public void consume() {
        System.arraycopy(data, position, data, 0, numberOfCharacters - position);
        numberOfCharacters -= position + 1;
        position = 0;
    }
}
