package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Section;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

import java.io.IOException;

public abstract class SectionHtmlWriter <DHW extends SectionHtmlWriter<DHW>> extends ModelHtmlWriter<DHW> {

    public SectionHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public void write(Section section) throws IOException {
        start(section);
        writeContent(section);
        end(section);
    }

    private void writeContent(Section section) throws IOException {
        writeBlocks(section.getBlocks());
    }

    protected abstract void start(Section section) throws IOException;

    protected abstract void end(Section section) throws IOException;
}
