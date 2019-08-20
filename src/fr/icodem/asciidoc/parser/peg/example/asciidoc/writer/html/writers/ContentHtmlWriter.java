package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Content;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Section;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

import java.io.IOException;

public abstract class ContentHtmlWriter <DHW extends ContentHtmlWriter<DHW>> extends ModelHtmlWriter<DHW> {

    public ContentHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(Content content) throws IOException {
        startContent(content);
        writeContent(content);
        endContent(content);
    }

    protected abstract void startContent(Content content) throws IOException;

    protected void writeContent(Content content) throws IOException {
        if (content.getPreamble() != null) {
            getWriters().getPreambleWriter().write(content.getPreamble());
        }
        if (content.getSections() != null) {
            for (Section section : content.getSections()) {
                getWriters().getSectionWriter().write(section);
            }
        }
    }

    protected abstract void endContent(Content content) throws IOException;

}
