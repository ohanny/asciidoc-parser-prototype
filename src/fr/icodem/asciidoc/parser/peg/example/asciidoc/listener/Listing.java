package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing.HighlightParameter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing.ListingCallout;

import java.util.List;
import java.util.stream.Collectors;

public class Listing {

    public static class LineChunk {
        private String text;
        private boolean not;
        private boolean important;
        private boolean comment;
        private boolean mark;

        public static LineChunk of(String text, boolean not, boolean important, boolean comment, boolean mark) {
            LineChunk chunk = new LineChunk();
            chunk.text = text;
            chunk.not = not;
            chunk.important = important;
            chunk.comment = comment;
            chunk.mark = mark;

            return chunk;
        }

        public String getText() {
            return text;
        }

        public boolean isNot() {
            return not;
        }

        public boolean isImportant() {
            return important;
        }

        public boolean isComment() {
            return comment;
        }

        public boolean isMark() {
            return mark;
        }
    }

    public static class Line {
        private int number;
        private String text;
        private List<ListingCallout> callouts;
        private List<LineChunk> chunks;

        private Line(int number, String text, List<ListingCallout> callouts, List<LineChunk> chunks) {
            this.number = number;
            this.text = text;
            this.callouts = callouts;
            this.chunks = chunks;
        }

        public static Line of(int number, String text, List<ListingCallout> callouts, List<LineChunk> chunks) {
            return new Line(number, text, callouts, chunks);
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

        public List<LineChunk> getLineChunks() {
            return chunks;
        }
    }

    private List<Line> lines;
    private boolean source;
    private String language;
    private boolean linenums;
    private boolean highlight;

    public static Listing of(List<Line> lines, boolean source, String language, boolean linenums, boolean highlight) {
        return new Listing(lines, source, language, linenums, highlight);
    }

    private Listing(List<Line> lines, boolean source, String language, boolean linenums, boolean highlight) {
        this.lines = lines;
        this.source = source;
        this.language = language;
        this.linenums = linenums;
        this.highlight = highlight;
    }

    public List<Line> getLines() {
        return lines;
    }

    public boolean isSource() {
        return source;
    }

    public String getLanguage() {
        return language;
    }

    public boolean isLinenums() {
        return linenums;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public List<ListingCallout> getCallouts() {
        return lines.stream()
                .map(l -> l.getCallouts())
                .flatMap(c -> c.stream())
                .collect(Collectors.toList());
    }

}
