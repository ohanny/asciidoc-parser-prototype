package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.listing;

import java.util.List;

public class ListingLineChunk {
    private String text;
    private boolean not;
    private boolean important;
    private boolean comment;
    private boolean mark;
    private boolean strong;
    private boolean highlight;
    private int markLevel;
    private int strongLevel;

    private List<ListingLineChunk> chunks;

    public static ListingLineChunk of(String text, boolean not, boolean important, boolean comment,
                                      boolean mark, boolean strong, boolean highlight, int markLevel,
                                      int strongLevel, List<ListingLineChunk> chunks) {
        ListingLineChunk chunk = new ListingLineChunk();
        chunk.text = text;
        chunk.not = not;
        chunk.important = important;
        chunk.comment = comment;
        chunk.mark = mark;
        chunk.strong = strong;
        chunk.highlight = highlight;
        chunk.markLevel = markLevel;
        chunk.strongLevel = strongLevel;

        chunk.chunks = chunks;

        return chunk;
    }

    public String getText() {
        return text;
    }

    public boolean isNot() {
        return not;
    }

    public boolean isImportant() {
        return important;
    }

    public boolean isComment() {
        return comment;
    }

    public boolean isNotMarked() {
        return !(important || mark || strong || comment);
    }

    public boolean isMark() {
        return mark;
    }

    public boolean isStrong() {
        return strong;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public int getMarkLevel() {
        return markLevel;
    }

    public int getStrongLevel() {
        return strongLevel;
    }

    public List<ListingLineChunk> getChunks() {
        return chunks;
    }

}
