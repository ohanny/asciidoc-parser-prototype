package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Element;

public abstract class Block extends Element {
    protected Title title;
    protected AttributeList attributes;

    public Title getTitle() {
        return title;
    }

    public AttributeList getAttributes() {
        return attributes;
    }

}
