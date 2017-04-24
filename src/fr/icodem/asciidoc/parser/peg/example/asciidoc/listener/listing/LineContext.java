package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing;

import java.util.ArrayList;
import java.util.List;

public class LineContext {
    int lineNumber;
    String language;
    public char[] data;
    public int offset;
    public int length;
    List<ListingCallout> callouts;


    private LineContext() {
    }

    public static LineContext of(int lineNumber, char[] data, int offset, int length) {
        LineContext line = new LineContext();
        line.lineNumber = lineNumber;
        line.data = data;
        line.offset = offset;
        line.length = length;

        return line;
    }

    @Deprecated
    private LineContext(int lineNumber, String language, char[] data, int length) {
        this.lineNumber = lineNumber;
        this.language = language;
        this.data = data;
        this.length = length;
        this.callouts = new ArrayList<>();
    }

    @Deprecated
    public static LineContext of(int lineNumber, String language, char[] data, int length) {
        return new LineContext(lineNumber, language, data, length);
    }
}
