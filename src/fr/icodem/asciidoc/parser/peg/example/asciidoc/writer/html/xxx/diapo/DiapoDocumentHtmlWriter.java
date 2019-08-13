package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeEntry;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.DocumentHtmlWriter;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlTag.*;

public class DiapoDocumentHtmlWriter extends DocumentHtmlWriter<DiapoDocumentHtmlWriter> {

    public DiapoDocumentHtmlWriter(Outputter outputter, WriterSet writers) {
        super(outputter, writers);
    }

    @Override
    protected void startDocument() {
        append(DOCTYPE.tag()).nl()
          .append(HTML.start()).nl()
          .append(HEAD.start()).nl()
          .incIndent()
            .includeMetadata()
            .includeAuthors()
            .includeStylesheets()
            .includeTitle()
          .decIndent()
          .append(HEAD.end()).nl()
        ;
    }

    protected DiapoDocumentHtmlWriter includeMetadata() {
        return indent().append(META.tag("charset", "utf-8")).nl()
          .indent().append(META.tag("name", "viewport", "content", "width=device-width, initial-scale=1.0")).nl()
          .indent().append(META.tag("name", "generator", "content", "iodoc")).nl()
          .indent().append(META.tag("http-equiv", "x-ua-compatible", "content", "ie=edge")).nl()
        ;
    }

    protected DiapoDocumentHtmlWriter includeAuthors() {
        return this;
    }

    protected DiapoDocumentHtmlWriter includeTitle() {
        if (!document.hasTitle()) return this;
        String title = document.getHeader().getDocumentTitle().getText();
        return indent().append(TITLE.start()).append(title).append(TITLE.end()).nl();
    }

    protected DiapoDocumentHtmlWriter includeStylesheets() {
        AttributeEntry iconsAtt = getAttributeEntry("icons");
        return indent().append(LINK.tag("rel", "stylesheet", "href", "shower/styles/styles.css")).nl()
          .appendIf("font".equals(iconsAtt.getValue()), () ->
            indent().append(LINK.tag("rel", "stylesheet", "href", "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css")).nl()
          )
          .indent().append(LINK.tag("rel", "stylesheet", "href", "iodoc/style.css")).nl()
          .indent().append(STYLE.start()).nl()
          .incIndent()
            .indent().append(".slide pre.nolinenums code:not(:only-child)::before {content: none;}").nl()
            .indent().append(".shower { --slide-ratio: calc(16 / 10); }").nl()
          .decIndent()
          .indent().append(STYLE.end()).nl()
        ;
    }

    @Override
    protected void endDocument() {
        decIndent().append(HTML.end());
    }

}
