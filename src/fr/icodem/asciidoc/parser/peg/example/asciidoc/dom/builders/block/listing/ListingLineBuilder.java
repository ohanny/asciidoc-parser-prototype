package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block.listing;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Callout;

import java.util.LinkedList;
import java.util.List;

public class ListingLineBuilder {
    public int lineNumber;
    public char[] data;
    public int offset;
    public int length;
    List<Callout> callouts;

    List<ListingLineChunkBuilder> chunks;

    private ListingLineBuilder() {}

    public static ListingLineBuilder of(int lineNumber, char[] data, int offset, int length) {
        ListingLineBuilder line = new ListingLineBuilder();
        line.lineNumber = lineNumber;
        line.data = data;
        line.offset = offset;
        line.length = length;

        return line;
    }

    public List<ListingLineChunkBuilder> getChunks() {
        return chunks;
    }

    public void setChunks(List<ListingLineChunkBuilder> chunks) {
        this.chunks = chunks;
    }

    public String getText() {
        return new String(data, offset, length);
    }

    public void addCallout(int index, Callout callout) {
        if (callouts == null) callouts = new LinkedList<>();
        callouts.add(index, callout);
    }

}
