package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ExampleBlock;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.ExampleHtmlWriter;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.DIV;

public class DiapoExampleHtmlWriter extends ExampleHtmlWriter {

    public DiapoExampleHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    protected void startExample(ExampleBlock example) {
        indent().append(DIV.start("class", "exampleblock")).nl()
          .incIndent()
            .writeBlockTitle(example)
            .indent().append(DIV.start("class", "content")).nl()
              .incIndent()
        ;
    }

    @Override
    protected void endExample(ExampleBlock example) {
        decIndent()
          .indent().append(DIV.end()).nl()
            .decIndent()
            .indent().append(DIV.end()).nl()
        ;
    }

}
