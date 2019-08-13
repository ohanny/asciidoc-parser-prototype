package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Author;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;

import java.io.IOException;
import java.util.List;

public abstract class AuthorsHtmlWriter extends ModelHtmlWriter<AuthorsHtmlWriter> {

    public AuthorsHtmlWriter(Outputter outputter, WriterSet writers) {
        super(outputter, writers);
    }

    public abstract void write(List<Author> authors) throws IOException;
}
