package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.FormattedText;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.Listing;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ListingProcessor {
    private SplitLinesProcessor splitLinesProcessor = new SplitLinesProcessor();
    private CalloutProcessor calloutProcessor = CalloutProcessor.newInstance();
    private CodeMarksProcessor codeMarksProcessor = new CodeMarksProcessor();
    private ReplacementProcessor replacementProcessor = new ReplacementProcessor();


    public static ListingProcessor newInstance() {
        return new ListingProcessor();
    }

    private ListingProcessor() {}

    public Listing process(FormattedText title, char[] input, boolean source, String language, boolean linenums, boolean highlight, List<HighlightParameter> highlightParams) {

        List<LineContext> lines = splitLinesProcessor.process(input);

        lines.stream()
             .forEach(l -> calloutProcessor.processCallouts(l));

        codeMarksProcessor.process(lines, highlightParams);
        replacementProcessor.process(lines);

        // result
        List<Listing.Line> listingLines = new LinkedList<>();
        lines.stream()
             .forEach(l -> {

                 List<Listing.LineChunk> chunks =
                         l.chunks
                          .stream()
                          .map(lc -> transform(lc))
                          .collect(Collectors.toList());

                 listingLines.add(Listing.Line.of(l.lineNumber, l.getText(), l.callouts, chunks));
             });

        return Listing.of(title, listingLines, source, language, linenums, highlight);
    }



    private Listing.LineChunk transform(LineChunkContext chunk) {
        List<Listing.LineChunk> nestedChunks = null;
        if (chunk.getChunks() != null) {
            nestedChunks =
                    chunk.getChunks()
                      .stream()
                      .map(lc -> transform(lc))
                      .collect(Collectors.toList());
        }

        return Listing.LineChunk.of(chunk.getText(), chunk.isNot(), chunk.isImportant(), chunk.isComment(), chunk.isMark(),
                                    chunk.isStrong(), chunk.isHighlight(), chunk.getMarkLevel(), nestedChunks);
    }

}
