package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeEntry;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Content;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.ContentHtmlWriter;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.*;

public class DiapoContentHtmlWriter extends ContentHtmlWriter<DiapoContentHtmlWriter> {
    public DiapoContentHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    @Override
    protected void startContent(Content content) {
      append(BODY.start("class", "shower list")).nl()
        .incIndent();
    }

    @Override
    protected void endContent(Content content) {
        indent().append(DIV.start("class", "progress")).append(DIV.end()).nl()
          .indent().append(SCRIPT.start("src", "shower/shower.min.js")).append(SCRIPT.end()).nl()
          .incIndent()
            .includeHighlightjs()
          .decIndent()
          .decIndent()
          .indent().append(BODY.end()).nl()
        ;
    }

    private DiapoContentHtmlWriter includeHighlightjs() {
        boolean highlightjs = isAttributeValueEqualTo("source-highlighter", "highlightjs");
        boolean highlightSelective = isAttributeEnabled("highlight-selective");

        return appendIf(highlightjs, () ->
          indent().append(LINK.tag("rel", "stylesheet", "href", getAttributeEntryValue("highligthjs-style"))).nl()
            .indent().append(SCRIPT.start("src", getAttributeEntryValue("highligthjs-script"))).append(SCRIPT.end()).nl()
            .appendIf(!highlightSelective, () ->
              indent().append(SCRIPT.start()).append("hljs.initHighlightingOnLoad();").append(SCRIPT.end()).nl()
            )
            .appendIf(highlightSelective, () ->
              indent().append(SCRIPT.start()).nl()
                .incIndent()
                .indent().append("let blocks = document.querySelectorAll('" + getHighlightjsSelector() + "');").nl()
                .indent().append("for (let i = 0; i < blocks.length; ++i) {").nl()
                .incIndent()
                .indent().append("hljs.highlightBlock(blocks[i]);").nl()
                .decIndent()
                .indent().append("}").nl()
                .decIndent()
            )
            .indent().append(SCRIPT.end()).nl()
        )
        ;
    }

    protected String getHighlightjsSelector() {
        return "pre.highlight code";
    }

}
