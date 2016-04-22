package fr.icodem.asciidoc.parser.peg.buffers;

import java.util.Deque;
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

    void incrementLastSize(int size) {
        Segment<T> lastSource = sources.peekLast();
        lastSource.length += size;
        this.length += size;
    }

    void increment(int size) {
        this.length += size;
    }

    void decrement(int size) {
        this.length -= size;
    }


//    void append(T source, int start, int length) {
//        sources.offer(Segment.newSegment(source, start, length));
//    }

}
