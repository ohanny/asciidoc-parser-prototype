package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.listing;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Callout;

import java.util.List;

public class ListingLine {
    private int number;
    private String text;
    private List<Callout> callouts;
    private List<ListingLineChunk> chunks;

    private ListingLine(int number, String text, List<Callout> callouts, List<ListingLineChunk> chunks) {
        this.number = number;
        this.text = text;
        this.callouts = callouts;
        this.chunks = chunks;
    }

    public static ListingLine of(int number, String text, List<Callout> callouts, List<ListingLineChunk> chunks) {
        return new ListingLine(number, text, callouts, chunks);
    }

    public int getNumber() {
        return number;
    }

    public String getText() {
        return text;
    }

    public List<Callout> getCallouts() {
        return callouts;
    }

    public List<ListingLineChunk> getLineChunks() {
        return chunks;
    }

}
