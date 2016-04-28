package fr.icodem.asciidoc.parser.peg.buffers;

public class StringHolder {
    private String string;
    private int charsLoaded;

    public StringHolder(String string) {
        this.string = string;
    }

    public int length() {
        return string.length();
    }

    public char charAt(int index) {
        return string.charAt(index);
    }

    public void increment(int size) {
        charsLoaded += size;
    }

    public String getString() {
        return string;
    }

    public int getCharsLoaded() {
        return charsLoaded;
    }

    public void setCharsLoaded(int charsLoaded) {
        this.charsLoaded = charsLoaded;
    }
}
