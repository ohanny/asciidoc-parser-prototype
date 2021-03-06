package fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.html;

import fr.icodem.asciidoc.backend.html.HtmlTag;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.*;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.DocumentWriter;

import java.util.*;
import java.util.stream.Collectors;

import static fr.icodem.asciidoc.backend.html.HtmlTag.*;

public class DefaultHtmlRenderer<DHR extends DefaultHtmlRenderer<DHR>> extends HtmlBaseRenderer<DHR> {

    protected DefaultHtmlRenderer(DocumentWriter writer) {
        super(writer);
    }

    public static DefaultHtmlRenderer withWriter(DocumentWriter writer) {
        return new DefaultHtmlRenderer(writer);
    }

    protected String title;
    protected boolean hasHeader;
    protected boolean contentStarted;

    /* **********************************************/
    // Attributes methods
    /* **********************************************/

    protected String getMoreClasses(String baseClass, AttributeList attList) {
        String moreClasses = getCssClasses(attList);
        if (baseClass == null) return moreClasses;
        return baseClass + (moreClasses == null?"":" " + moreClasses);
    }

    protected String getCssClasses(AttributeList attList) {
        String cssClasses = null;
        if (attList != null && attList.getRoles() != null && !attList.getRoles().isEmpty()) {
            cssClasses = attList.getRoles()
                                 .stream()
                                 .collect(Collectors.joining(" "));
        }

        return cssClasses;
    }

    /* **********************************************/
    // Post-processing
    /* **********************************************/

    @Override
    public void postProcess(Toc toc) {
        if (toc != null) {

            AttributeEntry tocAtt = getAttributeEntry("toc");

            String tocClass = "toc";
            if ("right".equals(tocAtt.getValue()) || "left".equals(tocAtt.getValue())) {
                tocClass = "toc2";
            }

            seekOnWriter("TOC")
              .indent()
              .append(DIV.start("id", "toc", "class", tocClass))
              .nl()
              .incIndent()
              .indent()
              .append(DIV.start("id", "toctitle"))
              .append(toc.getRoot().getTitle())
              .append(DIV.end())
              .nl()
              .indent()
              .append(UL.start("class", "sectlevel" + toc.getRoot().getLevel()))
              .nl()
              .incIndent()
              .forEach(toc.getRoot().getChildren(), this::processTocItem)
              .decIndent()
              .indent()
              .append(UL.end())
              .nl()
              .decIndent()
              .indent()
              .append(DIV.end())
              .nl()
              .endInsertOnWriter()
            ;

        }
    }

    private void processTocItem(TocItem item) {
        indent()
          .append(LI.start())
          .appendIf(item.getChildren().size() > 0, () ->
              nl()
                .incIndent()
                .indent()
                .append(A.start("href", "#" + item.getRef()))
                .append(item.getTitle())
                .append(A.end())
                .nl()
                .indent()
                .append(UL.start("class", "sectlevel" + item.getLevel()))
                .nl()
                .incIndent()
                .forEach(item.getChildren(), this::processTocItem)
                .decIndent()
                .indent()
                .append(UL.end())
                .nl()
                .decIndent()
                .indent()
                .append(LI.end())
                .nl()
          )
        .appendIf(item.getChildren().size() == 0, () ->
            append(A.start("href", "#" + item.getRef()))
            .append(item.getTitle())
            .append(A.end())
            .append(LI.end())
            .nl()
        )
      ;
    }

    /* **********************************************/
    // Text
    /* **********************************************/
    private String replaceSpecialCharacters(String text) { // TODO temporaire
        text = text.replaceAll("&", "&amp;");
        text = text.replaceAll("<", "&lt;");
        text = text.replaceAll(">", "&gt;");
        return text;
    }

    @Override
    public void writeText(String text) {
        text = replaceSpecialCharacters(text);
        append(text);
    }


    /* **********************************************/
    // Macro
    /* **********************************************/

