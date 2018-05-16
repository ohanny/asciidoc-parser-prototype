package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import java.util.List;

public interface AsciidocHandler {

    //void attributeEntries(AttributeEntries atts);
    void postProcess(Toc toc);

    //
    void writeText(String text);

    // Macro
    void writeImage(ImageMacro macro);
    void writeVideo(VideoMacro macro);

    // Block
    void startDocument();
    void endDocument();

    void startHeader();
    void endHeader();

    void startDocumentTitle();
    void writeDocumentTitle(String title);
    void endDocumentTitle();

    void writeAuthors(List<Author> authors);
//    void startAuthors();
//    void endAuthors();
//
//    void startAuthor();
//    void writeAuthorName(String name);
//    void writeAuthorAddress(String address);
//    void writeAuthorAddressLabel(String label);
//    void endAuthor();

    void startPreamble();
    void endPreamble();

    void startContent();
    void endContent();

    void startSection(int level, AttributeList attList);
    void endSection(int level);

    void writeSectionTitle(int level, String title, String ref, AttributeList attList);

    void horizontalRule();

    void startParagraph(String admonition, String icons, AttributeList attList);
    void endParagraph(String admonition);

    void startQuote(String attribution, String citationTitle);
    void endQuote(String attribution, String citationTitle);

    void startList();
    void endList();

    void startOrderedList(int level, AttributeList attList);
    void endOrderedList(int level);

    void startUnorderedList(int level, AttributeList attList);
    void endUnorderedList(int level);

    void startListItem(int level, int position, AttributeList parentAttList);
    void endListItem(int level);

    void startListItemValue();
    void endListItemValue();

    void startLabeledList();
    void endLabeledList();

    void startLabeledListItemTitle();
    void endLabeledListItemTitle();

    void startLabeledListItemContent();
    void endLabeledListItemContent();

    void startLabeledListItemSimpleContent();
    void endLabeledListItemSimpleContent();

    void startTable(AttributeList attList);
    void endTable();

    void startColumnGroup();
    void endColumnGroup();

    void column(AttributeList attList, double width);

    void startTableHeader();
    void endTableHeader();

    void startTableHeaderCell();
    void writeTableHeaderCellContent(String text);
    void endTableHeaderCell();

    void startTableBody();
    void endTableBody();

    void startTableFooter();
    void endTableFooter();

    void startTableRow();
    void endTableRow();

    void startTableCell();
    void writeTableCellContent(String text);
    void endTableCell();

    void writeListingBlock(Listing listing, AttributeList attList);

    void startCallouts();
    void endCallouts();

    void startCallout();
    void writeCalloutNumber(String nb);
    void enterCalloutText();
    void exitCalloutText();
    void endCallout();

    void startExample(String admonition, String icons, AttributeList attList);
    void endExample(String admonition);

    // Inline text
    void startBold();
    void endBold();

    void startItalic();
    void endItalic();

    void startSubscript();
    void endSubscript();

    void startSuperscript();
    void endSuperscript();

    void startMonospace();
    void endMonospace();

    void startMark(AttributeList attList);
    void endMark(AttributeList attList);

    void xref(XRef xref);
}
