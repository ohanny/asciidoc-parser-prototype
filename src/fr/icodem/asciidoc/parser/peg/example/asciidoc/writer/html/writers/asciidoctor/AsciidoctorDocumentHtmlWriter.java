package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.asciidoctor;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.DocumentHtmlWriter;

import java.io.IOException;

public class AsciidoctorDocumentHtmlWriter extends DocumentHtmlWriter {


    public AsciidoctorDocumentHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    protected void startDocument() throws IOException {

    }

    @Override
    protected void endDocument() throws IOException {

    }

}
