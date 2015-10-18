package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.antlr.AsciidocBaseListener;
import fr.icodem.asciidoc.parser.elements.ElementFactory;

public abstract class AsciidocProcessor extends AsciidocBaseListener {
    protected AsciidocParserHandler handler;
    protected ElementFactory ef;

    public AsciidocProcessor(AsciidocParserHandler handler) {
        this.handler = handler;
        if (handler == null) throw new IllegalArgumentException("Handler must not be null");
        this.ef = new ElementFactory();
    }

    abstract void parse(String text);
}
