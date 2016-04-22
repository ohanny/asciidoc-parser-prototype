package fr.icodem.asciidoc.parser.peg.buffers;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

public class SpaceInfo<T> {
    int start;
    int length;

    Deque<Segment<T>> sources = new LinkedList<>();

    static class Segment<T> {
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

    static <T> SpaceInfo<T> newSpaceInfo(int start, int length) {
        SpaceInfo<T> space = new SpaceInfo<>();
        space.start = start;
        space.length = length;
        return space;
    }


    // append source, don't know length yet
    void append(T source) { // appendRight ?
        int start = 0;
        Segment<T> lastSource = sources.peekLast();
        if (lastSource != null) {
            start = lastSource.getEnd() + 1;
        }

        sources.offer(Segment.newSegment(source, start, 0));
    }

//    private void incrementLastSize(int size) {
//        Segment<T> lastSource = sources.peekLast();
//        lastSource.length += size;
//        this.length += size;
//    }

//    private void increment(int size) {
//        this.length += size;
//    }
//
//    private void decrement(int size) {
//        this.length -= size;
//    }
//
//    private void move(int offset) {
//        start += offset;
//    }

    void expandStart(int size) {
        start -= size;
        length += size;
    }

    void expandEnd(int size) {
        length += size;

        Segment<T> lastSource = sources.peekLast();
        if (lastSource != null) {
            lastSource.length += size;
        }
    }

    void shrinkStart(int size) {
        start += size;
        length -= size;
    }

    void shrinkEndToLeft(int size) {// TODO rename
        length -= size;

        int removed = 0;
        for (Iterator<Segment<T>> it = sources.iterator(); it.hasNext();) {
            Segment<T> segment = it.next();

            int remaining = size - removed;
            int sizeToRemove = Math.min(remaining, segment.length);
            segment.length -= sizeToRemove;
            segment.start -= removed;
            removed += sizeToRemove;

            if (segment.start < 0) throw new RuntimeException("XXX="+segment.start);// TODO remove
        }
    }

    void shrinkEnd(int size) {
        length -= size;
    }


//    void append(T source, int start, int length) {
//        sources.offer(Segment.newSegment(source, start, length));
//    }

}
