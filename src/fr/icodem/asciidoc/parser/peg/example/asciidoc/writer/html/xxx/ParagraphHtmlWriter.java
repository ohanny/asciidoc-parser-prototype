package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Paragraph;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Section;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

import java.io.IOException;

public abstract class ParagraphHtmlWriter extends ModelHtmlWriter<ParagraphHtmlWriter> {

    public ParagraphHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(Paragraph p) throws IOException {
        startParagraph(p);
        writeContent(p);
        endParagraph(p);
    }

    protected abstract void startParagraph(Paragraph p);

    private void writeContent(Paragraph p) throws IOException {
        getTextWriter().write(p.getText());
    }

    protected abstract void endParagraph(Paragraph p);
}