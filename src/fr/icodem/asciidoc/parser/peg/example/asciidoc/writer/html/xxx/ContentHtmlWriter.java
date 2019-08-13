package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Content;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Section;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;

import java.io.IOException;

public abstract class ContentHtmlWriter <DHW extends ContentHtmlWriter<DHW>> extends ModelHtmlWriter<DHW> {

    public ContentHtmlWriter(Outputter outputter, WriterSet writers) {
        super(outputter, writers);
    }

    public void write(Content content) throws IOException {
        startContent(content);
        writeContent(content);
        endContent(content);
    }

    protected abstract void startContent(Content content) throws IOException;

    protected void writeContent(Content content) throws IOException {
        if (content.getPreamble() != null) {
            writers.getPreambleWriter().write(content.getPreamble());
        }
        if (content.getSections() != null) {
            for (Section section : content.getSections()) {
                writers.getSectionWriter().write(section);
            }
        }
    }

    protected abstract void endContent(Content content) throws IOException;

}
