package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing;

import java.util.List;

public class ReplacementProcessor {
    private char[] buffer = new char[1024];
    private int position;

    public void process(List<LineContext> lines) {
        position = 0;
        lines.forEach(this::process);
    }

    private void process(LineContext line) {
        line.chunks.forEach(this::process);
    }

    private void process(LineChunkContext chunk) {
        int start = position;
        for (int i = chunk.offset; i < chunk.offset + chunk.length; i++) {
            char c = chunk.data[i];
            switch (c) {
                case '<':
                    position += copy(ListingConstants.LT, buffer, position);
                    break;
                case '>':
                    position += copy(ListingConstants.GT, buffer, position);
                    break;
                case '&':
                    position += copy(ListingConstants.AMP, buffer, position);
                    break;
                default:
                    buffer[position] = c;
                    position++;
                    break;
            }
        }

        chunk.data = buffer;
        chunk.offset = start;
        chunk.length = position - start;
    }

    private int copy(char[] data, char[] dest, int destPos) {
        for (int i = 0; i < data.length; i++) {
            dest[destPos + i] = data[i];
        }
        return data.length;
    }

}