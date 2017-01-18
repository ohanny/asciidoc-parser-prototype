package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing;

public class HighlightParameter {
    private CodePoint from;
    private CodePoint to;

    private boolean not;
    private boolean important;
    private boolean comment;
    private boolean mark;
    private boolean highlight;

    public static HighlightParameter normal(CodePoint from, CodePoint to) {
        HighlightParameter param = new HighlightParameter();
        param.from = from;
        param.to = to;

        return param;
    }

    public static HighlightParameter not(CodePoint from, CodePoint to) {
        HighlightParameter param = normal(from, to);
        param.not = true;
        return param;
    }

    public static HighlightParameter important(CodePoint from, CodePoint to) {
        HighlightParameter param = normal(from, to);
        param.important = true;
        return param;
    }

    public static HighlightParameter comment(CodePoint from, CodePoint to) {
        HighlightParameter param = normal(from, to);
        param.comment = true;
        return param;
    }

    public static HighlightParameter mark(CodePoint from, CodePoint to) {
        HighlightParameter param = normal(from, to);
        param.mark = true;
        return param;
    }

    public static HighlightParameter highlight(CodePoint from, CodePoint to) {
        HighlightParameter param = normal(from, to);
        param.highlight = true;
        return param;
    }

    public CodePoint getFrom() {
        return from;
    }

    public CodePoint getTo() {
        return to;
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

    public boolean isHighlight() {
        return highlight;
    }
}
