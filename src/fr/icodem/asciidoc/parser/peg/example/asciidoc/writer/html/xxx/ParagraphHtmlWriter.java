package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Paragraph;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

public abstract class ParagraphHtmlWriter extends ModelHtmlWriter<ParagraphHtmlWriter> {

    public ParagraphHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(Paragraph p) {
        startParagraph(p);
        writeContent(p);
        endParagraph(p);
    }

    protected abstract void startParagraph(Paragraph p);

    private void writeContent(Paragraph p) {
        getTextWriter().write(p.getText());
    }

    protected abstract void endParagraph(Paragraph p);
}
