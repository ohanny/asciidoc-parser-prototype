package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block.listing;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.listing.CodePoint;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.listing.HighlightParameter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class CodeMarksProcessor {
    public void process(List<ListingLineBuilder> lines, List<HighlightParameter> params) {
        lines.forEach(l -> processLine(l, params));
    }

    private void processLine(ListingLineBuilder line, List<HighlightParameter> params) {
        List<HighlightParameter> paramsForLine = filterAndSort(params, line);
        buildChunks(line, paramsForLine);
    }

    private List<HighlightParameter> filterAndSort(List<HighlightParameter> params, ListingLineBuilder line) {
        if (params == null) return null;

        return params.stream()
                     .filter(p -> paramsForLine(p, line.lineNumber))
                     .map(p -> fixEdgesOfLine(line, p))
                     .sorted(this::compareParams)
                     .collect(Collectors.toList());
    }

    private HighlightParameter fixEdgesOfLine(ListingLineBuilder line, HighlightParameter par) {
        boolean changed = false;
        CodePoint from = par.getFrom();
        CodePoint to = par.getTo();

        // start
        if (from.getColumn() == -1) {
            changed = true;
            from = CodePoint.ofPoint(line.lineNumber, 1);
        }

        // end
        if (par.getTo().getColumn() == -1) {
            changed = true;
            to = CodePoint.ofPoint(line.lineNumber, line.length);
        }
        if (par.getTo().getLine() == -1) {
            changed = true;
            to = CodePoint.ofPoint(from.getLine(), to.getColumn());
        }

        if (changed) return par.derive(from, to);

        return par;
    }

    private int compareParams(HighlightParameter par1, HighlightParameter par2) {
        int result = par1.getFrom().getLine() - par2.getFrom().getLine();

        if (result == 0) {
            result = par1.getFrom().getColumn() - par2.getFrom().getColumn();
            if (result == 0) {
                result = par2.getTo().getColumn() - par1.getTo().getColumn();
            }
        }

        return result;
    }

    private void buildChunks(ListingLineBuilder line, List<HighlightParameter> params) {
        List<ListingLineChunkBuilder> chunks = getChunks(1, line.length, line.lineNumber,
                                                  line.data, line.offset, params);

        if (chunks == null || chunks.isEmpty()) {
            chunks = Collections.singletonList(ListingLineChunkBuilder.of(line.data, line.offset, line.length, 1, line.length));
        }

        line.setChunks(chunks);

        chunks.stream()
              .forEach(c -> buildChunks(c, line, params));
    }

    private void buildChunks(ListingLineChunkBuilder chunk, ListingLineBuilder line, List<HighlightParameter> parameters) {
        if (parameters == null) return;

        List<HighlightParameter> params = paramsForChunk(parameters, chunk);

        if (!params.isEmpty()) {
            chunk.chunks = getChunks(chunk.columnFrom, chunk.columnTo, line.lineNumber,
                                                      chunk.data, line.offset, params);
            chunk.chunks.stream()
                    .forEach(c -> buildChunks(c, line, params));
        }
    }

    private List<HighlightParameter> paramsForChunk(List<HighlightParameter> params, ListingLineChunkBuilder chunk) {
        return params.stream()
                     .filter(p -> isParamForChunk(p, chunk))
                     .collect(Collectors.toList());
    }

    private boolean isParamForChunk(HighlightParameter param, ListingLineChunkBuilder chunk) {
        return  !param.isProcessed() && param.getFrom().getColumn() >= chunk.columnFrom && param.getTo().getColumn() <= chunk.columnTo;
    }

    private List<ListingLineChunkBuilder> getChunks(int columnFrom, int columnTo, int lineNumber, char[] data,
                                             int offset, List<HighlightParameter> params) {
        if (params == null || params.isEmpty()) return null;

        List<ListingLineChunkBuilder> chunks = new LinkedList<>();

        for (int pos = columnFrom; pos <= columnTo;) {
            HighlightParameter param = getNextParam(pos, lineNumber, columnTo, params);
            param.markAsProcessed();

            int length = param.getTo().getColumn() - param.getFrom().getColumn() + 1;

            ListingLineChunkBuilder chunk = ListingLineChunkBuilder.of(data, offset + pos - 1, length, pos, pos + length - 1);

            if (param.isImportant()) chunk.important();
            else if (param.isComment()) chunk.comment();
            else if (param.isMark()) {
                chunk.mark(param.getMarkLevel());
            } else if (param.isStrong()) {
                chunk.strong(param.getStrongLevel());
            }
            else if (param.isHighlight()) chunk.highlight();

            chunks.add(chunk);
            pos += length;
        }

        return chunks;
    }

    // when start of line, posInLine = 1
    private HighlightParameter getNextParam(int posInLine, int lineNumber, int limit, List<HighlightParameter> params) {
        for (HighlightParameter p : params) {
            if (posInLine < p.getFrom().getColumn()) {
                return HighlightParameter.normal(
                    CodePoint.ofPoint(lineNumber, posInLine),
                    CodePoint.ofPoint(lineNumber, p.getFrom().getColumn() - 1)
                );
            } else if (posInLine >= p.getFrom().getColumn() && (posInLine <= p.getTo().getColumn())) {
                return p;
            }
        }

        return HighlightParameter.normal(
            CodePoint.ofPoint(lineNumber, posInLine),
            CodePoint.ofPoint(lineNumber, limit)
        );
    }




    private boolean paramsForLine(HighlightParameter p, int lineNumber) {
        return (p.getFrom().getLine() <= lineNumber && p.getTo().getLine() >= lineNumber)
                || (p.getFrom().getLine() == lineNumber && p.getTo().getLine() == -1);
    }

    private boolean paramsForLineNot(HighlightParameter p, int lineNumber) {
        return paramsForLine(p, lineNumber) && p.isNot();
    }

}
