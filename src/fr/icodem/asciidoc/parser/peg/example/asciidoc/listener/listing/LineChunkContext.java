package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing;

import java.util.List;

public class LineChunkContext {
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

    List<LineChunkContext> chunks; // nested chunks

    public static LineChunkContext of(char[] data, int offset, int length, int columnFrom, int columnTo) {
        LineChunkContext chunk = new LineChunkContext();
        chunk.data = data;
        chunk.offset = offset;
        chunk.length = length;
        chunk.columnFrom = columnFrom;
        chunk.columnTo = columnTo;

        return chunk;
    }

    public LineChunkContext not() {
        this.not = true;
        return this;
    }

    public LineChunkContext important() {
        this.important = true;
        return this;
    }

    public LineChunkContext comment() {
        this.comment = true;
        return this;
    }

    public LineChunkContext mark(int markLevel) {
        this.mark = true;
        this.markLevel = markLevel;
        return this;
    }

    public LineChunkContext strong(int strongLevel) {
        this.strong = true;
        this.strongLevel = strongLevel;
        return this;
    }

    public LineChunkContext highlight() {
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


    public List<LineChunkContext> getChunks() {
        return chunks;
    }

}
