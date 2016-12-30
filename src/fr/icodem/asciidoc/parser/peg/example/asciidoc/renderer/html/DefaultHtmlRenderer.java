package fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.html;

import fr.icodem.asciidoc.backend.html.HtmlTag;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.*;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.DocumentWriter;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;
import java.util.stream.Collectors;

import static fr.icodem.asciidoc.backend.html.HtmlTag.*;

public class DefaultHtmlRenderer extends HtmlBaseRenderer {

    private DefaultHtmlRenderer(DocumentWriter writer) {
        super(writer);
    }

    public static DefaultHtmlRenderer withWriter(DocumentWriter writer) {
        return new DefaultHtmlRenderer(writer);
    }

    private String title;
    private boolean hasHeader;
    private boolean contentStarted;

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
              .append(UL.start("class", "sectlevel0"))
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
          .runIf(item.getChildren().size() > 0, () ->
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
        .runIf(item.getChildren().size() == 0, () ->
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

    @Override
    public void writeText(String text) {
        append(text);
    }


    /* **********************************************/
    // Macro
    /* **********************************************/

    @Override
    public void writeImage(ImageMacro image) {
        indent()
          .append(DIV.start("class", "imageblock"))
          .nl()
          .incIndent()
          .indent()
          .append(DIV.start("class", "content"))
          .nl()
          .incIndent()
          .indent()
          .append(IMG.tag("src", image.getTarget(), "alt", image.getAlternateText()))
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

    /* **********************************************/
    // Block
    /* **********************************************/

    @Override
    public void startDocument() {
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

        append(DOCTYPE.tag())
            .nl()
            .append(HTML.start())
            .nl()
            .append(HEAD.start())
            .nl()
            .incIndent()
            .indent()
            .append(META.tag("charset", "UTF-8"))
            .nl()
            .indent()
            .append(META.tag("name", "viewport", "content", "width=device-width, initial-scale=1.0"))
            .nl()
            .indent()
            .append(META.tag("name", "generator", "content", "iodoc"))
            .nl()
            .bufferOn()
            .mark("authors")
            .indent()
            .append(LINK.tag("rel", "stylesheet", "href", "styles.css"))
            .nl()
            .mark("title")
            .append(HEAD.end())
            .nl()
            .decIndent()
            .append(BODY.start("class", bodyClass))
            .nl()
            .incIndent();
    }

    @Override
    public void endDocument() {
        decIndent()
            .indent()
            .append(BODY.end())
            .nl()
            .decIndent()
            .append(HTML.end());
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
        moveTo("title")
          .indent()
          .append(TITLE.start())
          .append(title)
          .append(TITLE.end())
          .nl()
          .bufferOff()
          .runIf(true, () -> markOnWriter("TOC"))
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
        this.title = title;
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
              .runIf(a.getAddress() != null, () -> {
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
                  .nl();
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
          .moveEnd();
    }

    @Override
    public void startPreamble() {
        indent()
            .append(DIV.start("id", "preamble"))
            .incIndent()
            .nl();
    }

    @Override
    public void endPreamble() {
        decIndent()
            .indent()
            .append(DIV.end())
            .nl();
    }

    @Override
    public void startContent() {
        runIf(!hasHeader, () -> bufferOff())
            .runIf(!contentStarted, () ->
                indent()
                .append(DIV.start("id", "content"))
                .incIndent()
                .nl()
            );

        contentStarted = true;
    }

    @Override
    public void endContent() {
        decIndent()
            .indent()
            .append(DIV.end())
            .nl();
    }

    @Override
    public void startSection(int level) { // TODO case level = 0 should generate an error (not a book document, but article) ?
        indent()
          .append(DIV.start("class", "sect" + (level - 1)))
          .nl()
          .incIndent();
    }

    @Override
    public void endSection(int level) {
        runIf(level == 2, () ->
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
    public void writeSectionTitle(int level, String title, String ref) {
        HtmlTag titleHeader = getTitleHeader(level);
        indent()
          .append(titleHeader.start("id", ref))
          .append(title)
          .append(getTitleHeader(level).end())
          .nl()
          .runIf(level == 2, () ->
            indent()
              .append(DIV.start("class", "sectionbody"))
              .nl()
              .incIndent()
        );
    }

    @Override
    public void horizontalRule() {
        indent()
            .append(HR.tag())
            .nl();
    }

    @Override
    public void startParagraph(String admonition) {
        if (admonition == null) {
            indent()
              .append(DIV.start("class", "paragraph"))
              .nl()
              .incIndent()
              .indent()
              .append(P.start());
        } else {
            indent()
              .append(DIV.start("class", "admonitionblock " + admonition.toLowerCase()))
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
                .append(DIV.start("class", "title"))
                .append("Note")
                .append(DIV.end())
                .nl()
                .decIndent()
                .indent()
                .append(TD.end())
                .nl()
                .indent()
                .append(TD.start("class", "content"))
                .nl()
                .incIndent()
                .indent();
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

        runIf(level > 1, () -> moveTo("BeforeEndLI" + (level-1)))
            .indent()
            .append(DIV.start("class", divStyles))
            .nl()
            .incIndent()
            .indent()
            .runIf(olType == null, () -> append(OL.start("class", olStyle)))
            .runIf(olType != null, () -> append(OL.start("class", olStyle, "type", olType)))
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
            .runIf(level > 1, () -> moveTo("AfterEndLI" + (level-1)));
    }

    @Override
    public void startUnorderedList(int level, AttributeList attList) {
        runIf(level > 1, () -> moveTo("BeforeEndLI" + (level-1)))
            .indent()
            .append(DIV.start("class", "ulist"))
            .nl()
            .incIndent()
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
            .runIf(level > 1, () -> moveTo("AfterEndLI" + (level-1)));
    }

    @Override
    public void startListItem(int level) {
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
    public void startLabeledList() {
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
    public void endLabeledList() {
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
    public void startLabeledListItemTitle() {
        indent()
            .append(DT.start("class", "hdlist1"));
    }

    @Override
    public void endLabeledListItemTitle() {
        append(DT.end())
            .nl();
    }

    @Override
    public void startLabeledListItemContent() {
        indent()
            .append(DD.start())
            .nl()
            .incIndent();
    }

    @Override
    public void endLabeledListItemContent() {
        decIndent()
            .indent()
            .append(DD.end())
            .nl();
    }

    @Override
    public void startLabeledListItemSimpleContent() {
        indent()
            .append(P.start());
    }

    @Override
    public void endLabeledListItemSimpleContent() {
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
            .runIf(style.length() > 0, () -> append(COL.tag("style", style)))
            .runIf(style.length() == 0, () -> append(COL.tag()))
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

    @Override
    public void writeListingBlock(String listing, String language) {
        indent()
          .append(DIV.start("class", "listingblock"))
          .nl()
          .incIndent()
          .indent()
          .append(DIV.start("class", "content"))
          .nl()
          .incIndent()
          .indent()
          .runIf(language != null, () -> append(PRE.start("class", "highlight")))
          .runIf(language == null, () -> append(PRE.start()))
          .nl()
          .runIf(language != null, () ->
            incIndent()
              .indent()
              .append(CODE.start("class", "language-" + language, "data-lang", language))
              .nl()
          )
          .append(listing)
          .runIf(language != null, () ->
            nl()
              .decIndent()
              .indent()
              .append(CODE.end())
          )
          .nl()
          .decIndent()
          .indent()
          .append(PRE.end())
          .nl()
          .decIndent()
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
        if (attList == null || attList.getFirstPositionalAttribute() == null) {
            append(MARK.start());
        } else {
            append(SPAN.start("class", attList.getFirstPositionalAttribute()));
        }
    }

    @Override
    public void endMark(AttributeList attList) {
        if (attList == null || attList.getFirstPositionalAttribute() == null) {
            append(MARK.end());
        } else {
            append(SPAN.end());
        }
    }

}
