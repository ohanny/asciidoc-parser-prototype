package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Paragraph;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.ParagraphHtmlWriter;

import java.io.IOException;

public class DiapoParagraphHtmlWriter extends ParagraphHtmlWriter {

    public DiapoParagraphHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    public void write(Paragraph p) throws IOException {

    }
}
