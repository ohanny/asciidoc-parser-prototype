package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.Listing;

import java.util.ArrayList;
import java.util.List;

public class ListingProcessor {
    private char[] buffer = new char[128];

    private CalloutProcessor calloutProcessor = CalloutProcessor.newInstance();

    public static ListingProcessor newInstance() {
        return new ListingProcessor();
    }

    private ListingProcessor() {}

    public Listing process(String language, char[] input) {
        List<Listing.Line> lines = new ArrayList<>();


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
                    LineContext lineContext = LineContext.of(lines.size() + 1, language, buffer, count);
                    calloutProcessor.processCallouts(lineContext);
                    String lineText = new String(lineContext.data, 0, lineContext.length);
                    lines.add(Listing.Line.of(lines.size() + 1, lineText, lineContext.callouts));
                    count = 0;
                    break;
                default:
                    buffer[count] = c;
                    count++;
                    break;
            }
        }

        Listing listing = Listing.of(language, lines);

        return listing;
    }

    private int copy(char[] data, char[] dest, int destPos) {
        for (int i = 0; i < data.length; i++) {
            dest[destPos + i] = data[i];
        }
        return data.length;
    }

}
