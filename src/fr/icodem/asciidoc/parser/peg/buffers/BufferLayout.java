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
        boolean root;
        boolean endOfInputReached;

        static <T> Segment<T> newRootSegment(T source, int start, int length) {
            Segment segment = newSegment(source, start, length);
            segment.root = true;
            return segment;
        }

        static <T> Segment<T> newSegment(T source, int start, int length) {
            Segment segment = new Segment();
            segment.source = source;
            segment.start = start;
            segment.length = length;
            return segment;
        }

        int getEnd() {
            return start + length - 1;
        }

        Segment<T> childSegment(int start, int length) {
            Segment segment = newSegment(source, start, length);
            segment.source = this.source;
            return segment;
        }

    }


    public BufferLayout(int size) {
        this.size = size;
        this.activeLength = 0;
        this.freeLength = size;
        this.suspendedLength = 0;
    }

    public int getFreeSpaceSize() {
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
        }

    }

    // segment is divided into two segments :
    // the first one remains in active space, but its length is adjusted
    // to current position; the second one is added to suspended space
    private void suspendSegmentAtPosition(Segment<T> segment, int position, char[] buffer) {
        if (segment != null) {
            // length of two segments
            int len1 = position - segment.start + 1;
            int len2 = segment.length - len1;

            // don't create suspended segment if no data to suspend
            if (len2 == 0) return;

            // start of second segment (in suspended space)
            int start2 = size - suspendedLength - len2;

            segment.length = len1;
            suspendedSegments.add(segment.childSegment(start2, len2));

            activeLength -= len2;
            suspendedLength += len2;

            if (len2 > 0) {
                System.arraycopy(buffer, position + 1, buffer, start2, len2);
            }
        }
    }

    // append source, don't know length yet
    public void includeSource(T source, int position, char[] buffer) {
        Segment<T> curSegment = activeSegments.peekLast();

        // split actual segment if exists
        suspendSegmentAtPosition(curSegment, position, buffer);

        // add new segment to active space
        int start = (curSegment != null)?curSegment.getEnd() + 1:0;
        activeSegments.offer(Segment.newRootSegment(source, start, 0));
    }

    public T getCurrentSource() {
        return activeSegments.peekLast().source;
    }

    public void endOfInput() {
        activeSegments.peekLast().endOfInputReached = true;
    }

    public void restoreLastSuspendedSegment(int position, char[] buffer) {
        Segment<T> lastSuspendedSegment = suspendedSegments.pollLast();
        if (lastSuspendedSegment != null) {
            System.arraycopy(buffer, lastSuspendedSegment.start, buffer, activeLength, lastSuspendedSegment.length);

            activeSegments.add(lastSuspendedSegment);
            activeLength += lastSuspendedSegment.length;
            suspendedLength -= lastSuspendedSegment.length;
        }
    }

    public boolean shouldLoadFromSource(int position) {
        return position == activeLength - 1 && !activeSegments.peekLast().endOfInputReached;
    }


}
