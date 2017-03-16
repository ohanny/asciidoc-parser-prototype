package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.Listing;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ListingProcessor {
    //private char[] buffer = new char[128];
    private char[] buffer = new char[1024];

    private CalloutProcessor calloutProcessor = CalloutProcessor.newInstance();

    public static ListingProcessor newInstance() {
        return new ListingProcessor();
    }

    private ListingProcessor() {}

    public Listing process(char[] input, boolean source, String language, boolean linenums, boolean highlight, List<HighlightParameter> highlightParams) {
        List<Listing.Line> lines = new LinkedList<>();


        int count = 0;
        for (int i = 0; i < input.length; i++) {
            char c = input[i];
            switch (c) {
                case '<':
                    count += copy(ListingConstants.LT, buffer, count);
                    break;
                case '>':
                    count += copy(ListingConstants.GT, buffer, count);
                    break;
                case '&':
                    count += copy(ListingConstants.AMP, buffer, count);
                    break;
                case '\n':
                    int lineNumber = lines.size() + 1;
                    LineContext lineContext = LineContext.of(lineNumber, language, buffer, count);
                    calloutProcessor.processCallouts(lineContext);
                    String lineText = new String(lineContext.data, 0, lineContext.length);

                    List<Listing.LineChunk> chunks;
                    if (highlightParams != null && highlightParams.stream().anyMatch(p -> paramsForLine(p, lineNumber))) {
                        chunks = new LinkedList<>();
                        if (highlightParams.stream()
                                           .anyMatch(p -> paramsForLineNot(p, lineNumber))) {
                            chunks.add(Listing.LineChunk.of(lineText, true, false, false, false, false));
                        } else {
                            List<HighlightParameter> params =
                                    highlightParams.stream()
                                                   .filter(p -> paramsForLine(p, lineNumber))
                                                   .collect(Collectors.toList());

                            int pos = 1;
                            for (HighlightParameter p : params) {
                                int from = p.getFrom().getColumn();
                                if (from == -1) {
                                    from = 1;
                                }

                                if (pos > lineText.length()) {
                                    if (chunks.isEmpty()) {
                                        chunks.add(Listing.LineChunk.of(lineText, false, false, false, false, p.isHighlight()));
                                    }
                                    break;
                                }

                                if (from > lineText.length()) {
                                    chunks.add(Listing.LineChunk.of(lineText.substring(pos - 1), true, false, false, false, false));
                                    break;
                                }

                                if (from > pos) {
                                    chunks.add(Listing.LineChunk.of(lineText.substring(pos - 1, from - 1), false, false, false, false, false));
                                }

                                int to = p.getTo().getColumn();
                                if (to == -1 || to > lineText.length()) {
                                    to = lineText.length();
                                }

                                chunks.add(Listing.LineChunk.of(lineText.substring(from - 1, to), false, p.isImportant(), p.isComment(), p.isMark(), p.isHighlight()));
                                pos = to;
                            }

                            if (pos < lineText.length()) {
                                chunks.add(Listing.LineChunk.of(lineText.substring(pos), false, false, false, false, false));
                            }

                        }
                    } else {
                        chunks = Collections.singletonList(Listing.LineChunk.of(lineText, false, false, false, false, false));
                    }

                    lines.add(Listing.Line.of(lines.size() + 1, lineText, lineContext.callouts, chunks));
                    count = 0;
                    break;
                default:
                    buffer[count] = c;
                    count++;
                    break;
            }
        }

        Listing listing = Listing.of(lines, source, language, linenums, highlight);

        return listing;
    }

    private boolean paramsForLineNot(HighlightParameter p, int lineNumber) {
       return paramsForLine(p, lineNumber) && p.isNot();
    }

    private boolean paramsForLine(HighlightParameter p, int lineNumber) {
       return (p.getFrom().getLine() <= lineNumber && p.getTo().getLine() >= lineNumber)
               || (p.getFrom().getLine() == lineNumber && p.getTo().getLine() == -1);
    }

    private int copy(char[] data, char[] dest, int destPos) {
        for (int i = 0; i < data.length; i++) {
            dest[destPos + i] = data[i];
        }
        return data.length;
    }

}
