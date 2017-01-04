package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing;

public class ListingCallout {
    private int num;
    private int line;

    private ListingCallout(int num, int line) {
        this.num = num;
        this.line = line;
    }

    public static ListingCallout of(int num, int line) {
        return new ListingCallout(num, line);
    }

    public int getNum() {
        return num;
    }

    public int getLine() {
        return line;
    }

    @Override
    public String toString() {
        return "ListingCallout{" +
                "num=" + num +
                ", line=" + line +
                '}';
    }
}
