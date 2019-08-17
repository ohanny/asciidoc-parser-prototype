package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Document;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

import java.io.IOException;

public abstract class DocumentHtmlWriter<DHW extends DocumentHtmlWriter<DHW>> extends ModelHtmlWriter<DHW> {

    public DocumentHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(Document document) throws IOException {
        state.setDocument(document);

        startDocument();
        writeContent();
        endDocument();
    }

    protected abstract void startDocument() throws IOException;

    private void writeContent() throws IOException {
        if (getDocument().getContent() != null) {
            getWriters().getContentWriter().write(getDocument().getContent());
        }
    }

    protected abstract void endDocument() throws IOException;
}
