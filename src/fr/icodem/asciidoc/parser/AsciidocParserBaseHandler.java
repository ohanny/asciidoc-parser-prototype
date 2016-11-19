package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.elements.*;

import java.util.List;

@Deprecated
public class AsciidocParserBaseHandler implements AsciidocParserHandler {
    @Override
    public void startDocument() {}

    @Override
    public void endDocument() {}

    @Override
    public void documentHeader(DocumentHeader header) {}

    @Override
    public void startPreamble() {}

    @Override
    public void endPreamble() {}

    @Override
    public void startParagraph(Paragraph paragraph) {}

    @Override
    public void startSection(Section section) {}

    @Override
    public void endSection(Section section) {}

    @Override
    public void startSectionTitle(SectionTitle sectionTitle) {}

    @Override
    public void startAttributeEntry(AttributeEntry att) {}

    @Override
    public void visitList(AbstractList list) {}
}
