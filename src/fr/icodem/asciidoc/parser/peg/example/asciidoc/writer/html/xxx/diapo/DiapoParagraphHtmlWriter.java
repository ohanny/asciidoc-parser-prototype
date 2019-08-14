package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Paragraph;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.ParagraphHtmlWriter;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.DIV;
import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.P;

public class DiapoParagraphHtmlWriter extends ParagraphHtmlWriter {

    public DiapoParagraphHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    protected void startParagraph(Paragraph p) {
        String style = styleBuilder().reset(p.getAttributes()).addPosition().addSize().style();
        String classes = getMoreClasses("paragraph", p.getAttributes());
        indent().append(DIV.start("class", classes, "style", style)).nl()
          .incIndent()
          .indent().append(P.start())
        ;
    }

    @Override
    protected void endParagraph(Paragraph p) {
        append(P.end()).nl()
          .decIndent()
          .indent().append(DIV.end()).nl();
    }

}
