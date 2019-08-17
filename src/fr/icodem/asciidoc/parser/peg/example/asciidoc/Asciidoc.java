package fr.icodem.asciidoc.parser.peg.example.asciidoc;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.DocumentModelBuilder;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeEntries;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeEntry;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Document;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.DocumentWriter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.DocumentModelHtmlWriter;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Asciidoc {

    public static void main(String[] args) throws IOException {

        Path path = Paths.get(System.getProperty("user.home")).resolve("BTSync/Professionnel/cours/appo/01-objet");
        Path input = path.resolve("00-index.adoc");
        Path output = path.resolve("00-aaaa.html");

        Asciidoc.diapo().withDocDir(path).write(input, output);
    }

    public static class Builder implements DocumentWriter {
        private AttributeEntries attributeEntries;
        private DocumentModelBuilder docBuilder;
        private DocumentModelHtmlWriter.Builder docWriter;

        public static Builder newBuilder() {

            Builder builder = new Builder();

            builder.attributeEntries = AttributeEntries.newAttributeEntries();
            builder.docBuilder = DocumentModelBuilder.newDocumentBuilder(builder.attributeEntries);

            return builder;
        }

        public Builder diapo() {
            docWriter = DocumentModelHtmlWriter.diapo();
            return this;
        }

        public Builder withDocDir(Path path) {
            attributeEntries.addAttribute(AttributeEntry.of("docdir", path.toString()));
            return this;
        }

        @Override
        public void write(Reader reader, Writer writer) throws IOException {
            Document doc = docBuilder.build(reader);
            docWriter.write(doc, writer);
        }
    }

    public static Builder diapo() {
        return Builder.newBuilder().diapo();
    }

}
