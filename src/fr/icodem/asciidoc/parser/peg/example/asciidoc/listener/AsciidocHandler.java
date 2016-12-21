package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

public interface AsciidocHandler {

    void writeText(String text);

    // Block
    void startDocument();
    void endDocument();

    void startHeader();
    void endHeader();

    void startDocumentTitle();
    void writeDocumentTitle(String title);
    void endDocumentTitle();

    void startAuthors();
    void endAuthors();

    void startAuthor();
    void writeAuthorName(String name);
    void writeAuthorAddress(String address);
    void writeAuthorAddressLabel(String label);
    void endAuthor();

    void startPreamble();
    void endPreamble();

    void startContent();
    void endContent();

    void startSection();
    void endSection();

    void startSectionTitle(int level);
    void endSectionTitle(int level);

    void horizontalRule();

    void startParagraph();
    void endParagraph();

    void startList();
    void endList();

    void startOrderedList(int level, AttributeList attList);
    void endOrderedList(int level);

    void startUnorderedList(int level, AttributeList attList);
    void endUnorderedList(int level);

    void startListItem(int level);
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


    // Inline text
    void startBold();
    void endBold();

    void startItalic();
    void endItalic();


    /*
    void startPreamble();
    void endPreamble();
    void startParagraph(Paragraph paragraph);
    void startSection(Section section);
    void endSection(Section section);
    void startSectionTitle(SectionTitle sectionTitle);
    void startAttributeEntry(AttributeEntry att);
    void visitList(AbstractList list);
     */
}
