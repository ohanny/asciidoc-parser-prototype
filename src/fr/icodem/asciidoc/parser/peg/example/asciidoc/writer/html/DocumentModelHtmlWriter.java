package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Document;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.DocumentModelWriter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.*;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.diapo.*;

import java.io.IOException;
import java.io.Writer;
import java.util.function.BiFunction;

public class DocumentModelHtmlWriter implements DocumentModelWriter {

    private WriterSet writers;

    public static class Builder {
        private BiFunction<Outputter, WriterSet, DocumentHtmlWriter> documentWriterFunc;

        private BiFunction<Outputter, WriterSet, HeaderHtmlWriter> headerWriterFunc;
        private BiFunction<Outputter, WriterSet, RevisionInfoHtmlWriter> revisionInfoWriterFunc;
        private BiFunction<Outputter, WriterSet, AuthorsHtmlWriter> authorsWriterFunc;

        private BiFunction<Outputter, WriterSet, ContentHtmlWriter> contentWriterFunc;
        private BiFunction<Outputter, WriterSet, PreambleHtmlWriter> preambleWriterFunc;
        private BiFunction<Outputter, WriterSet, SectionHtmlWriter> sectionWriterFunc;

        private BiFunction<Outputter, WriterSet, HorizontalRuleHtmlWriter> horizontalRuleWriterFunc;

        private BiFunction<Outputter, WriterSet, ParagraphHtmlWriter> paragraphWriterFunc;
        private BiFunction<Outputter, WriterSet, ListHtmlWriter> listWriterFunc;
        private BiFunction<Outputter, WriterSet, ListItemHtmlWriter> listItemWriterFunc;
        private BiFunction<Outputter, WriterSet, DescriptionListHtmlWriter> descriptionListWriterFunc;
        private BiFunction<Outputter, WriterSet, DescriptionListItemHtmlWriter> descriptionListItemWriterFunc;
        private BiFunction<Outputter, WriterSet, TableHtmlWriter> tableWriterFunc;
        private BiFunction<Outputter, WriterSet, TableRowHtmlWriter> tableRowWriterFunc;
        private BiFunction<Outputter, WriterSet, TableCellHtmlWriter> tableCellWriterFunc;
        private BiFunction<Outputter, WriterSet, QuoteHtmlWriter> quoteWriterFunc;
        private BiFunction<Outputter, WriterSet, ExampleHtmlWriter> exampleWriterFunc;
        private BiFunction<Outputter, WriterSet, LiteralHtmlWriter> literalWriterFunc;
        private BiFunction<Outputter, WriterSet, SidebarHtmlWriter> sidebarWriterFunc;

        public void write(Document document, Writer writer) throws IOException {
            Outputter outputter = new Outputter(writer);

            WriterSet writers = WriterSet.newInstance();

            writers.setDocumentWriter(documentWriterFunc.apply(outputter, writers));

            writers.setHeaderWriter(headerWriterFunc.apply(outputter, writers));
            writers.setRevisionInfoWriter(revisionInfoWriterFunc.apply(outputter, writers));
            writers.setAuthorsWriter(authorsWriterFunc.apply(outputter, writers));

            writers.setContentWriter(contentWriterFunc.apply(outputter, writers));
            writers.setPreambleWriter(preambleWriterFunc.apply(outputter, writers));
            writers.setSectionWriter(sectionWriterFunc.apply(outputter, writers));

            writers.setHorizontalRuleWriter(horizontalRuleWriterFunc.apply(outputter, writers));

            writers.setParagraphWriter(paragraphWriterFunc.apply(outputter, writers));
            writers.setListWriter(listWriterFunc.apply(outputter, writers));
            writers.setListItemWriter(listItemWriterFunc.apply(outputter, writers));
            writers.setDescriptionListWriter(descriptionListWriterFunc.apply(outputter, writers));
            writers.setDescriptionListItemWriter(descriptionListItemWriterFunc.apply(outputter, writers));
            writers.setTableWriter(tableWriterFunc.apply(outputter, writers));
            writers.setTableRowWriter(tableRowWriterFunc.apply(outputter, writers));
            writers.setTableCellWriter(tableCellWriterFunc.apply(outputter, writers));
            writers.setQuoteWriter(quoteWriterFunc.apply(outputter, writers));
            writers.setExampleWriter(exampleWriterFunc.apply(outputter, writers));
            writers.setLiteralWriter(literalWriterFunc.apply(outputter, writers));
            writers.setSidebarWriter(sidebarWriterFunc.apply(outputter, writers));

            DocumentModelHtmlWriter docWriter = new DocumentModelHtmlWriter();
            docWriter.writers = writers;

            docWriter.write(document, writer);
        }

        public Builder withDocumentWriter(BiFunction<Outputter, WriterSet, DocumentHtmlWriter> func) {
            this.documentWriterFunc = func;
            return this;
        }

        public Builder withHeaderWriter(BiFunction<Outputter, WriterSet, HeaderHtmlWriter> func) {
            this.headerWriterFunc = func;
            return this;
        }

        public Builder withRevisionInfoWriter(BiFunction<Outputter, WriterSet, RevisionInfoHtmlWriter> func) {
            this.revisionInfoWriterFunc = func;
            return this;
        }

        public Builder withAuthorsWriter(BiFunction<Outputter, WriterSet, AuthorsHtmlWriter> func) {
            this.authorsWriterFunc = func;
            return this;
        }

