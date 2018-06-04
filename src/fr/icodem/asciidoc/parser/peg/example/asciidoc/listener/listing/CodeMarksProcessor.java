package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing;

import java.util.LinkedList;
import java.util.List;

// TODO Naive implementation. This class needs to be re-implemented when highlight rules are well defined.
public class CodeMarksProcessor {
    public void process(List<LineContext> lines, List<HighlightParameter> params) {
        lines.forEach(l -> process(l, params));
    }

    /*
     warning : first implementation -> 'not' rule is not implemented, other rules must not be interlaced,
     params must be sorted
     */
    private void process(LineContext line, List<HighlightParameter> params) {
        List<LineChunkContext> chunks = new LinkedList<>();

        for (int pos = 0; pos < line.length;) {
            HighlightParameter param = getNextParam(line, pos, params);

            int length = param.getTo().getColumn() - param.getFrom().getColumn() + 1;

            LineChunkContext chunk = LineChunkContext.of(line.data, line.offset + pos, length);

            if (param.isImportant()) chunk.important();
            else if (param.isComment()) chunk.comment();
            else if (param.isMark()) chunk.mark();
            else if (param.isStrong()) chunk.strong();
            else if (param.isHighlight()) chunk.highlight();

            chunks.add(chunk);

            pos += length;
        }

        if (line.length == 0) {
            chunks.add(LineChunkContext.of(line.data, line.offset, line.length));
        }

        line.setChunks(chunks);
    }

    private HighlightParameter getNextParam(LineContext line, int posInLine, List<HighlightParameter> params) {
        if (params != null) {
            // find param for current position in line
            for (HighlightParameter p : params) {
                if (paramsForLine(p, line.lineNumber)) {
                    if (posInLine >= p.getFrom().getColumn() - 1 && posInLine <= p.getTo().getColumn() - 1) {
                        return p;
                    }
                    else if (posInLine == 0 && p.getFrom().getColumn() == -1 && p.getTo().getLine() == -1 && p.getTo().getColumn() == -1) {
                        return p.derive(
                                CodePoint.ofPoint(line.lineNumber, 1),
                                CodePoint.ofPoint(line.lineNumber, line.length)
                        );
                    }
                }
            }

            // find param for next position in line
            for (HighlightParameter p : params) {
                if (paramsForLine(p, line.lineNumber)) {
                    if (posInLine < p.getFrom().getColumn() - 1) {
                        return HighlightParameter.normal(
                                CodePoint.ofPoint(line.lineNumber, posInLine + 1),
                                CodePoint.ofPoint(line.lineNumber, p.getFrom().getColumn() - 1)
                        );
                    }
                }
            }
        }

        return HighlightParameter.normal(
                CodePoint.ofPoint(line.lineNumber, posInLine + 1),
                CodePoint.ofPoint(line.lineNumber, line.length)
        );
    }

    /*
    public void process1(LineContext line, List<HighlightParameter> params) {

        List<LineChunkContext> chunks;
        if (params != null && params.stream().anyMatch(p -> paramsForLine(p, line.lineNumber))) {
            chunks = new LinkedList<>();
            if (params.stream()
                    .anyMatch(p -> paramsForLineNot(p, line.lineNumber))) {
                chunks.add(LineChunkContext.of(line.data, line.offset, line.length).not());
            } else {
                List<HighlightParameter> paramsForLine =
                        params.stream()
                                .filter(p -> paramsForLine(p, line.lineNumber))
                                .collect(Collectors.toList());

                int pos = 1;
                for (HighlightParameter p : paramsForLine) {
                    int from = p.getFrom().getColumn();
                    if (from == -1) {
                        from = 1;
                    }

                    if (pos > line.length) {
                        if (chunks.isEmpty()) {
                            chunks.add(LineChunkContext.of(line.data, line.offset, line.length).highlight());
                        }
                        break;
                    }

                    if (from > line.length) {
                        chunks.add(LineChunkContext.of(line.data, pos - 1, line.length - pos).not());
                        break;
                    }

                    if (from > pos) {
                        chunks.add(LineChunkContext.of(line.data, pos - 1, from - 1));
                    }

                    int to = p.getTo().getColumn();
                    if (to == -1 || to > line.length) {
                        to = line.length;
                    }

                    LineChunkContext chunk = LineChunkContext.of(line.data, from - 1, to - from + 1);
                    chunks.add(chunk);
                    if (p.isImportant()) chunk.important();
                    if (p.isComment()) chunk.comment();
                    if (p.isMark()) chunk.mark();
                    if (p.isHighlight()) chunk.highlight();

                    pos = to;
                }

                if (pos < line.length) {
                    chunks.add(LineChunkContext.of(line.data, pos, line.length - pos));
                }


            }
        } else {
            chunks = Collections.singletonList(LineChunkContext.of(line.data, line.offset, line.length));
        }

        line.setChunks(chunks);
    }
    */

    private boolean paramsForLineNot(HighlightParameter p, int lineNumber) {
        return paramsForLine(p, lineNumber) && p.isNot();
    }

    private boolean paramsForLine(HighlightParameter p, int lineNumber) {
        //System.out.println(lineNumber + " => " + (p.getFrom().getLine() <= lineNumber && p.getTo().getLine() >= lineNumber) + " || " + (p.getFrom().getLine() == lineNumber && p.getTo().getLine() == -1));

        return (p.getFrom().getLine() <= lineNumber && p.getTo().getLine() >= lineNumber)
                || (p.getFrom().getLine() == lineNumber && p.getTo().getLine() == -1);
    }

}
