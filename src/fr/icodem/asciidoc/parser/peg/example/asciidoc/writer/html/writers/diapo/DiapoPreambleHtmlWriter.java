package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Preamble;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.PreambleHtmlWriter;

import static fr.icodem.asciidoc.backend.html.HtmlTag.H1;
import static fr.icodem.asciidoc.backend.html.HtmlTag.HEADER;

public class DiapoPreambleHtmlWriter extends PreambleHtmlWriter<DiapoPreambleHtmlWriter> {

    public DiapoPreambleHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    protected void startPreamble(Preamble preamble) {
        indent().append(HEADER.start("class", "caption")).nl()
          .incIndent()
            .includeTitle()
          //.decIndent()
        ;
    }

    private DiapoPreambleHtmlWriter includeTitle() {
        if (!getDocument().hasTitle()) return this;

        String title = replaceSpecialCharacters(getDocument().getHeader().getDocumentTitle().getText());
        return indent()
          .append(H1.start()).append(title).append(H1.end()).nl();
    }

    @Override
    protected void endPreamble(Preamble preamble) {
        decIndent()
          .indent().append(HEADER.end()).nl();
    }
}
