package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

public interface AsciidocHandler {

    String DOCUMENT_TITLE = "DOCUMENT_TITLE";

    void writeText(String node, String text);

    void startDocument();
    void endDocument();

    void startHeader();
    void endHeader();

    void startDocumentTitle();
    void endDocumentTitle();

    /*
    void documentHeader(DocumentHeader header);
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
