package fr.icodem.asciidoc.parser2;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

public class ParserReader {
    private char[] data;
    private int[] markers;
    private int position;
    private int index;

    public ParserReader(Reader reader) throws IOException {
        load(reader);
    }

    private void load(Reader reader) throws IOException {
        data = new char[1024];
        markers = new int[1000];

        for (int i : markers) {
            markers[i] = -1;
        }

        try {
            int numRead;
            do {
                //numRead = reader.read(data, position, 1024);
                numRead = reader.read(data, 0, 1024);
                position += numRead;
            } while (numRead != -1);
        } finally {
            if (reader != null) reader.close();
        }
    }

    public int read() {
        char c = data[++index];
        if (c != 0) {
            return c;
        }
        return -1;
    }

    public char lookahead(int index) {
        return data[index];
    }

    public void mark() {
        for (int i = 0; i < markers.length; i++) {
            if (markers[i] == -1) {
                markers[i] = index;
                break;
            }
        }
    }

    public void releaseMarker() {
        for (int i = 0; i < markers.length; i++) {
            if (markers[i] == -1 && i > 0) {
                markers[i - 1] = -1;
            }
        }
    }

    public void consume() {

    }

    public int getIndex() {
        return index;
    }

    public String getText(int from, int to) {
        char[] chars = Arrays.copyOfRange(data, from, to);
        String text = new String(chars);
        return text;
    }
}
