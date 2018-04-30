package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing;

import java.util.LinkedList;
import java.util.List;

public class LineContext {
    int lineNumber;
    public char[] data;
    public int offset;
    public int length;
    List<ListingCallout> callouts;

    List<LineChunkContext> chunks;

    private LineContext() {}

    public static LineContext of(int lineNumber, char[] data, int offset, int length) {
        LineContext line = new LineContext();
        line.lineNumber = lineNumber;
        line.data = data;
        line.offset = offset;
        line.length = length;

        return line;
    }

    public List<LineChunkContext> getChunks() {
        return chunks;
    }

    public void setChunks(List<LineChunkContext> chunks) {
        this.chunks = chunks;
    }

    public String getText() {
        return new String(data, offset, length);
    }

    public void addCallout(int index, ListingCallout callout) {
        if (callouts == null) callouts = new LinkedList<>();
        callouts.add(index, callout);
    }
}
