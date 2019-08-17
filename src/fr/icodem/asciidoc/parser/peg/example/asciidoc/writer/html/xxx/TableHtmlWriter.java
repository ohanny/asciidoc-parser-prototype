package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Table;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

public abstract class TableHtmlWriter extends ModelHtmlWriter<TableHtmlWriter> {

    public TableHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    public abstract void write(Table table);
}
