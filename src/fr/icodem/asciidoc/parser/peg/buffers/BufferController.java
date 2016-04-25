package fr.icodem.asciidoc.parser.peg.buffers;

import fr.icodem.asciidoc.parser.peg.listeners.InputBufferStateListener;

import java.util.Arrays;

import static fr.icodem.asciidoc.parser.peg.Chars.EOI;

public class BufferController<T> {
    private BufferLayout<T> layout;

    /**
     * A {@link InputBufferStateListener listener} is notified of the internal
     * state of the input buffer at various stage of the parsing.
     * Mainly used for test purpose.
     */
    private InputBufferStateListener listener;


    /**
     * A moving window buffer of the data being scanned. We keep adding
     * to the buffer while there's data in the input source.
     * When {@link #consume(int) consume} occurs, characters starting after
     * limit position are shifted to index 0.
     */
    private char[] data;

    /**
     * Position of current char in active space
     */
    private int position;

    private int offset;

    private int lastConsumeLimit = -1;

    private NewLinesTracker newLinesTracker;


    public BufferController<T> initBuffer(int size) {
        return initBuffer(new char[size]);
    }

    public BufferController<T> initBuffer(char[] data) {
        this.data = data;
        init();

        return this;
    }

    private void init() {
        position = -1;
        //initNewLinePositions();
        initNewLineTracker();
        initSpaces();
    }

    private void initNewLineTracker() {
        newLinesTracker = new NewLinesTracker();
        newLinesTracker.init();
    }

    public BufferController<T> useListener(InputBufferStateListener listener) {
        this.listener = listener;
        return this;
    }

    private void initSpaces() {
        layout = new BufferLayout(data.length);
    }

    public BufferController<T> include(T source) {

        // split actual source data

        // append new source data in active space
        layout.includeSource(source, position, data);

        return this;
    }

    public boolean shouldLoadFromSource() {
        //return position == layout.getActiveLength() - 1 && !endOfInputReached;
        return layout.shouldLoadFromSource(position);
    }

    // remaining data to read - ensure capacity
    public char[] ensureCapacity() {
        if (layout.getFreeSpaceSize() == 0) {
            layout.increaseFreeSpace(data.length);
            data = Arrays.copyOf(data, data.length * 2);
            listener.visitData("increase", data, getUsedSize(), position, offset);
        }
        return data;
    }

    public int getFreeSpaceOffset() {
        return layout.getFreeSpaceOffset();
    }

    public int getFreeSpaceSize() {
        return layout.getFreeSpaceSize();
    }

    /**
     * The number of character currently in {@link #data data}.
     */
    private int getUsedSize() {
        return layout.getUsedSize();
    }

    public void endOfInput() {
        data[layout.getFreeSpaceOffset()] = EOI;

        layout.newDataAdded(1);
        layout.endOfInput();
        layout.restoreLastSuspendedSegment(position, data);
    }

    public void newDataAdded(int size) {
        layout.newDataAdded(size);
    }


    public T getCurrentSource() {
        return layout.getCurrentSource();
    }

    public char getNextChar() {
        if (position < layout.getActiveLength() - 1) {
            position++;
        }

        char c = data[position];
        if (c == '\n') {
            newLinesTracker.addNewLine(position);
        }
        listener.visitNextChar(position + offset, c);
        return c;
    }


    public int getPosition() {
        return position + offset;
    }

    public char[] extract(int start, int end) {
        if (end < start) return null;

        char[] chars = Arrays.copyOfRange(data, start - offset, end - offset + 1);

        listener.visitExtract(chars, start, end);

        return chars;
    }

    public void consume(int limit) {
        if (limit <= lastConsumeLimit) return;

        lastConsumeLimit = limit;
        int srcPos = limit - offset + 1;
        int length = layout.getActiveLength() - srcPos;

        layout.consume(srcPos);


        System.arraycopy(data, srcPos, data, 0, length);

        offset += srcPos;
        position = position - srcPos;

        //clearNewLinePositions();
        newLinesTracker.clear();

        listener.visitData("consume", data, getUsedSize(), position, offset);
    }

    public void reset(int marker) {
        int oldPos = position;
        position = marker - offset;
        //syncNewLinePositions();
        newLinesTracker.sync(position);

        listener.visitReset(oldPos, marker);
    }

    public int getPositionInLine() {
        //return newLinesTracker.getPositionInLine(position, offset);
        return newLinesTracker.getPositionInLine(position);
    }

}
