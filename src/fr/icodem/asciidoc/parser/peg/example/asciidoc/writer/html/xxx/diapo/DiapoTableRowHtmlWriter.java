package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.TableRow;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.TableRowHtmlWriter;

import java.io.IOException;

public class DiapoTableRowHtmlWriter extends TableRowHtmlWriter {


    public DiapoTableRowHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    public void write(TableRow row) throws IOException {

    }
}
