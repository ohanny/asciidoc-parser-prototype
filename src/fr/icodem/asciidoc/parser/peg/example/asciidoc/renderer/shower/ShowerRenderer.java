package fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.shower;

import fr.icodem.asciidoc.backend.html.HtmlTag;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.*;
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

    /* **********************************************/
    // Block
    /* **********************************************/

    @Override
    protected String getBodyClass() {
        return "shower list";
    }

    @Override
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
          .append(LINK.tag("rel", "stylesheet", "href", "iodoc/style.css"))
          .nl()
          .indent()
          .append(STYLE.start())
          .nl()
          .incIndent()
          .indent()
          .append(".slide pre.nolinenums code:not(:only-child)::before {content: none;}")
          .nl()
          .decIndent()
          .indent()
          .append(STYLE.end())
          .nl()
        ;
        return this;
    }

    protected String getHighlightjsSelector() {
        return "pre.highlight";
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
    public void writeSectionTitle(int level, String title, String ref, AttributeList attList) {
        if (attList != null && attList.hasOption("conceal")) return;

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

    /*
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
    }*/

    @Override
    public void startListItem(int level, int position, AttributeList parentAttList) {
        boolean addNextClass = parentAttList != null
                                    && parentAttList.hasOption("step")
                                    && level == 1 && position > 1;

        indent()
          .runIf(addNextClass, () -> append(LI.start("class", "next")))
          .runIf(!addNextClass, () -> append(LI.start()));
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
    protected String getListingPreClass(Listing listing) {
        String preClass = super.getListingPreClass(listing);
        if (preClass == null) preClass = "listingblock";
        else preClass += " listingblock";

        if (!listing.isLinenums()) {
            if (preClass == null) preClass = "nolinenums";
            else preClass += " nolinenums";
        }
        return preClass;
    }

    private String getListingCodeClass(String language, Listing.Line line) {
        String codeClass = null;

        if (language != null) {
            codeClass = language;
        }

        if (line.getLineChunks().get(0).isHighlight()) {
            if (codeClass == null) codeClass = "mark";
            else codeClass += " mark";
        }

        return codeClass;
    }

    @Override
    public void writeListingBlock(Listing listing, AttributeList attList) {
        indent()
          .append(DIV.start("class", getMoreClasses("listingblock", attList), "style", styleBuilder().reset(attList).addPosition().style()))
          .nl()
          .incIndent()
          .indent();

        runIf(listing.getTitle() != null, () ->
          append(DIV.start("class", "title listingblock"))
            .append(listing.getTitle())
            .append(DIV.end())
            .nl()
        )
          .append(PRE.start("class", getMoreClasses(getListingPreClass(listing), attList)))
          .forEach(listing.getLines(), (line, index) ->
            append(CODE.start("class", getListingCodeClass(listing.getLanguage(), line)))
              .forEach(line.getLineChunks(), this::writeListingLineChunk)
              .writeListingCallout(line)
              .append(CODE.end())
              .runIf(listing.getLines().size() - 1 != index, () -> nl())
          )
          .append(PRE.end())
          .nl()
        ;
    }

    private void writeListingLineChunk(Listing.LineChunk chunk) {
        runIf(chunk.isMark() && chunk.getMarkLevel() == 0, () ->
                append(MARK.start())
                        .writeTextOrChunks(chunk)
                        .append(MARK.end())
        ).
        runIf(chunk.isMark() && chunk.getMarkLevel() > 0, () ->
                append(MARK.start("class", "mark" + chunk.getMarkLevel()))
                        .writeTextOrChunks(chunk)
                        .append(MARK.end())
        ).
        runIf(chunk.isStrong(), () ->
                append(STRONG.start())
                        .writeTextOrChunks(chunk)
                        .append(STRONG.end())
        )
        .runIf(chunk.isImportant(), () ->
                append(MARK.start("class", "important"))
                        .writeTextOrChunks(chunk)
                        .append(MARK.end())
        )
        .runIf(chunk.isComment(), () ->
                append(SPAN.start("class", "comment"))
                        .writeTextOrChunks(chunk)
                        .append(SPAN.end())
        )
        .runIf(chunk.isNotMarked(), () ->
                writeTextOrChunks(chunk)
        );
    }

    private ShowerRenderer writeTextOrChunks(Listing.LineChunk chunk) {
        if (chunk.getChunks() == null) {
            append(chunk.getText());
        } else {
            chunk.getChunks()
                 .stream()
                 .forEach(this::writeListingLineChunk);
        }
        return this;
    }

    @Override
    public void endListingBlock() {
        decIndent()
                .indent()
                .append(DIV.end())
                .nl();
    }


    /* **********************************************/
    // Macro
    /* **********************************************/

    @Override
    public void writeImage(ImageMacro image) {
        indent()
                .append(FIGURE.start("class", getCssClasses(image.getBlockAttributes()),
                        "style", styleBuilder().reset(image.getBlockAttributes()).addPosition().style()))
                .nl()
                .incIndent()
                .indent()
                .append(IMG.tag("src", image.getTarget(), "alt", image.getAlternateText(),
                        "style", styleBuilder().reset(image.getBlockAttributes()).addSize().style()))
                .nl()
                .runIf(image.getTitle() != null, () ->
                        indent()
                        .append(FIGCAPTION.start())
                        .append(image.getTitle())
                        .append(FIGCAPTION.end())
                        .nl()
                )
                .decIndent()
                .indent()
                .append(FIGURE.end())
                .nl()
        ;
    }


}
