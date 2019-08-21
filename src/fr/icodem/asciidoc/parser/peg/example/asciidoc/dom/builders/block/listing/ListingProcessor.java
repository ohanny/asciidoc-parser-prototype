package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block.listing;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.listing.HighlightParameter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.listing.ListingLine;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.listing.ListingLineChunk;

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

    public List<ListingLine> process(char[] input, List<HighlightParameter> highlightParams) {

        List<ListingLineBuilder> lines = splitLinesProcessor.process(input);

        lines.stream()
             .forEach(l -> calloutProcessor.processCallouts(l));

        codeMarksProcessor.process(lines, highlightParams);
        replacementProcessor.process(lines);

        // result
        List<ListingLine> listingLines = new LinkedList<>();
        lines.stream()
             .forEach(l -> {

                 List<ListingLineChunk> chunks =
                         l.chunks
                          .stream()
                          .map(lc -> transform(lc))
                          .collect(Collectors.toList());

                 listingLines.add(ListingLine.of(l.lineNumber, l.getText(), l.callouts, chunks));
             });

        return listingLines;
    }



    private ListingLineChunk transform(ListingLineChunkBuilder chunk) {
        List<ListingLineChunk> nestedChunks = null;
        if (chunk.getChunks() != null) {
            nestedChunks =
                    chunk.getChunks()
                      .stream()
                      .map(lc -> transform(lc))
                      .collect(Collectors.toList());
        }

        return ListingLineChunk.of(chunk.getText(), chunk.isNot(), chunk.isImportant(), chunk.isComment(), chunk.isMark(),
                                    chunk.isStrong(), chunk.isHighlight(), chunk.getMarkLevel(), chunk.getStrongLevel(), nestedChunks);
    }

}
