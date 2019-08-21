package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block.listing;

import java.util.List;

public class ListingLineChunkBuilder {
    char[] data;
    int offset;
    int length;

    int columnFrom;
    int columnTo;

    private boolean not;
    private boolean important;
    private boolean comment;
    private boolean mark;
    private boolean strong;
    private boolean highlight;
    private int markLevel;
    private int strongLevel;

    List<ListingLineChunkBuilder> chunks; // nested chunks

    public static ListingLineChunkBuilder of(char[] data, int offset, int length, int columnFrom, int columnTo) {
        ListingLineChunkBuilder chunk = new ListingLineChunkBuilder();
        chunk.data = data;
        chunk.offset = offset;
        chunk.length = length;
        chunk.columnFrom = columnFrom;
        chunk.columnTo = columnTo;

        return chunk;
    }

    public ListingLineChunkBuilder not() {
        this.not = true;
        return this;
    }

    public ListingLineChunkBuilder important() {
        this.important = true;
        return this;
    }

    public ListingLineChunkBuilder comment() {
        this.comment = true;
        return this;
    }

    public ListingLineChunkBuilder mark(int markLevel) {
        this.mark = true;
        this.markLevel = markLevel;
        return this;
    }

    public ListingLineChunkBuilder strong(int strongLevel) {
        this.strong = true;
        this.strongLevel = strongLevel;
        return this;
    }

    public ListingLineChunkBuilder highlight() {
        this.highlight = true;
        return this;
    }

    public String getText() {
        return new String(data, offset, length);
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

    public int getColumnFrom() {
        return columnFrom;
    }

    public int getColumnTo() {
        return columnTo;
    }


    public List<ListingLineChunkBuilder> getChunks() {
        return chunks;
    }


}
