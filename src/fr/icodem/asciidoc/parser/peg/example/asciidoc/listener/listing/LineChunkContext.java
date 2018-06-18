package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing;

public class LineChunkContext {
    char[] data;
    int offset;
    int length;

    private boolean not;
    private boolean important;
    private boolean comment;
    private boolean mark;
    private boolean strong;
    private boolean highlight;
    private int markLevel;

    public static LineChunkContext of(char[] data, int offset, int length) {
        LineChunkContext chunk = new LineChunkContext();
        chunk.data = data;
        chunk.offset = offset;
        chunk.length = length;

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

    public LineChunkContext strong() {
        this.strong = true;
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
}
