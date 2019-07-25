package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

public abstract class Block extends Element {
    protected Title title;

    public Title getTitle() {
        return title;
    }
}
