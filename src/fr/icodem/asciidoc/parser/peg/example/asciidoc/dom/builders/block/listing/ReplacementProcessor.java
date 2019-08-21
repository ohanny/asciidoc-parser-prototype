package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block.listing;

import java.util.List;

public class ReplacementProcessor {
    private char[] buffer = new char[2048]; // TODO size should be incremented as needed
    private int position;

    public void process(List<ListingLineBuilder> lines) {
        position = 0;
        lines.forEach(this::process);
    }

    private void process(ListingLineBuilder line) {
        line.chunks.forEach(this::process);
    }

    private void process(ListingLineChunkBuilder chunk) {
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
                case '€':
                    position += copy(ListingConstants.EURO, buffer, position);
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