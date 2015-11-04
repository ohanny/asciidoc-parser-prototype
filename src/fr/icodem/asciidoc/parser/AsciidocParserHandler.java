package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.elements.*;

import java.util.List;

public interface AsciidocParserHandler {
    /**
     * The document includes header info if header is present in the document
     * @param document the document with header
     */
    void startDocument(Document document);
    void endDocument(Document document);
    void startPreamble();
    void endPreamble();
    void startContent();
    void endContent();
    void startDocumentTitle(DocumentTitle docTitle);
    void endDocumentTitle(DocumentTitle docTitle);
    void startTitle(Title title);
    void endTitle(Title title);
    void startParagraph(Paragraph paragraph);
    void endParagraph(Paragraph paragraph);
    void startSection(Section section);
    void endSection(Section section);
    void startSectionTitle(SectionTitle sectionTitle);
    void endSectionTitle(SectionTitle sectionTitle);
    void startAttributeEntry(AttributeEntry att);
    //void startAuthors(List<Author> authors);
}
