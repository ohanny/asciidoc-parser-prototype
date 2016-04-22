package fr.icodem.asciidoc.parser.peg.buffers;

import fr.icodem.asciidoc.parser.peg.listeners.InputBufferStateListener;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

import static fr.icodem.asciidoc.parser.peg.Chars.EOI;

public class BufferController<T> {
    private SpaceInfo activeSpace;
    private SpaceInfo freeSpace;
    private SpaceInfo suspendedSpace;

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

    private boolean endOfInputReached;
    /**
     * The number of character currently in {@link #data data}.
     */
    //private int numberOfCharacters;

    private int[] newLinePositions;
    private int lastNewLinePositionIndex;


    private Deque<SourceContext<T>> sources = new LinkedList<>();

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
        initNewLinePositions();
        initSpaces();
    }

    public BufferController<T> useListener(InputBufferStateListener listener) {
        this.listener = listener;
        return this;
    }

    private void initSpaces() {
        activeSpace = SpaceInfo.newSpaceInfo(0, 0);
        freeSpace = SpaceInfo.newSpaceInfo(0, data.length);
        suspendedSpace = SpaceInfo.newSpaceInfo(-1, 0);
    }

    public BufferController<T> include(T source) {

        // split actual source data
        SourceContext<T> actualSource = sources.pollLast();
        if (actualSource != null) {
            //actualSource.suspend(pos);
        }


        // append new source data in active space
        activeSpace.append(source);

        //SourceContext<T> sourceCtx = new SourceContext<>(source);
        //sourceCtx.addSegment(position + 1);

        // ???
        sources.offer(new SourceContext<>(source));


        //active.append(source);
        return this;
    }

    private void suspend() {

    }

    private void restore() {

    }



    public boolean shouldLoadFromSource() {
        //return position == numberOfCharacters - 1 && !endOfInputReached;
        return position == activeSpace.length - 1 && !endOfInputReached;
    }

    // remaining data to read - ensure capacity
    public char[] ensureCapacity() {
        //int free = data.length - numberOfCharacters;
        //if (free == 0) {
        if (freeSpace.length == 0) {
            //freeSpace.increment(data.length);
            freeSpace.expandEnd(data.length);
            data = Arrays.copyOf(data, data.length * 2);
            //listener.visitData("increase", data, numberOfCharacters, position, offset);
            listener.visitData("increase", data, getUsedSize(), position, offset);
        }
        return data;
    }

    public int getFreeSpaceOffset() {
        return freeSpace.start;
        //return numberOfCharacters;
    }

    public int getFreeSpaceSize() {
        return freeSpace.length;
    }

    public int getUsedSize() {
        return activeSpace.length + suspendedSpace.length;
    }

    public void endOfInput() {
        //freeSpace.decrement(1);
        //freeSpace.move(1);
        data[activeSpace.length] = EOI;

        freeSpace.shrinkStart(1);
        activeSpace.expandEnd(1);
//        activeSpace.incrementLastSize(1);
        //data[numberOfCharacters++] = EOI;
        //numberOfCharacters++;

        endOfInputReached = true;
    }

    public void newDataAddedToBuffer(int size) {
        //numberOfCharacters += size;
        //activeSpace.incrementLastSize(size);
        activeSpace.expandEnd(size);

        freeSpace.shrinkStart(size);
        //freeSpace.decrement(size);
        //freeSpace.move(size);
    }

//    void xx () {
//        if (numberOfCharacters != freeSpace.start) {
//            throw new RuntimeException("XXXXXX numberOfCharacters="+numberOfCharacters+", freeSpace.start="+freeSpace.start);
//        }
//
//    }


    public T getCurrentSource() {
        SourceContext<T> actualSource = sources.getLast();
        return actualSource.getSource();
    }

    public char getNextChar() {
        //if (position < numberOfCharacters - 1) {
        if (position < activeSpace.length - 1) {
            position++;
        }

        char c = data[position];
        if (c == '\n') {
            addNewLinePosition(position);
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
        //int length = numberOfCharacters - srcPos;
        int length = activeSpace.length - srcPos;

        //freeSpace.move(-pos);
        //freeSpace.increment(pos);
        activeSpace.shrinkEndToLeft(srcPos);
        freeSpace.expandStart(srcPos);

        System.arraycopy(data, srcPos, data, 0, length);

        //numberOfCharacters -= srcPos;
        offset += srcPos;
        position = position - srcPos;

        clearNewLinePositions();

        //listener.visitData("consume", data, numberOfCharacters, position, offset);
        listener.visitData("consume", data, getUsedSize(), position, offset);
    }

    public void reset(int marker) {
        int oldPos = position;
        position = marker - offset;
        syncNewLinePositions();

        listener.visitReset(oldPos, marker);
    }



    // *********************
    // new lines ***********
    // *********************
    private void initNewLinePositions() {
        newLinePositions = new int[128];
        clearNewLinePositions();
    }

    private void clearNewLinePositions() {
        Arrays.fill(newLinePositions, -1);
        lastNewLinePositionIndex = -1;
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


}
