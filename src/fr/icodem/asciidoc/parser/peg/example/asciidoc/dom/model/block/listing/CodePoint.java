package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.listing;

public class CodePoint {
    private int line;
    private int column;

    @Override
    public String toString() {
        return "CodePoint{" +
                "line=" + line +
                ", column=" + column +
                '}';
    }

    public static CodePoint ofLine(int line) {
        CodePoint cp = new CodePoint();
        cp.line = line;
        cp.column = -1;
        return cp;
    }

    public static CodePoint ofPoint(int line, int column) {
        CodePoint cp = new CodePoint();
        cp.line = line;
        cp.column = column;
        return cp;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}
