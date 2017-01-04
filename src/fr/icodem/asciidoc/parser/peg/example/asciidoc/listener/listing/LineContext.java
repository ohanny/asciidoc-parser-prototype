package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing;

import java.util.ArrayList;
import java.util.List;

public class LineContext {
    int lineNumber;
    String language;
    char[] data;
    int length;
    List<ListingCallout> callouts;

    private LineContext(int lineNumber, String language, char[] data, int length) {
        this.lineNumber = lineNumber;
        this.language = language;
        this.data = data;
        this.length = length;
        this.callouts = new ArrayList<>();
    }

    public static LineContext of(int lineNumber, String language, char[] data, int length) {
        return new LineContext(lineNumber, language, data, length);
    }
}