    @Override
    public void writeImage(ImageMacro image) {
        indent()
          .append(DIV.start("class", getMoreClasses("imageblock", image.getBlockAttributes()),
                            "style", styleBuilder().reset(image.getBlockAttributes()).addPosition().style()))
          .nl()
          .incIndent()
          .indent()
            .append(DIV.start("class", "content"))
            .nl()
            .incIndent()
            .indent()
              .append(IMG.tag("src", image.getTarget(), "alt", image.getAlternateText(),
                          "style", styleBuilder().reset(image.getBlockAttributes()).addSize().style()))
              .nl()
            .decIndent()
            .indent()
            .append(DIV.end())
            .nl()
            .appendIf(image.getTitle() != null, () ->
              writeBlockTitle(image.getTitle())
            )
          .decIndent()
          .indent()
          .append(DIV.end())
          .nl()
        ;
    }

    @Override
    public void writeVideo(VideoMacro video) {
        indent()
          .append(DIV.start("class", getMoreClasses("videoblock", video.getBlockAttributes()),
                            "style", styleBuilder().reset(video.getBlockAttributes()).addPosition().style()))
          .nl()
          .incIndent()
          .indent()
          .append(DIV.start("class", "content"))
          .nl()
          .incIndent()
          .indent()
          .append(VIDEO.start("src", video.getTarget(), "controls", "true", "style", styleBuilder().reset(video.getBlockAttributes()).addSize().style()))
          //.append(VIDEO.start("src", video.getTarget(), "controls", "true", "width", "400"))
          .nl()
          .indent()
          .append("Your browser does not support the video tag.")
          .nl()
          .indent()
          .append(VIDEO.end())
          .decIndent()
          .indent()
          .append(DIV.end())
          .nl()
          .decIndent()
          .indent()
          .append(DIV.end())
          .nl()
        ;
    }



    /* **********************************************/
    // Block
    /* **********************************************/

    @Override
    public void startDocument() {
        append(DOCTYPE.tag())
          .nl()
          .append(HTML.start())
          .nl()
          .append(HEAD.start())
          .nl()
          .incIndent()
          .indent()
          .append(META.tag("charset", "utf-8"))
          .nl()
          .indent()
          .append(META.tag("name", "viewport", "content", "width=device-width, initial-scale=1.0"))
          .nl()
          .indent()
          .append(META.tag("name", "generator", "content", "iodoc"))
          .nl()
          .includeCustomMeta()
          .bufferOn()
          .mark("authors")
          .includeStylesheets()
          .mark("title")
          .append(HEAD.end())
          .nl()
          .decIndent()
          .append(BODY.start("class", getBodyClass()))
          .nl()
          .incIndent()
        ;
    }

    protected String getBodyClass() {
        String bodyClass = getAttributeEntry("doctype").getValue();

        AttributeEntry tocAtt = getAttributeEntry("toc");
        if (!tocAtt.isDisabled()) {
            if ("right".equals(tocAtt.getValue())) {
                bodyClass += " toc2 toc-right";
            }
            else if ("left".equals(tocAtt.getValue())) {
                bodyClass += " toc2 toc-left";
            }
        }

        return bodyClass;
    }

    protected DHR includeCustomMeta() {
        return (DHR)this;
    }

    protected DHR includeStylesheets() {
        AttributeEntry iconsAtt = getAttributeEntry("icons");
        indent()
          .append(LINK.tag("rel", "stylesheet", "href", "article/style.css"))
          .nl()
          .indent()
          .append(LINK.tag("rel", "stylesheet", "href", "https://fonts.googleapis.com/css?family=Open+Sans:300,300italic,400,400italic,600,600italic%7CNoto+Serif:400,400italic,700,700italic%7CDroid+Sans+Mono:400,700"))
          .nl()
          .appendIf("font".equals(iconsAtt.getValue()), () ->
            indent()
              .append(LINK.tag("rel", "stylesheet", "href", "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css"))
              .nl()
          )
        ;

        return (DHR)this;
    }

    protected String getHighlightjsSelector() {
        return "pre.highlight code";
    }

