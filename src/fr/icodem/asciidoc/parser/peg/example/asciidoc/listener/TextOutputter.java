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
    private Marker marker; // current marker in use

    private static int markersCount;
    private class Marker {
        int id;
        int position;
        Indenter indenter;

        public Marker(int position, int indentLevel) {
            this.id = ++markersCount;
            this.position = position;
            this.indenter = new Indenter(indentLevel);
        }
    }

    private Map<String, Marker> markers;

    private class Indenter {
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
    //private Indenter markerIndenter; // when buffer is used
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
            positionInBuffer += str.length();
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
        markers.put(key, new Marker(markPos, indenter.level));
        //markers.put(key, new Marker(buffer.length(), indenter.level));
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
    }

    public void moveTo(String key) {
        //updateMarkers();

        marker = markers.get(key);
        if (marker != null) {
            positionInBuffer = marker.position;
            indenter = marker.indenter;
            System.out.println("MOVETO="+positionInBuffer);
        } else {
            throw new IllegalStateException("No marker found for key : " + key);
        }
    }

    public void moveLast() {

        updateMarkers();

        positionInBuffer = -1;
        indenter = rootIndenter;
        marker = null;
    }

    private void updateMarkers() {
        if (marker == null) return;

        //int pos = marker.position; // position when moveTo() was called
        int delta = positionInBuffer - marker.position; // number of chars added after moveTo()

        markers.values()
                .stream()
                .filter(m -> m.position > marker.position)
                //.filter(m -> m.id >= marker.id && m.position >= pos)
                .forEach(m -> m.position += delta); // à reprendre exemple auteurs avant title
    }

}
