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
    void startParagraph(Paragraph paragraph);
    void startSection(Section section);
    void endSection(Section section);
    void startSectionTitle(SectionTitle sectionTitle);
    void startAttributeEntry(AttributeEntry att);
}
