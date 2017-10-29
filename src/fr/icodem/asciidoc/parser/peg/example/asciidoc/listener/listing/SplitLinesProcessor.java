package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing;

import java.util.LinkedList;
import java.util.List;

public class SplitLinesProcessor {
    public List<LineContext> process(char[] input) {
        List<LineContext> lines = new LinkedList<>();

        int offset = 0;
        int count = 0;
        for (int i = 0; i < input.length; i++) {
            char c = input[i];
            switch (c) {
                case '\r':
                    break;
                case '\n':
                    int lineNumber = lines.size() + 1;
                    lines.add(LineContext.of(lineNumber, input, offset, count));
                    offset = i + 1;
                    count = 0;
                    break;
                default:
                    count++;
                    break;
            }
        }

        if (input[input.length - 1] != '\n') {
            int lineNumber = lines.size() + 1;
            lines.add(LineContext.of(lineNumber, input, offset, count));
        }

        return lines;
    }
}
