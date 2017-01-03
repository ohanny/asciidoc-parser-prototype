package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

public class ListingProcessor {
    private char[] buffer = new char[512];

    private final static char[] LT = "&lt;".toCharArray();
    private final static char[] GT = "&gt;".toCharArray();

    public static ListingProcessor newInstance() {
        return new ListingProcessor();
    }

    public String process(char[] input) {

        int count = 0;
        for (int i = 0; i < input.length; i++) {
            char c = input[i];
            switch (c) {
                case '<':
                    count += copy(LT, buffer, count);
                    break;
                case '>':
                    count += copy(GT, buffer, count);
                    break;
                default:
                    buffer[count] = c;
                    count++;
                    break;
            }
        }

        return new String(buffer, 0, count);
    }

    private int copy(char[] data, char[] dest, int destPos) {
        for (int i = 0; i < data.length; i++) {
            dest[destPos + i] = data[i];
        }
        return data.length;
    }
}
