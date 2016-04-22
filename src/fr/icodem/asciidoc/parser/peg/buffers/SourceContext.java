package fr.icodem.asciidoc.parser.peg.buffers;

import java.util.LinkedList;
import java.util.List;

@Deprecated
public class SourceContext<T> {
    private T source;
    private List<Segment> segments = new LinkedList<>();

    static class Segment {
        int start;
        int length;
        boolean suspended;

        static Segment newSegment(int start) {
            Segment segment = new Segment();
            segment.start = start;
            return segment;
        }
    }

    public SourceContext(T source) {
        this.source = source;
        segments = new LinkedList<>();
    }

    public T getSource() {
        return source;
    }

    void addSegment(int start) {
        segments.add(Segment.newSegment(start));
    }

//    /**
//     * New segment of data is appended in active space.
//     * Length of this segment is not yet known.
//     * @param pos
//     */
//    public void append(int pos) {
//        segments.add(Segment.newSegment(pos));
//    }
//
//    /**
//     * Creates a segment on pending space. Only one segment should suspended
//     * at a time.
//     * @param pos
//     * @param length
//     */
//    public void suspend(int pos, int length) {
//        if (length > 0) {
//
//        }
//    }
//
//    /**
//     * Restore segment from pending space to active space
//     * @param pos
//     */
//    public void restore(int pos) {
//
//    }

}
