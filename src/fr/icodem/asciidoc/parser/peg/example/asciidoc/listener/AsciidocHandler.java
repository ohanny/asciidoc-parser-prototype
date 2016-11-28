package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

public interface AsciidocHandler {

    String DOCUMENT_TITLE = "DOCUMENT_TITLE";

    void writeText(String node, String text);

    // Block
    void startDocument();
    void endDocument();

    void startHeader();
    void endHeader();

    void startDocumentTitle();
    void endDocumentTitle();

    void startAuthors();
    void endAuthors();

    void startAuthor();
    void endAuthor();

    void writeAuthorName(String name);
    void writeAuthorAddress(String address);
    void writeAuthorAddressLabel(String label);

    void startPreamble();
    void endPreamble();

    void startContent();
    void endContent();

    void startSection();
    void endSection();

    void startSectionTitle(int level);
    void endSectionTitle(int level);

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


    // Formatted text
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
