package fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.shower;

import fr.icodem.asciidoc.backend.html.HtmlTag;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.AttributeEntry;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.AttributeList;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.Listing;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.DocumentWriter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.html.CssElement;
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
          .indent()
          .append(LINK.tag("rel", "stylesheet", "href", "highlight/styles/atom-one-light.css"))
          .nl()
          .indent()
          .append(LINK.tag("rel", "stylesheet", "href", "iodoc/style.css"))
          .nl()
          .indent()
          .append(SCRIPT.start("src", "highlight/highlight.pack.js"))
          .append(SCRIPT.end())
           .nl()
           .indent()
           .append(SCRIPT.start())
           .append("hljs.initHighlightingOnLoad();")
           .append(SCRIPT.end())
           .nl()
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


    @Override
    public void startList() {
        bufferOn();
    }

    @Override
    public void endList() {
        bufferOff();
    }

    @Override
    public void startOrderedList(int level, AttributeList atts) {
        runIf(level > 1, () -> moveTo("BeforeEndLI" + (level-1)))
          .indent()
          .append(OL.start())
          .nl()
          .incIndent();
    }

    @Override
    public void endOrderedList(int level) {
        decIndent()
          .indent()
          .append(OL.end())
          .nl()
          .runIf(level > 1, () -> moveTo("AfterEndLI" + (level-1)));
    }

    @Override
    public void startUnorderedList(int level, AttributeList attList) {
        runIf(level > 1, () -> moveTo("BeforeEndLI" + (level-1)))
          .indent()
          .append(UL.start())
          .nl()
          .incIndent();
    }

    @Override
    public void endUnorderedList(int level) {
        decIndent()
          .indent()
          .append(UL.end())
          .nl()
          .runIf(level > 1, () -> moveTo("AfterEndLI" + (level-1)));
    }

    @Override
    public void startListItem(int level) {
        indent()
          .append(LI.start());
    }

    @Override
    public void endListItem(int level) {
        mark("BeforeEndLI" + level)
          .append(LI.end())
          .nl()
          .mark("AfterEndLI" + level);
    }

    @Override
    public void startListItemValue() {
//        indent()
//          .append(P.start());
    }

    @Override
    public void endListItemValue() {
//        append(P.end())
//                .nl();
    }

    @Override
    public void writeListingBlock(Listing listing) {
        String language = listing.getLanguage();
        boolean linenums = listing.isLinenums();

        indent()
          .append(PRE.start())
          .runIf(!linenums && language != null, () ->
            append(CODE.start("class", language))
          )
          .runIf(!linenums && language == null, () ->
            append(CODE.start())
          )
          .forEach(listing.getLines(), (line, index) ->
            runIf(linenums, () ->
              append(CODE.start())
            )
            .append(line.getText())
            .writeListingCallout(line)
            .runIf(linenums, () ->
              append(CODE.end())
            )
            .runIf(listing.getLines().size() - 1 != index, () -> nl())
          )
          .runIf(!linenums, () ->
            append(CODE.end())
          )
          .append(PRE.end())
          .nl()
        ;
    }


}