    @Override
    public void endDocument() {
        boolean highlightjs = isAttributeValueEqualTo("source-highlighter", "highlightjs");
        boolean highlightSelective = isAttributeEnabled("highlight-selective");

        appendIf(highlightjs, () ->
          indent()
            .append(LINK.tag("rel", "stylesheet", "href", getAttributeEntryValue("highligthjs-style")))
            .nl()
            .indent()
            .append(SCRIPT.start("src", getAttributeEntryValue("highligthjs-script")))
            .append(SCRIPT.end())
            .nl()
            .appendIf(!highlightSelective, () ->
              indent()
                .append(SCRIPT.start())
                .append("hljs.initHighlightingOnLoad();")
                .append(SCRIPT.end())
                .nl()
              )
            .appendIf(highlightSelective, () ->
              indent()
                .append(SCRIPT.start())
                .nl()
                .indent()
                .append("let blocks = document.querySelectorAll('" + getHighlightjsSelector() + "');")
                .nl()
                .indent()
                .append("for (let i = 0; i < blocks.length; ++i) {")
                .nl()
                .incIndent()
                .indent()
                .append("hljs.highlightBlock(blocks[i]);")
                .nl()
                .decIndent()
                .indent()
                .append("}")
                .nl()
            )
            .indent()
            .append(SCRIPT.end())
            .nl()
        )
          .decIndent()
          .indent()
          .append(BODY.end())
          .nl()
          .decIndent()
          .append(HTML.end())
        ;
    }

    @Override
    public void startHeader() {
        hasHeader = true;

        indent()
            .append(DIV.start("id", "header"))
            .nl()
            .incIndent();
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
          .appendIf(!tocAtt.isDisabled(), () -> markOnWriter("TOC"))
          .decIndent()
          .indent()
          .append(DIV.end())
          .nl()
        ;

        startContent();
    }

    @Override
    public void startDocumentTitle() {
        indent()
            .append(H1.start());
    }

    @Override
    public void writeDocumentTitle(String title) {
        this.title = replaceSpecialCharacters(title);
        append(title);
    }

    @Override
    public void endDocumentTitle() {
        append(H1.end()).nl();
    }

    @Override
    public void writeAuthors(List<Author> authors) {
        String authorsStr = authors
                        .stream()
                        .map(a -> a.getName())
                        .collect(Collectors.joining(", "));

        indent()
          .append(DIV.start("class", "details"))
          .nl()
          .incIndent()
          .forEach(authors, a -> {
            String index = a.getPosition() == 1?"":"" + a.getPosition();

            indent()
              .append(SPAN.start("id", "author" + index, "class", "author"))
              .append(a.getName())
              .append(SPAN.end())
              .append(BR.tag())
              .nl()
              .appendIf(a.getAddress() != null, () -> {
                String href = a.getAddress();
                if (!(href.startsWith("http://") || href.startsWith("https://"))) {
                  href = "mailto:" + href;
                }

                String label = a.getAddressLabel() != null ? a.getAddressLabel() : a.getAddress();

                indent()
                  .append(SPAN.start("id", "email" + index, "class", "email"))
                  .append(A.start("href", href))
                  .append(label)
                  .append(A.end())
                  .append(SPAN.end())
                  .append(BR.tag())
                  .nl()
                ;
                });
              })
          .decIndent()
          .indent()
          .append(DIV.end())
          .nl()
          .moveTo("authors")
          .indent()
          .append(META.tag("name", "author", "content", authorsStr))
          .nl()
          .moveEnd()
        ;
    }

    @Override
    public void startPreamble() {
        indent()
          .append(DIV.start("id", "preamble"))
          .incIndent()
          .nl()
        ;
    }

    @Override
    public void endPreamble() {
        decIndent()
          .indent()
          .append(DIV.end())
          .nl()
        ;
    }

    @Override
    public void startContent() {
        appendIf(!hasHeader, () -> bufferOff())
          .appendIf(!contentStarted, () ->
            indent()
              .append(DIV.start("id", "content"))
              .incIndent()
              .nl()
          )
        ;

        contentStarted = true;
    }

    @Override
    public void endContent() {
        decIndent()
          .indent()
          .append(DIV.end())
          .nl()
        ;
    }

    @Override
    public void startSection(int level, AttributeList attList) { // TODO case level = 0 should generate an error (not a book document, but article) ?
        indent()
          .append(DIV.start("class", getMoreClasses("sect" + (level - 1), attList)))
          .nl()
          .incIndent();
    }

