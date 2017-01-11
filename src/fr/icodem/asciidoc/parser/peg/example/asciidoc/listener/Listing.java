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

    private List<Line> lines;
    private String language;
    private boolean linenums;

    public static Listing of(List<Line> lines, String language, boolean linenums) {
        return new Listing(lines, language, linenums);
    }

    private Listing(List<Line> lines, String language, boolean linenums) {
        this.lines = lines;
        this.language = language;
        this.linenums = linenums;
    }

    public List<Line> getLines() {
        return lines;
    }

    public String getLanguage() {
        return language;
    }

    public boolean isLinenums() {
        return linenums;
    }

    public List<ListingCallout> getCallouts() {
        return lines.stream()
                .map(l -> l.getCallouts())
                .flatMap(c -> c.stream())
                .collect(Collectors.toList());
    }

}
