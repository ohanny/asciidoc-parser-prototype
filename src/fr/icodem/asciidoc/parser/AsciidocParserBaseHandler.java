package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.elements.*;

import java.util.List;

public class AsciidocParserBaseHandler implements AsciidocParserHandler {
    @Override
    public void startDocument(Document document) {}

    @Override
    public void endDocument(Document document) {}

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
    public void attributeList(AttributeList attributeList) {

    }

}