    @Override
    public void writeSectionTitle(int level, String title, String ref, AttributeList attList) {
        HtmlTag titleHeader = getTitleHeader(level);
        indent()
                .append(titleHeader.start("id", ref))
                .append(replaceSpecialCharacters(title))
                .append(titleHeader.end())
                .nl()
                .appendIf(level == 2, () ->
                        indent()
                                .append(DIV.start("class", "sectionbody"))
                                .nl()
                                .incIndent()
                );
    }

    @Override
    public void endSection(int level) {
        appendIf(level == 2, () ->
          decIndent()
            .indent()
            .append(DIV.end())
            .nl()
        )
        .decIndent()
            .indent()
            .append(DIV.end())
            .nl();
    }

    @Override
    public void horizontalRule() {
        indent()
            .append(HR.tag())
            .nl();
    }

    private static Properties ADMONITIONS = new Properties();
    {
        ADMONITIONS.setProperty("note", "Note");
        ADMONITIONS.setProperty("tip", "Tip");
        ADMONITIONS.setProperty("important", "Important");
        ADMONITIONS.setProperty("caution", "Caution");
        ADMONITIONS.setProperty("warning", "Warning");
    }

    @Override
    public void startParagraph(String admonition, String icons, AttributeList attList) {
        if (admonition == null) {
            indent()
              .append(DIV.start("class", getMoreClasses("paragraph", attList), "style", styleBuilder().reset(attList).addPosition().addSize().style()))
              .nl()
              .incIndent()
              .indent()
              .append(P.start())
            ;
        } else {
            startAdmonition(admonition, icons, attList);
//            indent()
//              .append(DIV.start("class", getMoreClasses("admonitionblock " + admonition, attList), "style", styleBuilder().reset(attList).addPosition().addSize().style()))
//                .nl()
//                .incIndent()
//                .indent()
//                .append(TABLE.start())
//                .nl()
//                .incIndent()
//                .indent()
//                .append(TR.start())
//                .nl()
//                .incIndent()
//                .indent()
//                .append(TD.start("class", "icon"))
//                .nl()
//                .incIndent()
//                .indent()
//                .appendIf("font".equals(icons), () ->
//                    append(I.start("class", "fa icon-" + admonition, "title", ADMONITIONS.getProperty(admonition)))
//                      .append(I.end())
//                )
//                .appendIf(!"font".equals(icons), () ->
//                    append(DIV.start("class", "title"))
//                      .append(ADMONITIONS.getProperty(admonition))
//                      .append(DIV.end())
//                )
//                .nl()
//                .decIndent()
//                .indent()
//                .append(TD.end())
//                .nl()
//                .indent()
//                .append(TD.start("class", "content"))
//                .nl()
//                .incIndent()
//                .indent()
//            ;
        }
    }

    @Override
    public void endParagraph(String admonition) {
        if (admonition == null) {
            append(P.end())
              .nl()
              .decIndent()
              .indent()
              .append(DIV.end())
              .nl();
        } else {
            endAdmonition();
//            nl()
//              .decIndent()
//              .indent()
//              .append(TD.end())
//              .nl()
//              .decIndent()
//              .indent()
//              .append(TR.end())
//              .nl()
//              .decIndent()
//              .indent()
//              .append(TABLE.end())
//              .nl()
//              .decIndent()
//              .indent()
//              .append(DIV.end())
//              .nl();
        }
    }

    private void startAdmonition(String admonition, String icons, AttributeList attList) {
        indent()
                .append(DIV.start("class", getMoreClasses("admonitionblock " + admonition, attList), "style", styleBuilder().reset(attList).addPosition().addSize().style()))
                .nl()
                .incIndent()
                .indent()
                .append(TABLE.start())
                .nl()
                .incIndent()
                .indent()
                .append(TR.start())
                .nl()
                .incIndent()
                .indent()
                .append(TD.start("class", "icon"))
                .nl()
                .incIndent()
                .indent()
                .appendIf("font".equals(icons), () ->
                        append(I.start("class", "fa icon-" + admonition, "title", ADMONITIONS.getProperty(admonition)))
                                .append(I.end())
                )
                .appendIf(!"font".equals(icons), () ->
                        append(DIV.start("class", "title"))
                                .append(ADMONITIONS.getProperty(admonition))
                                .append(DIV.end())
                )
                .nl()
                .decIndent()
                .indent()
                .append(TD.end())
                .nl()
                .indent()
                .append(TD.start("class", "content"))
                .nl()
                .incIndent()
                .indent()
        ;
    }

