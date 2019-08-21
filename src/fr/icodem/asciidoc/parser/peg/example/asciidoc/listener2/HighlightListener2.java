package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener2;

import fr.icodem.asciidoc.parser.peg.NodeContext;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.listing.HighlightParameter;
import fr.icodem.asciidoc.parser.peg.listeners.ParseTreeListener;

import java.util.List;
import java.util.function.Consumer;

public class HighlightListener2 implements ParseTreeListener {

    private HighlightListenerDelegate2 delegate;

    public HighlightListener2(Consumer<List<HighlightParameter>> consumer) {
        this.delegate = new HighlightListenerDelegate2(consumer);
    }

    @Override
    public void characters(NodeContext context, char[] chars, int startIndex, int endIndex) {
        switch (context.getNodeName()) {
            case "lineFrom":
                delegate.lineFrom(getInt(chars, startIndex, endIndex));
                break;

            case "lineTo":
                delegate.lineTo(getInt(chars, startIndex, endIndex));
                break;

            case "columnFrom":
                delegate.columnFrom(getInt(chars, startIndex, endIndex));
                break;

            case "columnTo":
                delegate.columnTo(getInt(chars, startIndex, endIndex));
                break;

            case "markLevel":
                delegate.markLevel(getInt(chars, startIndex, endIndex));
                break;

            case "strongLevel":
                delegate.strongLevel(getInt(chars, startIndex, endIndex));
                break;
        }
    }

    private String getString(char[] chars, int startIndex, int endIndex) {
        //return new String(chars, startIndex, endIndex);
        return new String(chars);// TODO recycle char tab => add offset + length (cf SAX)
    }

    private int getInt(char[] chars, int startIndex, int endIndex) {
        return Integer.parseInt(getString(chars, startIndex, endIndex));
    }

    @Override
    public void enterNode(NodeContext context) {
        switch (context.getNodeName()) {
            case "highlights":
                delegate.enterHighlights();
                break;

            case "parameterSet":
                delegate.enterParameterSet();
                break;

            case "not":
                delegate.not();
                break;
            case "important":
                delegate.important();
                break;
            case "comment":
                delegate.comment();
                break;
            case "mark":
                delegate.mark();
                break;
            case "strong":
                delegate.strong();
                break;
        }

    }

    @Override
    public void exitNode(NodeContext context) {
        switch (context.getNodeName()) {
            case "highlights":
                delegate.exitHighlights();
                break;

            case "parameterSet":
                delegate.exitParameterSet();
                break;
        }

    }
}
