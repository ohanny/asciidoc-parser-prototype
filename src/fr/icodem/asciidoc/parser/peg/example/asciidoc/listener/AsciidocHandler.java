package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

public interface AsciidocHandler {
    void startDocument();
    void endDocument();

    void startHeader();
    void endHeader();
    /*
        void startDocument();
    void endDocument();
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
