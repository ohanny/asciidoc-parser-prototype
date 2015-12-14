package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.elements.*;

import java.util.List;

public interface AsciidocParserHandler {
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
}
