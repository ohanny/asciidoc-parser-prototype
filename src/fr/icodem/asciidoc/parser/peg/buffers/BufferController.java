package fr.icodem.asciidoc.parser.peg.buffers;

import fr.icodem.asciidoc.parser.peg.listeners.InputBufferStateListener;

import java.util.Arrays;

import static fr.icodem.asciidoc.parser.peg.Chars.EOI;

public class BufferController<T> implements InputBuffer<T> {
    private BufferLayout<T> layout;
    private BufferLoader<T> loader;

    /**
     * A {@link InputBufferStateListener listener} is notified of the internal
     * state of the input buffer at various stage of the parsing.
     * Mainly used for test purpose.
     */
    private InputBufferStateListener listener;


    /**
     * A moving window buffer of the buffer being scanned. We keep adding
     * to the buffer while there's buffer in the input source.
     * When {@link #consume(int) consume} occurs, characters starting after
     * limit position are shifted to index 0.
     */
    private char[] buffer;

    /**
     * Position of current char in active space
     */
    private int position;

    private int offset;

    private int lastConsumeLimit = -1;

    private NewLinesTracker newLinesTracker;

    public BufferController(BufferLoader<T> loader) {
        this.loader = loader;
    }

    public BufferController<T> initBuffer(int size) {
        return initBuffer(new char[size]);
    }

    public BufferController<T> initBuffer(char[] data) {
        this.buffer = data;
        init();

        return this;
    }

    private void init() {
        position = -1;
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
        layout = new BufferLayout(buffer.length);
    }

    @Override // doit être déclenché par node uniquement ?
    public BufferController<T> include(T source) {

        // split actual source buffer

        // append new source buffer in active space
        layout.includeSource(source, position, buffer);

        return this;
    }

    private boolean shouldLoadFromSource() {
        return layout.shouldLoadFromSource(position);
    }

    private void loadFromSource() {
        try {
            ensureCapacity();

            int offset = layout.getFreeSpaceOffset();
            int length = layout.getFreeSpaceSize();;

            // fill buffer from input
            int numRead = loader.load(layout.getCurrentSource(), buffer, offset, length);

            // new buffer added to buffer
            if (numRead != -1) {
                layout.newDataAdded(numRead);
            } else {
                endOfInput();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // remaining buffer to read - ensure capacity
    private void ensureCapacity() {
        if (layout.getFreeSpaceSize() == 0) {
            layout.increaseFreeSpace(buffer.length);
            buffer = Arrays.copyOf(buffer, buffer.length * 2);
            listener.visitData("increase", buffer, layout.getUsedSize(), position, offset);
        }
    }

    private void endOfInput() {
        buffer[layout.getFreeSpaceOffset()] = EOI;

        layout.newDataAdded(1);
        layout.endOfInput();
        layout.restoreLastSuspendedSegment(position, buffer);
    }

    @Override
    public char getNextChar() {
        // load chars from source if needed
        if (shouldLoadFromSource()) {
            loadFromSource();
        }

        if (position < layout.getActiveLength() - 1) {
            position++;
        }

        char c = buffer[position];
        if (c == '\n') {
            newLinesTracker.addNewLine(position);
        }
        listener.visitNextChar(position + offset, c);
        return c;
    }

    @Override
    public int getPosition() {
        return position + offset;
    }

    @Override
    public char[] extract(int start, int end) {
        if (end < start) return null;

        char[] chars = Arrays.copyOfRange(buffer, start - offset, end - offset + 1);

        listener.visitExtract(chars, start, end);

        if (end > lastExtracted) lastExtracted = end;
        return chars;
    }

    private int lastExtracted;

    @Override
    public void consume(int limit) {
        if (limit <= lastConsumeLimit) return;

        lastConsumeLimit = limit;
        int srcPos = limit - offset + 1;

srcPos = lastExtracted - offset + 1;
        int length = layout.getActiveLength() - srcPos;

        layout.consume(srcPos);


if (length > 0)
        System.arraycopy(buffer, srcPos, buffer, 0, length);

        offset += srcPos;
        position = position - srcPos;

        newLinesTracker.clear();

lastExtracted = -1;

        listener.visitData("consume", buffer, layout.getUsedSize(), position, offset);
    }

    @Override
    public void reset(int marker) {
        int oldPos = position;
        position = marker - offset;
        newLinesTracker.sync(position);

        listener.visitReset(oldPos, marker);
    }

    @Override
    public int getPositionInLine() {
        //return newLinesTracker.getPositionInLine(position, offset);
        return newLinesTracker.getPositionInLine(position);
    }

}