    private void endAdmonition() {
        nl()
                .decIndent()
                .indent()
                .append(TD.end())
                .nl()
                .decIndent()
                .indent()
                .append(TR.end())
                .nl()
                .decIndent()
                .indent()
                .append(TABLE.end())
                .nl()
                .decIndent()
                .indent()
                .append(DIV.end())
                .nl();
    }

    @Override
    public void startQuote(String attribution, String citationTitle) {
        indent()
          .append(DIV.start("class", "quote"))
          .nl()
          .incIndent()
          .indent()
          .append(BLOCKQUOTE.start())
        ;
    }

    @Override
    public void endQuote(String attribution, String citationTitle) {
        append(BLOCKQUOTE.end())
          .nl()
          .indent()
          .append(DIV.start("class", "attribution"))
          .nl()
          .incIndent()
          .indent()
          .append("&#8212; ")
          .append(attribution)
          .append(BR.tag())
          .nl()
          .appendIf(citationTitle != null, () ->
            indent()
              .append(CITE.start())
              .append(replaceSpecialCharacters(citationTitle))
              .append(CITE.end())
              .nl()
          )
          .decIndent()
          .indent()
          .append(DIV.end())
          .nl()
          .decIndent()
          .indent()
          .append(DIV.end())
          .nl()
        ;
    }

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
        CssElement css = null;
        if (atts != null) {
            css = CssElement.getOrderedListNumerationStyle(atts.getFirstPositionalAttribute());
        }
        if (css == null) {
            css = CssElement.getOrderedListNumerationStyle(level);
        }

        String divStyles = "olist " + css.getOrderedListNumerationStyleName();
        String olStyle = css.getOrderedListNumerationStyleName();
        String olType = css.getOrderedListNumerationType();

