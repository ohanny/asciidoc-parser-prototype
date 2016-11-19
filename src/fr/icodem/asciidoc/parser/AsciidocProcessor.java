package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.antlr.AsciidocBaseListener;
import fr.icodem.asciidoc.parser.elements.AttributeEntry;
import fr.icodem.asciidoc.parser.elements.ElementFactory;

import java.util.List;
import java.util.Map;

@Deprecated
public abstract class AsciidocProcessor extends AsciidocBaseListener {
    protected AsciidocParserHandler handler;
    protected ElementFactory ef;
    protected Map<String, AttributeEntry> attributes;

    public AsciidocProcessor(AsciidocParserHandler handler, List<AttributeEntry> attributes) {
        this.handler = handler;
        if (handler == null) throw new IllegalArgumentException("Handler must not be null");
        this.ef = new ElementFactory();

        this.attributes = AttributeDefaults.Instance.getAttributes();
        for (AttributeEntry att : attributes) {
            this.attributes.put(att.getName(), att);
        }
    }

    abstract void parse(String text);
}
