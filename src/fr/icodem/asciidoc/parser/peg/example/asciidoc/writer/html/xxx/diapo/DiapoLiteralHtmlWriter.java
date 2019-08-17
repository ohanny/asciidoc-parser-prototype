package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.LiteralBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.LiteralHtmlWriter;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.DIV;
import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.PRE;

public class DiapoLiteralHtmlWriter extends LiteralHtmlWriter {

    public DiapoLiteralHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    protected void startLiteral(LiteralBlock literal) {
        indent().append(DIV.start("class", "literalblock")).nl()
                .incIndent()
                .writeBlockTitle(literal)
                .indent().append(DIV.start("class", "content")).nl()
                .incIndent()
        ;

    }

    @Override
    protected void writeContent(LiteralBlock literal) {
        indent().append(PRE.start()).append(() -> getTextWriter().write(literal.getText())).append(PRE.end()).nl();
    }

    @Override
    protected void endLiteral(LiteralBlock literal) {
        decIndent()
                .indent().append(DIV.end()).nl()
                .decIndent()
                .indent().append(DIV.end()).nl()
        ;

    }

}
