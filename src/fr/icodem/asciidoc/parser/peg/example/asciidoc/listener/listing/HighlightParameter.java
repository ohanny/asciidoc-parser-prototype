package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing;

public class HighlightParameter {
    private CodePoint from;
    private CodePoint to;

    private boolean not;
    private boolean important;
    private boolean comment;
    private boolean mark;
    private boolean strong;
    private boolean highlight;

    @Override
    public String toString() {
        return "HighlightParameter{" +
                "from=" + from +
                ", to=" + to +
                ", not=" + not +
                ", important=" + important +
                ", comment=" + comment +
                ", mark=" + mark +
                ", strong=" + strong +
                ", highlight=" + highlight +
                '}';
    }

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

    public static HighlightParameter strong(CodePoint from, CodePoint to) {
        HighlightParameter param = normal(from, to);
        param.strong = true;
        return param;
    }

    public static HighlightParameter highlight(CodePoint from, CodePoint to) {
        HighlightParameter param = normal(from, to);
        param.highlight = true;
        return param;
    }

    public HighlightParameter derive(CodePoint from, CodePoint to) {
        HighlightParameter param = new HighlightParameter();
        param.not = not;
        param.important = important;
        param.comment = comment;
        param.mark = mark;
        param.highlight = highlight;
        param.from = from;
        param.to = to;

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

    public boolean isStrong() {
        return strong;
    }

    public boolean isHighlight() {
        return highlight;
    }
}
