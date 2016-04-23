package fr.icodem.asciidoc.parser.peg.buffers;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public class BufferLayout<T> {
    private int size;
    private int activeLength;
    private int freeLength;
    private int suspendedLength;

    private Deque<Segment<T>> activeSegments = new LinkedList<>();
    private Deque<Segment<T>> suspendedSegments = new LinkedList<>();

    private static class Segment<T> {
        T source;
        int start;
        int length;

        int getEnd() {
            return start + length - 1;
        }

        static <T> Segment newSegment(T source, int start, int length) {
            Segment segment = new Segment();
            segment.source = source;
            segment.start = start;
            segment.length = length;
            return segment;
        }
    }


    public BufferLayout(int size) {
        this.size = size;
        this.activeLength = 0;
        this.freeLength = size;
        this.suspendedLength = 0;
    }

    public int getFreeSpaceSize() {// get free space ???
        return freeLength;
    }

    public int getActiveLength() {
        return activeLength;
    }

    public int getUsedSize() {
        return activeLength + suspendedLength;
    }

    public int getFreeSpaceOffset() {
        return activeLength;
    }

    public void increaseFreeSpace(int size) {
        this.freeLength += size;
        this.size += size;

        for (Iterator<Segment<T>> it = suspendedSegments.iterator(); it.hasNext(); ) {
            Segment<T> segment = it.next();
            segment.start += size;
        }
    }

    public void newDataAdded(int size) {
        this.activeLength += size;
        this.freeLength -= size;

        Segment<T> lastSource = activeSegments.peekLast();
        if (lastSource != null) {
            lastSource.length += size;
        }
    }

    public void consume(int size) {
        this.activeLength -= size;
        this.freeLength += size;

        // remove from active segments
        int removed = 0;
        for (Iterator<Segment<T>> it = activeSegments.iterator(); it.hasNext();) {
            Segment<T> segment = it.next();

            int remaining = size - removed;
            int sizeToRemove = Math.min(remaining, segment.length);
            segment.length -= sizeToRemove;
            segment.start -= removed;
            removed += sizeToRemove;

            if (segment.start < 0) throw new RuntimeException("XXX="+segment.start);// TODO remove
        }

    }

    // append source, don't know length yet
    void includeSource(T source) {
        int start = 0;
        Segment<T> lastSource = activeSegments.peekLast();
        if (lastSource != null) {
            start = lastSource.getEnd() + 1;
        }

        activeSegments.offer(Segment.newSegment(source, start, 0));
    }

    public T getCurrentSource() {
        return activeSegments.peekLast().source;
    }

}
