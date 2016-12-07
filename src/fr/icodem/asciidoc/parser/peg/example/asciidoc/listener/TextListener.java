package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import fr.icodem.asciidoc.parser.peg.NodeContext;
import fr.icodem.asciidoc.parser.peg.listeners.ParseTreeListener;

public class TextListener implements ParseTreeListener {

    private AsciidocHandler handler;

    public TextListener(AsciidocHandler handler) {
        this.handler = handler;
    }

    @Override
    public void characters(NodeContext context, char[] chars, int startIndex, int endIndex) {
        if (context.getNodeName().equals("text")) {
            handler.writeText(new String(chars));
        }
    }

    @Override
    public void enterNode(NodeContext context) {
        switch (context.getNodeName()) {
            case "bold" :
                handler.startBold();
                break;
            case "italic" :
                handler.startItalic();
                break;
        }
    }

    @Override
    public void exitNode(NodeContext context) {
        switch (context.getNodeName()) {
            case "bold" :
                handler.endBold();
                break;
            case "italic" :
                handler.endItalic();
                break;
        }
    }
}
