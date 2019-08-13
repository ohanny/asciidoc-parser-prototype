package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.DocumentModelBuilder;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeEntries;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public interface DocumentModelWriter extends DocumentWriter {

    void write(Document document, Writer writer) throws IOException;

    @Override
    default void write(Reader reader, Writer writer) throws IOException {
        DocumentModelBuilder builder = getDocumentModelBuilder();
        String source = getSource(reader);
        Document doc = builder.build(source);

        write(doc, writer);
    }

    default DocumentModelBuilder getDocumentModelBuilder() {
        AttributeEntries attributeEntries = AttributeEntries.newAttributeEntries();
        return DocumentModelBuilder.newDocumentBuilder(attributeEntries);
    }

    default String getSource(Reader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
        }

        return sb.toString();
    }

}
