package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Document;

public class WriterState {
    private WriterSet writerSet;
    private Document document;

    private int indent;

    public static WriterState newInstance() {
        return new WriterState();
    }

    public WriterSet getWriterSet() {
        return writerSet;
    }

    public void setWriterSet(WriterSet writerSet) {
        this.writerSet = writerSet;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public int getIndent() {
        return indent;
    }

    public void incIndent() {
        this.indent++;
    }

    public void decIndent() {
        this.indent--;
    }
}