        appendIf(level > 1, () -> moveTo("BeforeEndLI" + (level-1)))
            .indent()
            .append(DIV.start("class", divStyles))
            .nl()
            .incIndent()
            .indent()
            .append(OL.start("class", olStyle, "type", olType, "style", styleBuilder().reset(atts).addPosition().addSize().style()))
            .nl()
            .incIndent();
    }

    @Override
    public void endOrderedList(int level) {
        decIndent()
            .indent()
            .append(OL.end())
            .nl()
            .decIndent()
            .indent()
            .append(DIV.end())
            .nl()
            .appendIf(level > 1, () -> moveTo("AfterEndLI" + (level-1)));
    }

    @Override
    public void startUnorderedList(int level, AttributeList attList, FormattedText title) {
        appendIf(level > 1, () -> moveTo("BeforeEndLI" + (level-1)))
            .indent()
            .append(DIV.start("class", "ulist", "style", styleBuilder().reset(attList).addPosition().addSize().style()))
            .nl()
            .incIndent()
            .appendIf(level == 1 && title != null, () ->
              writeBlockTitle(title)
            )
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
            .decIndent()
            .indent()
            .append(DIV.end())
            .nl()
            .appendIf(level > 1, () -> moveTo("AfterEndLI" + (level-1)));
    }

    @Override
    public void startListItem(int level, int position, AttributeList parentAttList) {
        indent()
            .append(LI.start())
            .nl()
            .incIndent();
    }

    @Override
    public void endListItem(int level) {
        mark("BeforeEndLI" + level)
            .decIndent()
            .indent()
            .append(LI.end())
            .nl()
            .mark("AfterEndLI" + level);
    }

    @Override
    public void startListItemValue() {
        indent()
            .append(P.start());
    }

    @Override
    public void endListItemValue() {
        append(P.end())
            .nl();
    }

    @Override
    public void startDescriptionList() {
        indent()
            .append(DIV.start("class", "dlist"))
            .nl()
            .incIndent()
            .indent()
            .append(DL.start())
            .nl()
            .incIndent();
    }

    @Override
    public void endDescriptionList() {
        decIndent()
            .indent()
            .append(DL.end())
            .nl()
            .decIndent()
            .indent()
            .append(DIV.end())
            .nl();
    }

    @Override
    public void startDescriptionListItemTitle() {
        indent()
            .append(DT.start("class", "hdlist1"));
    }

    @Override
    public void endDescriptionListItemTitle() {
        append(DT.end())
            .nl();
    }

    @Override
    public void startDescriptionListItemContent() {
        indent()
            .append(DD.start())
            .nl()
            .incIndent();
    }

    @Override
    public void endDescriptionListItemContent() {
        decIndent()
            .indent()
            .append(DD.end())
            .nl();
    }

    @Override
    public void startDescriptionListItemSimpleContent() {
        indent()
            .append(P.start());
    }

    @Override
    public void endDescriptionListItemSimpleContent() {
        append(P.end())
            .nl();
    }

    @Override
    public void startTable(AttributeList attList) {
        String cssClass = "tableblock frame-all grid-all";
        if (attList == null || !attList.hasOption("autowidth")) {
            cssClass += " spread";
        }

        indent()
            .append(TABLE.start("class", cssClass))
            .nl()
            .incIndent();
    }

    @Override
    public void endTable() {
        decIndent()
            .indent()
            .append(TABLE.end())
            .nl();
    }

    @Override
    public void startColumnGroup() {
        indent()
            .append(COLGROUP.start())
            .nl()
            .incIndent();
    }

    @Override
    public void endColumnGroup() {
        decIndent()
            .indent()
            .append(COLGROUP.end())
            .nl();
    }

    @Override
    public void column(AttributeList attList, double width) {
        StringBuilder sb = new StringBuilder();
        if (attList == null || !attList.hasOption("autowidth")) {
            sb.append("width: " + width + "%;");
        }
        String style = sb.toString();

        indent()
            .appendIf(style.length() > 0, () -> append(COL.tag("style", style)))
            .appendIf(style.length() == 0, () -> append(COL.tag()))
            .nl();
    }

    @Override
    public void startTableBody() {
        indent()
            .append(TBODY.start())
            .nl()
            .incIndent();
    }

    @Override
    public void endTableBody() {
        decIndent()
            .indent()
            .append(TBODY.end())
            .nl();
    }

    @Override
    public void startTableHeader() {
        indent()
            .append(THEAD.start())
            .nl()
            .incIndent();
    }

    @Override
    public void endTableHeader() {
        decIndent()
            .indent()
            .append(THEAD.end())
            .nl();
    }

    @Override
    public void startTableHeaderCell() {
        indent()
            .append(TH.start("class", "tableblock halign-left valign-top"));
    }

    @Override
    public void writeTableHeaderCellContent(String text) {
        append(text);
    }

    @Override
    public void endTableHeaderCell() {
        append(TH.end())
                .nl();
    }

    @Override
    public void startTableFooter() {
        indent()
            .append(TFOOT.start())
            .nl()
            .incIndent();
    }

    @Override
    public void endTableFooter() {
        decIndent()
            .indent()
            .append(TFOOT.end())
            .nl();
    }

    @Override
    public void startTableRow() {
        indent()
            .append(TR.start())
            .nl()
            .incIndent();
    }

    @Override
    public void endTableRow() {
        decIndent().
            indent()
            .append(TR.end())
            .nl();
    }

    @Override
    public void startTableCell() {
        indent()
            .append(TD.start("class", "tableblock halign-left valign-top"));
//        indent()
//            .append(TD.start())
//            .nl()
//            .incIndent();
    }

    @Override
    public void writeTableCellContent(String text) {
        append(P.start("class", "tableblock"))
            .append(text)
            .append(P.end());
    }

    @Override
    public void endTableCell() {
        append(TD.end())
            .nl();
//        decIndent()
//            .indent()
//            .append(TD.end())
//            .nl();
    }

    protected String getListingPreClass(Listing listing) {
        boolean highlightjs = isAttributeValueEqualTo("source-highlighter", "highlightjs");
        boolean highlightSelective = isAttributeEnabled("highlight-selective");

        String preClass = null;
        if (!highlightSelective || listing.isHighlight()) {
            if (highlightjs && listing.isSource()) {
                preClass = "highlightjs highlight";
            } else if (listing.isSource()) {
                preClass = "highlight";
            }
        }

        return preClass;
    }

    @Override
    public void writeListingBlock(Listing listing, AttributeList attList) {
        String language = listing.getLanguage();

        String preClass = getListingPreClass(listing);

        indent()
          .append(DIV.start("class", getMoreClasses("listingblock", attList)))
          .nl()
          .incIndent()
          .appendIf(listing.getTitle() != null, () ->
              writeBlockTitle(listing.getTitle())
          )
          .indent()
          .append(DIV.start("class", "content"))
          .nl()
          .incIndent()
          .indent()
          .append(PRE.start("class", preClass))
          //.appendIf(preClass != null, () -> append(PRE.start("class", preClass)))
          //.appendIf(preClass == null, () -> append(PRE.start()))
          .appendIf(language != null, () ->
            append(CODE.start("class", "language-" + language, "data-lang", language))
          )
          .forEach(listing.getLines(), (line, index) ->
             append(line.getText())
                 .writeListingCallout(line)
               .appendIf(listing.getLines().size() - 1 != index, () -> nl())
          )
          .appendIf(language != null, () ->
            append(CODE.end())
          )
          .append(PRE.end())
          .nl()
          .decIndent()
          .indent()
          .append(DIV.end())
          .nl()
          .decIndent()
          .indent()
          .append(DIV.end())
          .nl()
        ;
    }

    @Override
    public void endListingBlock() {}

    protected DHR writeListingCallout(Listing.Line line) {
        if (line.getCallouts() == null) return (DHR)this;
        return
          forEach(line.getCallouts(), c ->
            append(I.start("class", "conum", "data-value", Integer.toString(c.getNum())))
              .append(I.end())
              .append(B.start())
              .append("(")
              .append(Integer.toString(c.getNum()))
              .append(")")
              .append(B.end())
              .append(" ")
           )
         ;
    }

    @Override
    public void startCallouts() {
        indent()
          .append(DIV.start("class", "colist arabic"))
          .nl()
          .incIndent()
          .indent()
          .append(TABLE.start())
          .nl()
          .incIndent()
        ;
    }

    @Override
    public void startCallout() {
        indent()
          .append(TR.start())
          .nl()
          .incIndent()
        ;
    }

    @Override
    public void writeCalloutNumber(String nb) {
        indent()
          .append(TD.start())
          .append(I.start("class", "conum", "data-value", nb))
          .append(I.end())
          .append(B.start())
          .append(nb)
          .append(B.end())
          .append(TD.end())
          .nl()
        ;
    }

    @Override
    public void enterCalloutText() {
        indent().append(TD.start());
    }

    @Override
    public void exitCalloutText() {
        append(TD.end()).nl();
    }

    @Override
    public void endCallout() {
        decIndent()
          .indent()
          .append(TR.end())
          .nl()
        ;
    }

    @Override
    public void endCallouts() {
        decIndent()
          .indent()
          .append(TABLE.end())
          .nl()
          .decIndent()
          .indent()
          .append(DIV.end())
          .nl()
        ;
    }

    @Override
    public void startExample(String admonition, String icons, AttributeList attList) {
        if (admonition == null) {
            indent()
                    .append(DIV.start("class", "exampleblock"))
                    .nl()
                    .incIndent()
                    .indent()
                    .append(DIV.start("class", "content"))
                    .nl()
                    .incIndent()
            ;
        } else {
            startAdmonition(admonition, icons, attList);
        }

    }

    @Override
    public void endExample(String admonition) {
        if (admonition == null) {
            decIndent()
                    .indent()
                    .append(DIV.end())
                    .nl()
                    .decIndent()
                    .indent()
                    .append(DIV.end())
                    .nl();
        } else {
            endAdmonition();
        }

    }

    @Override
    public void startSidebar(FormattedText title, AttributeList attList) {
        indent()
          .append(DIV.start("class", getMoreClasses("sidebarblock", attList), "style", styleBuilder().reset(attList).addPosition().addSize().style()))
          .nl()
          .incIndent()
          .indent()
          .append(DIV.start("class", "content"))
          .incIndent()
          .nl()
          .appendIf(title != null, () -> writeBlockTitle(title));
    }

    @Override
    public void endSidebar() {
        decIndent()
          .indent()
          .append(DIV.end())
          .nl()
          .decIndent()
          .indent()
          .append(DIV.end())
          .nl();
    }

    /* **********************************************/
    // Inline text
    /* **********************************************/

    @Override
    public void startBold() {
        append(STRONG.start());
    }

    @Override
    public void endBold() {
        append(STRONG.end());
    }

    @Override
    public void startItalic() {
        append(EM.start());
    }

    @Override
    public void endItalic() {
        append(EM.end());
    }

    @Override
    public void startSubscript() {
        append(SUB.start());
    }

    @Override
    public void endSubscript() {
        append(SUB.end());
    }

    @Override
    public void startSuperscript() {
        append(SUP.start());
    }

    @Override
    public void endSuperscript() {
        append(SUP.end());
    }

    @Override
    public void startMonospace() {
        append(CODE.start());
    }

    @Override
    public void endMonospace() {
        append(CODE.end());
    }

    @Override
    public void startMark(AttributeList attList) {
        if (attList == null || (attList.getFirstPositionalAttribute() == null && attList.getRoles().isEmpty())) {
            append(MARK.start());
        } else {
            String classAtt = attList.getFirstPositionalAttribute();
            if (classAtt == null) {
                classAtt = attList.getRoles()
                                  .stream()
                                  .collect(Collectors.joining(" "));
            }

            append(SPAN.start("class", classAtt));
        }
    }

    @Override
    public void endMark(AttributeList attList) {
        if (attList == null || (attList.getFirstPositionalAttribute() == null  && attList.getRoles().isEmpty())) {
            append(MARK.end());
        } else {
            append(SPAN.end());
        }
    }

    @Override
    public void xref(XRef xref) {
        String href = xref.isInternal()?"#" + xref.getValue():xref.getValue();

        append(A.start("href", href))
          .append(xref.getLabel())
          .append(A.end());
    }

    protected void writeBlockTitle(FormattedText title) {
        indent()
          .append(DIV.start("class", "title"))
          .append(() -> writeFormattedText(title))
          .append(DIV.end())
          .nl();
    }

    protected void writeFormattedText(FormattedText text) {
        writeChunk(text.getRoot());
    }

    private void writeChunk(FormattedText.Chunk chunk) {
        applyChunkFormat(chunk);
    }

    private void writeTypedChunk(FormattedText.Chunk chunk) {
        if (chunk instanceof FormattedText.TextChunk) {
            writeTextChunk((FormattedText.TextChunk) chunk);
        }
        else if (chunk instanceof FormattedText.XRefChunk) {
            writeXRefChunk((FormattedText.XRefChunk) chunk);
        }
        else if (chunk instanceof FormattedText.CompositeChunk) {
            writeCompositeChunk((FormattedText.CompositeChunk) chunk);
        }
    }

    private void writeTextChunk(FormattedText.TextChunk chunk) {
        append(replaceSpecialCharacters(chunk.getText()));
    }

    private void writeXRefChunk(FormattedText.XRefChunk chunk) {
        xref(chunk.getXref());
    }

    private void writeCompositeChunk(FormattedText.CompositeChunk chunk) {
        chunk.getChunks()
             .forEach(this::writeChunk);
    }

    private void applyChunkFormat(FormattedText.Chunk chunk) {
        switch (chunk.getType()) {
            case Normal:
                writeTypedChunk(chunk);
                break;
            case Bold:
                startBold();
                writeTypedChunk(chunk);
                endBold();
                break;
            case Italic:
                startItalic();
                writeTypedChunk(chunk);
                endItalic();
                break;
            case Monospace:
                startMonospace();
                writeTypedChunk(chunk);
                endMonospace();
                break;
            case Subscript:
                startSubscript();
                writeTypedChunk(chunk);
                endSubscript();
                break;
            case Superscript:
                startSuperscript();
                writeTypedChunk(chunk);
                endSuperscript();
                break;
            case Mark:
                startMark(chunk.getMark().getAttributeList());
                writeTypedChunk(chunk);
                endMark(chunk.getMark().getAttributeList());
                break;
        }
    }

}