        public Builder withContentWriter(BiFunction<Outputter, WriterSet, ContentHtmlWriter> func) {
            this.contentWriterFunc = func;
            return this;
        }

        public Builder withPreambleWriter(BiFunction<Outputter, WriterSet, PreambleHtmlWriter> func) {
            this.preambleWriterFunc = func;
            return this;
        }

        public Builder withSectionWriter(BiFunction<Outputter, WriterSet, SectionHtmlWriter> func) {
            this.sectionWriterFunc = func;
            return this;
        }

        public Builder withHorizontalRuleWriter(BiFunction<Outputter, WriterSet, HorizontalRuleHtmlWriter> func) {
            this.horizontalRuleWriterFunc = func;
            return this;
        }

        public Builder withParagraphWriter(BiFunction<Outputter, WriterSet, ParagraphHtmlWriter> func) {
            this.paragraphWriterFunc = func;
            return this;
        }

        public Builder withListWriter(BiFunction<Outputter, WriterSet, ListHtmlWriter> func) {
            this.listWriterFunc = func;
            return this;
        }

        public Builder withListItemWriter(BiFunction<Outputter, WriterSet, ListItemHtmlWriter> func) {
            this.listItemWriterFunc = func;
            return this;
        }

        public Builder withDescriptionListWriter(BiFunction<Outputter, WriterSet, DescriptionListHtmlWriter> func) {
            this.descriptionListWriterFunc = func;
            return this;
        }

        public Builder withDescriptionListItemWriter(BiFunction<Outputter, WriterSet, DescriptionListItemHtmlWriter> func) {
            this.descriptionListItemWriterFunc = func;
            return this;
        }

        public Builder withTableWriter(BiFunction<Outputter, WriterSet, TableHtmlWriter> func) {
            this.tableWriterFunc = func;
            return this;
        }

        public Builder withTableRowWriter(BiFunction<Outputter, WriterSet, TableRowHtmlWriter> func) {
            this.tableRowWriterFunc = func;
            return this;
        }

        public Builder withTableCellWriter(BiFunction<Outputter, WriterSet, TableCellHtmlWriter> func) {
            this.tableCellWriterFunc = func;
            return this;
        }

        public Builder withQuoteWriter(BiFunction<Outputter, WriterSet, QuoteHtmlWriter> func) {
            this.quoteWriterFunc = func;
            return this;
        }

        public Builder withExampleWriter(BiFunction<Outputter, WriterSet, ExampleHtmlWriter> func) {
            this.exampleWriterFunc = func;
            return this;
        }

        public Builder withLiteralWriter(BiFunction<Outputter, WriterSet, LiteralHtmlWriter> func) {
            this.literalWriterFunc = func;
            return this;
        }

        public Builder withSidebarWriter(BiFunction<Outputter, WriterSet, SidebarHtmlWriter> func) {
            this.sidebarWriterFunc = func;
            return this;
        }

    }

    @Override
    public void write(Document document, Writer writer) throws IOException {
        writers.getDocumentWriter().write(document);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder diapo() {
        return newBuilder()
                .withDocumentWriter((outputter, writers) -> new DiapoDocumentHtmlWriter(outputter, writers))
                .withHeaderWriter((outputter, writers) -> new DiapoHeaderHtmlWriter(outputter, writers))
                .withRevisionInfoWriter((outputter, writers) -> new DiapoRevisionInfoHtmlWriter(outputter, writers))
                .withAuthorsWriter((outputter, writers) -> new DiapoAuthorsHtmlWriter(outputter, writers))
                .withContentWriter((outputter, writers) -> new DiapoContentHtmlWriter(outputter, writers))
                .withPreambleWriter((outputter, writers) -> new DiapoPreambleHtmlWriter(outputter, writers))
                .withSectionWriter((outputter, writers) -> new DiapoSectionHtmlWriter(outputter, writers))
                .withHorizontalRuleWriter((outputter, writers) -> new DiapoHorizontalRuleHtmlWriter(outputter, writers))
                .withParagraphWriter((outputter, writers) -> new DiapoParagraphHtmlWriter(outputter, writers))
                .withListWriter((outputter, writers) -> new DiapoListHtmlWriter(outputter, writers))
                .withListItemWriter((outputter, writers) -> new DiapoListItemHtmlWriter(outputter, writers))
                .withDescriptionListWriter((outputter, writers) -> new DiapoDescriptionListHtmlWriter(outputter, writers))
                .withDescriptionListItemWriter((outputter, writers) -> new DiapoDescriptionListItemHtmlWriter(outputter, writers))
                .withTableWriter((outputter, writers) -> new DiapoTableHtmlWriter(outputter, writers))
                .withTableRowWriter((outputter, writers) -> new DiapoTableRowHtmlWriter(outputter, writers))
                .withTableRowWriter((outputter, writers) -> new DiapoTableRowHtmlWriter(outputter, writers))
                .withTableCellWriter((outputter, writers) -> new DiapoTableCellHtmlWriter(outputter, writers))
                .withQuoteWriter((outputter, writers) -> new DiapoQuoteHtmlWriter(outputter, writers))
                .withExampleWriter((outputter, writers) -> new DiapoExampleHtmlWriter(outputter, writers))
                .withLiteralWriter((outputter, writers) -> new DiapoLiteralHtmlWriter(outputter, writers))
                .withSidebarWriter((outputter, writers) -> new DiapoSidebarHtmlWriter(outputter, writers));
    }

}
