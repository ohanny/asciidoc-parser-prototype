package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class HighlightListenerDelegate {

    private Consumer<List<HighlightParameter>> consumer;

    private List<HighlightParameterContext> parameters = new LinkedList<>();
    private HighlightParameterContext currentParameter;

    private static class HighlightParameterContext {
        int lineFrom = -1;
        int lineTo = -1;
        int columnFrom = -1;
        int columnTo = -1;

        boolean not;
        boolean important;
        boolean comment;
        boolean mark;
    }

    public HighlightListenerDelegate(Consumer<List<HighlightParameter>> consumer) {
        this.consumer = consumer;
    }

    public void enterHighlight() {

    }

    public void exitHighlight() {

        List<HighlightParameter> params =
                parameters.stream()
                          .map(this::process)
                          .filter(p -> p!= null)
                          .collect(Collectors.toList());
        consumer.accept(params);

        parameters.clear();
    }

    private HighlightParameter process(HighlightParameterContext ctx) {
        if (ctx.lineTo >= ctx.lineFrom || ctx.lineTo == -1) {
            if (ctx.columnFrom == -1) {
                ctx.columnFrom = 1;
            }
            CodePoint from = CodePoint.ofPoint(ctx.lineFrom, ctx.columnFrom);
            if (ctx.lineTo == -1) {
                ctx.lineTo = ctx.lineFrom;
            }
            CodePoint to = CodePoint.ofPoint(ctx.lineTo, ctx.columnTo);

            if (ctx.not) {
                return HighlightParameter.not(from, to);
            }
            else if (ctx.important) {
                return HighlightParameter.important(from, to);
            }
            else if (ctx.comment) {
                return HighlightParameter.comment(from, to);
            }
            else if (ctx.mark) {
                return HighlightParameter.mark(from, to);
            }
            else {
                return HighlightParameter.normal(from, to);
            }
        }
        return null;
    }

    public void enterParameterSet() {
        currentParameter = new HighlightParameterContext();
        parameters.add(currentParameter);
    }

    public void exitParameterSet() {
        currentParameter = null;
    }

    public void lineFrom(int line) {
        currentParameter.lineFrom = line;
    }

    public void lineTo(int line) {
        currentParameter.lineTo = line;
    }

    public void columnFrom(int column) {
        currentParameter.columnFrom = column;
    }

    public void columnTo(int column) {
        currentParameter.columnTo = column;
    }

    public void not() {
        currentParameter.not = true;
    }

    public void important() {
        currentParameter.important = true;
    }

    public void comment() {
        currentParameter.comment = true;
    }

    public void mark() {
        currentParameter.mark = true;
    }

}
