package fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.shower;

import fr.icodem.asciidoc.backend.html.HtmlTag;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.AttributeEntry;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.DocumentWriter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.html.DefaultHtmlRenderer;

import java.util.function.Consumer;

import static fr.icodem.asciidoc.backend.html.HtmlTag.*;

public class ShowerRenderer extends DefaultHtmlRenderer<ShowerRenderer> {
    private ShowerRenderer(DocumentWriter writer) {
        super(writer);
        startSection = this::startFirstSection;
    }

    public static ShowerRenderer withWriter(DocumentWriter writer) {
        return new ShowerRenderer(writer);
    }

    private Consumer<AttributeList> startSection;

    @Override
    protected String getBodyClass() {
        return "shower list";
    }

    protected ShowerRenderer includeCustomMeta() {
        indent()
          .append(META.tag("http-equiv", "x-ua-compatible", "content", "ie=edge"))
          .nl()
        ;
        return this;
    }

    @Override
    protected ShowerRenderer includeStylesheets() {
        AttributeEntry iconsAtt = getAttributeEntry("icons");
        indent()
          .append(LINK.tag("rel", "stylesheet", "href", "shower/styles/screen-16x10.css"))
          .nl()
          .runIf("font".equals(iconsAtt.getValue()), () ->
            indent()
              .append(LINK.tag("rel", "stylesheet", "href", "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css"))
              .nl()
          )
        ;
        return this;
    }

    @Override
    public void endDocument() {
        indent()
          .append(DIV.start("class", "progress"))
          .nl()
          .indent()
          .append(SCRIPT.start("src", "shower/shower.min.js"))
          .append(SCRIPT.end())
          .nl()
        ;

        super.endDocument();
    }

    @Override
    public void startHeader() {
        hasHeader = true; // TODO useful ?

        indent()
          .append(HEADER.start("class", "caption"))
          .nl()
          .incIndent()
        ;
    }

    @Override
    public void endHeader() {
        AttributeEntry tocAtt = getAttributeEntry("toc");

        moveTo("title")
          .indent()
          .append(TITLE.start())
          .append(title)
          .append(TITLE.end())
          .nl()
          .bufferOff()
          .runIf(!tocAtt.isDisabled(), () -> markOnWriter("TOC")) // TODO how to deal with toc ?
          .bufferOn()
          .mark("preamble")
          .decIndent()
          .indent()
          .append(HEADER.end())
          .nl()
        ;
    }

    @Override
    public void startPreamble() {
        moveTo("preamble");
    }

    @Override
    public void endPreamble() {}

    @Override
    public void startContent() {
        bufferOff();
    }

    @Override
    public void endContent() {
        // end section for last slide
        decIndent()
          .indent()
          .append(SECTION.end())
          .nl()
        ;
    }

    @Override
    public void startSection(int level, AttributeList attList) {
        startSection.accept(attList);
    }

    private void startFirstSection(AttributeList attList) {
        indent()
          .append(SECTION.start("class", getMoreClasses("slide", attList)))
          .nl()
          .incIndent()
        ;

        startSection = this::startNextSection;
    }

    private void startNextSection(AttributeList attList) {
        decIndent()
          .indent()
          .append(SECTION.end())
          .nl()
          .indent()
          .append(SECTION.start("class", getMoreClasses("slide", attList)))
          .nl()
          .incIndent()
        ;
    }

    @Override
    public void writeSectionTitle(int level, String title, String ref) {
        HtmlTag titleHeader = getTitleHeader(2);
        indent()
          .append(titleHeader.start("id", ref))
          .append(title)
          .append(titleHeader.end())
          .nl()
        ;
    }

    @Override
    public void endSection(int level) {}

}
