package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class TextOutputter {
    private final static String NL = "\r\n";
    private final static String INDENT = "  ";

    private Writer writer;

    private StringBuilder buffer;
    private Consumer<String> bufferAppender;
    private Consumer<String> writerAppender;
    private Consumer<String> appender;

    private int positionInBuffer;
    private Marker marker; // current marker
    private Marker head; // first marker
    private Marker tail; // last marker

    private static class Marker {
        Marker next;
        int position;
        Indenter indenter;

        public static Marker of(int position, int indentLevel) {
            Marker marker = new Marker();
            marker.position = position;
            marker.indenter = new Indenter(indentLevel);

            return marker;
        }
    }

    private Map<String, Marker> markers;

    private static class Indenter {
        int level;

        public Indenter() {}

        public Indenter(int level) {
            this.level = level;
        }

        void increment() {
            level++;
        }

        void decrement() {
            level--;
        }
    }
    private Indenter rootIndenter;
    private Indenter indenter;

    public TextOutputter(Writer writer) {
        this.writer = writer;

        this.writer = writer;

        buffer = new StringBuilder();

        writerAppender = this::appendToWriter;
        bufferAppender = this::appendToBuffer;
        appender = writerAppender;
        markers = new HashMap<>();
        positionInBuffer = -1;

        rootIndenter = new Indenter();
        indenter = rootIndenter;
    }

    public void closeWriter() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendToWriter(String str) {
        try {
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendToBuffer(String str) {
        if (positionInBuffer == -1) {
            buffer.append(str);
        } else {
            buffer.insert(positionInBuffer, str);
            final int length = str.length();
            updateMarkers(length);
            positionInBuffer += length;
        }
    }

    public void append(String str) {
        appender.accept(str);
    }

    public void nl() {
        append(NL);
    }

    public void indent() {
        indent(indenter.level);
    }

    private void indent(int times) {
        for (int i = 0; i < times; i++) {
            append(INDENT);
        }
    }

    public void incrementIndentLevel() {// TODO rename method (shorter name) ?
        indenter.increment();
    }

    public void decrementIndentLevel() {// TODO rename method (shorter name) ?
        indenter.decrement();
    }

    public void mark(String key) {
        int markPos = positionInBuffer != -1?positionInBuffer:buffer.length();
        final Marker marker = Marker.of(markPos, indenter.level);
        markers.put(key, marker);

        if (head == null) {
            head = marker;
            tail = marker;
        } else if (this.marker == null) { // append marker at the end
            tail.next = marker;
            tail = marker;
        } else { // insert marker after current one

            // find where to insert
            Marker m = this.marker;
            while (m.next != null && m.next.position <= markPos) {
                m = m.next;
            }

            marker.next = m.next;
            m.next = marker;
        }
    }

    public void bufferOn() {
        appender = bufferAppender;
    }

    public void bufferOff() {
        if (buffer.length() > 0) {
            appendToWriter(buffer.toString());
            buffer.setLength(0);
        }
        appender = writerAppender;
        positionInBuffer = -1;
        indenter = rootIndenter;
        markers.clear();
        head = null;
        tail = null;
    }

    public void moveTo(String key) {
        marker = markers.get(key);
        if (marker != null) {
            positionInBuffer = marker.position;
            indenter = marker.indenter;
        } else {
            throw new IllegalStateException("No marker found for key : " + key);
        }
    }

    public void moveEnd() {
        positionInBuffer = -1;
        indenter = rootIndenter;
        marker = null;
    }

    private void updateMarkers(int delta) {
        if (marker == null) return;

        Marker m = marker.next;
        while (m != null) {
            if (m.position > positionInBuffer) {
                m.position += delta;
            }
            m = m.next;
        }
    }

}
