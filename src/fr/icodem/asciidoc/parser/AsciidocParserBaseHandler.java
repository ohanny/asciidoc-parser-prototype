package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.elements.*;

public class AsciidocParserBaseHandler implements AsciidocParserHandler {
    @Override
    public void startDocument(Document document) {}

    @Override
    public void endDocument(Document document) {}

    @Override
    public void startDocumentTitle(DocumentTitle docTitle) {}

    @Override
    public void endDocumentTitle(DocumentTitle docTitle) {}

    @Override
    public void startTitle(Title title) {}

    @Override
    public void endTitle(Title title) {}

    @Override
    public void startParagraph(Paragraph paragraph) {}

    @Override
    public void endParagraph(Paragraph paragraph) {}

    @Override
    public void startSection(Section section) {}

    @Override
    public void endSection(Section section) {}

    @Override
    public void startSectionTitle(SectionTitle sectionTitle) {}

    @Override
    public void endSectionTitle(SectionTitle sectionTitle) {}
}
