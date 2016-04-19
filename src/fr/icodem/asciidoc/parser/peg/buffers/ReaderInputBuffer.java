package fr.icodem.asciidoc.parser.peg.buffers;

import fr.icodem.asciidoc.parser.peg.listeners.DefaultInputBufferStateListener;
import fr.icodem.asciidoc.parser.peg.listeners.InputBufferStateListener;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

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
     * When {@link #consume(int) consume} occurs, characters starting after
     * limit position are shifted to index 0.
     */
    private char[] data;

    /**
     * Index into {@link #data data} of the next character.
     */
    private int position;

    private int offset;

    private int lastConsumeLimit = -1;

    private boolean endOfInputReached;

    /**
     * The number of character currently in {@link #data data}.
     */
    private int numberOfCharacters;

    private int[] newLinePositions;
    private int lastNewLinePositionIndex;

    private Queue<Reader> nextReaders;

    /**
     * Constructs an input buffer given an input reader.
     * @param reader the input text to be parsed
     */
    ReaderInputBuffer(Reader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("Reader must not be null");
        }

        this.nextReaders = new LinkedList<>();
        this.reader = reader;
        data = new char[1024];
        position = -1;
        newLinePositions = new int[128];
        clearNewLinePositions();

        this.listener = new DefaultInputBufferStateListener();
    }

    private void clearNewLinePositions() {
        Arrays.fill(newLinePositions, -1);
        lastNewLinePositionIndex = -1;
    }

    public ReaderInputBuffer bufferSize(int size) {
        data = new char[size];
        return this;
    }

    /**
     * The listener to be notified of internal state of the buffer
     * @param listener the listener notified of internal state of the buffer
     */
    @Override
    public InputBuffer useListener(InputBufferStateListener listener) {
        if (listener != null) {
            this.listener = listener;
        }
        return this;
    }


    @Override
    public char getNextChar() {
        // load chars from reader
        if (position == numberOfCharacters - 1 && !endOfInputReached) {
            try {
                int free = data.length - numberOfCharacters;
                if (free == 0) {
                    data = Arrays.copyOf(data, data.length * 2);
                    free = data.length - numberOfCharacters;
                    listener.visitData("increase", data, numberOfCharacters, position, offset);
                }
                int numRead = reader.read(data, numberOfCharacters, free);
                if (numRead != -1) {
                    numberOfCharacters += numRead;
                } else { // end of input
                    data[numberOfCharacters++] = EOI;
                    endOfInputReached = true;
                    // TODO close reader
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (position < numberOfCharacters - 1) {
            position++;
        }

        char c = data[position];
        if (c == '\n') {
            addNewLinePosition(position);
        }
        listener.visitNextChar(position + offset, c);
        return c;
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
        return position + offset;
    }

    @Override
    public int getPosition() {
        return position + offset;
    }

    @Override
    public char[] extract(int start, int end) {
        if (end < start) return null;

        char[] chars = Arrays.copyOfRange(data, start - offset, end - offset + 1);

        listener.visitExtract(chars, start, end);

        return chars;
    }

    @Override
    public void reset(int marker) {
        int oldPos = position;
        position = marker - offset;
        syncNewLinePositions();

        listener.visitReset(oldPos, marker);
    }

    @Override
    public void consume(int limit) {
        if (limit <= lastConsumeLimit) return;

        lastConsumeLimit = limit;
        int pos = limit - offset + 1;
        int length = numberOfCharacters - pos;

        System.arraycopy(data, pos, data, 0, length);

        numberOfCharacters -= pos;
        offset += pos;
        position = position - pos;

        clearNewLinePositions();

        listener.visitData("consume", data, numberOfCharacters, position, offset);
    }

    @Override
    public void include(Reader reader) {
        nextReaders.offer(reader);
    }

}
