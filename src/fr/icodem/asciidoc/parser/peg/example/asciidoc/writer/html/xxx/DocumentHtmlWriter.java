package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Document;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;

import java.io.IOException;

public abstract class DocumentHtmlWriter<DHW extends DocumentHtmlWriter<DHW>> extends ModelHtmlWriter<DHW> {

    public DocumentHtmlWriter(Outputter outputter, WriterSet writers) {
        super(outputter, writers);
    }

    public void write(Document document) throws IOException {
        writers.setDocument(document);

        startDocument();
        writeContent();
        endDocument();
    }

    protected abstract void startDocument() throws IOException;

    private void writeContent() throws IOException {
        if (document.getContent() != null) {
            writers.getContentWriter().write(document.getContent());
        }
    }

    protected abstract void endDocument() throws IOException;
}
