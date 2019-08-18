package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.DescriptionList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.DescriptionListHtmlWriter;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.DIV;
import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.DL;

public class DiapoDescriptionListHtmlWriter extends DescriptionListHtmlWriter {

    public DiapoDescriptionListHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    protected void startDescriptionList(DescriptionList list) {
        indent()
          .append(DIV.start("class", "dlist")).nl()
            .incIndent()
              .indent().append(DL.start()).nl()
              .incIndent()
        ;
    }

    @Override
    protected void endDescriptionList(DescriptionList list) {
        decIndent()
          .indent().append(DL.end()).nl()
          .decIndent()
          .indent().append(DIV.end()).nl()
        ;
    }

}
