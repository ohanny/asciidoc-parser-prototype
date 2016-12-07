package fr.icodem.asciidoc.parser.peg.buffers;

import fr.icodem.asciidoc.parser.peg.Chars;
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
     * When {@link #consume() consume} occurs, characters starting after
     * limit position are shifted to index 0.
     */
    private char[] buffer;

    /**
     * Position of current char in active space
     */
    private int position;

    private int offset;

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
        lastExtracted = -1;
        lastConsumed = -1;
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
            int length = layout.getFreeSpaceSize();

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
            buffer = layout.increaseFreeSpace(buffer);
//            layout.increaseFreeSpace(buffer.length);
            //buffer = Arrays.copyOf(buffer, buffer.length * 2);
            listener.visitData("increase", buffer, layout.getUsedSize(), position, offset);
        }
    }

    private void endOfInput() {
        buffer[layout.getFreeSpaceOffset()] = EOI;

        layout.newDataAdded(1);
        layout.endOfInput();
//        layout.restoreLastSuspendedSegment(position, buffer);
    }

    @Override
    public char getNextChar() {
        // load chars from source if needed
        if (shouldLoadFromSource()) {
            loadFromSource();
        }

        if (position < layout.getActiveLength() - 1) {
            position++;
        } else { // no more data in active space, check if some data remains in suspended area
            layout.restoreLastSuspendedSegment(position, buffer);
            if (position < layout.getActiveLength() - 1) {
                position++;
            }
        }

        // EOI has been consumed, it can't be read from the buffer
        if (position == -1) {
            listener.visitNextChar(position + offset, Chars.EOI);
            return Chars.EOI;
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
    public char[] extractSilently(int start, int end) {
        if (end < start) return null;

        char[] chars = Arrays.copyOfRange(buffer, start - offset, end - offset + 1);

        return chars;
    }

    @Override
    public char[] extract(int start, int end) {
        char[] chars = extractSilently(start, end);

        listener.visitExtract(chars, start, end);

        if (end > lastExtracted) lastExtracted = end;
        return chars;
    }



    private int lastExtracted;
    private int lastConsumed;

    @Override
    public void consume() {
        if (lastExtracted <= lastConsumed) return;

        lastConsumed = lastExtracted;

        int srcPos = lastExtracted - offset + 1;
        int length = layout.getActiveLength() - srcPos;

        layout.consume(srcPos);

        if (length > 0) {
            System.arraycopy(buffer, srcPos, buffer, 0, length);
        }

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

        if (position == oldPos) {
            return;
        }

        int pos = marker - offset;


        layout.reset(pos, buffer);
        newLinesTracker.sync(position);

        listener.visitReset(marker, oldPos, offset);
    }

    @Override
    public int getPositionInLine() {
        //return newLinesTracker.getPositionInLine(position, offset);
        return newLinesTracker.getPositionInLine(position);
    }

    @Override
    public int getLineNumber() {
        return newLinesTracker.getLineNumber();
    }
}
