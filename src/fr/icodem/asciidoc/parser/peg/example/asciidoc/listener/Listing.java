package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing.ListingCallout;

import java.util.List;
import java.util.stream.Collectors;

public class Listing {

    public static class Line {
        private int number;
        private String text;
        private List<ListingCallout> callouts;

        private Line(int number, String text, List<ListingCallout> callouts) {
            this.number = number;
            this.text = text;
            this.callouts = callouts;
        }

        public static Line of(int number, String text, List<ListingCallout> callouts) {
            return new Line(number, text, callouts);
        }

        public int getNumber() {
            return number;
        }

        public String getText() {
            return text;
        }

        public List<ListingCallout> getCallouts() {
            return callouts;
        }
    }

    private String language;
    private List<Line> lines;

    public static Listing of(String language, List<Line> lines) {
        return new Listing(language, lines);
    }

    private Listing(String language, List<Line> lines) {
        this.language = language;
        this.lines = lines;
    }

    public String getLanguage() {
        return language;
    }

    public List<Line> getLines() {
        return lines;
    }

    public List<ListingCallout> getCallouts() {
        return lines.stream()
                .map(l -> l.getCallouts())
                .flatMap(c -> c.stream())
                .collect(Collectors.toList());
    }

}
