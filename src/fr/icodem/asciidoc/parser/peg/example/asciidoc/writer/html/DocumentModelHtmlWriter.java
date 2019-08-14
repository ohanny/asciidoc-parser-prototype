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
        private BiFunction<Outputter, WriterState, DocumentHtmlWriter> documentWriterFunc;

        private BiFunction<Outputter, WriterState, HeaderHtmlWriter> headerWriterFunc;
        private BiFunction<Outputter, WriterState, RevisionInfoHtmlWriter> revisionInfoWriterFunc;
        private BiFunction<Outputter, WriterState, AuthorsHtmlWriter> authorsWriterFunc;

        private BiFunction<Outputter, WriterState, ContentHtmlWriter> contentWriterFunc;
        private BiFunction<Outputter, WriterState, PreambleHtmlWriter> preambleWriterFunc;
        private BiFunction<Outputter, WriterState, SectionHtmlWriter> sectionWriterFunc;

        private BiFunction<Outputter, WriterState, HorizontalRuleHtmlWriter> horizontalRuleWriterFunc;

        private BiFunction<Outputter, WriterState, ParagraphHtmlWriter> paragraphWriterFunc;
        private BiFunction<Outputter, WriterState, ListHtmlWriter> listWriterFunc;
        private BiFunction<Outputter, WriterState, ListItemHtmlWriter> listItemWriterFunc;
        private BiFunction<Outputter, WriterState, DescriptionListHtmlWriter> descriptionListWriterFunc;
        private BiFunction<Outputter, WriterState, DescriptionListItemHtmlWriter> descriptionListItemWriterFunc;
        private BiFunction<Outputter, WriterState, TableHtmlWriter> tableWriterFunc;
        private BiFunction<Outputter, WriterState, TableRowHtmlWriter> tableRowWriterFunc;
        private BiFunction<Outputter, WriterState, TableCellHtmlWriter> tableCellWriterFunc;
        private BiFunction<Outputter, WriterState, ListingHtmlWriter> listingWriterFunc;
        private BiFunction<Outputter, WriterState, QuoteHtmlWriter> quoteWriterFunc;
        private BiFunction<Outputter, WriterState, ExampleHtmlWriter> exampleWriterFunc;
        private BiFunction<Outputter, WriterState, LiteralHtmlWriter> literalWriterFunc;
        private BiFunction<Outputter, WriterState, SidebarHtmlWriter> sidebarWriterFunc;

        public void write(Document document, Writer writer) throws IOException {
            Outputter outputter = new Outputter(writer);

            WriterSet writers = WriterSet.newInstance();
            WriterState state = WriterState.newInstance();
            state.setWriterSet(writers);

            writers.setDocumentWriter(documentWriterFunc.apply(outputter, state));

            writers.setHeaderWriter(headerWriterFunc.apply(outputter, state));
            writers.setRevisionInfoWriter(revisionInfoWriterFunc.apply(outputter, state));
            writers.setAuthorsWriter(authorsWriterFunc.apply(outputter, state));

            writers.setContentWriter(contentWriterFunc.apply(outputter, state));
            writers.setPreambleWriter(preambleWriterFunc.apply(outputter, state));
            writers.setSectionWriter(sectionWriterFunc.apply(outputter, state));

            writers.setHorizontalRuleWriter(horizontalRuleWriterFunc.apply(outputter, state));

            writers.setParagraphWriter(paragraphWriterFunc.apply(outputter, state));
            writers.setListWriter(listWriterFunc.apply(outputter, state));
            writers.setListItemWriter(listItemWriterFunc.apply(outputter, state));
            writers.setDescriptionListWriter(descriptionListWriterFunc.apply(outputter, state));
            writers.setDescriptionListItemWriter(descriptionListItemWriterFunc.apply(outputter, state));
            writers.setTableWriter(tableWriterFunc.apply(outputter, state));
            writers.setTableRowWriter(tableRowWriterFunc.apply(outputter, state));
            writers.setTableCellWriter(tableCellWriterFunc.apply(outputter, state));
            writers.setListingWriter(listingWriterFunc.apply(outputter, state));
            writers.setQuoteWriter(quoteWriterFunc.apply(outputter, state));
            writers.setExampleWriter(exampleWriterFunc.apply(outputter, state));
            writers.setLiteralWriter(literalWriterFunc.apply(outputter, state));
            writers.setSidebarWriter(sidebarWriterFunc.apply(outputter, state));

            DocumentModelHtmlWriter docWriter = new DocumentModelHtmlWriter();
            docWriter.writers = writers;

            docWriter.write(document, writer);
        }

        public Builder withDocumentWriter(BiFunction<Outputter, WriterState, DocumentHtmlWriter> func) {
            this.documentWriterFunc = func;
            return this;
        }

        public Builder withHeaderWriter(BiFunction<Outputter, WriterState, HeaderHtmlWriter> func) {
            this.headerWriterFunc = func;
            return this;
        }

        public Builder withRevisionInfoWriter(BiFunction<Outputter, WriterState, RevisionInfoHtmlWriter> func) {
            this.revisionInfoWriterFunc = func;
            return this;
        }

        public Builder withAuthorsWriter(BiFunction<Outputter, WriterState, AuthorsHtmlWriter> func) {
            this.authorsWriterFunc = func;
            return this;
        }

        public Builder withContentWriter(BiFunction<Outputter, WriterState, ContentHtmlWriter> func) {
            this.contentWriterFunc = func;
            return this;
        }

        public Builder withPreambleWriter(BiFunction<Outputter, WriterState, PreambleHtmlWriter> func) {
            this.preambleWriterFunc = func;
            return this;
        }

        public Builder withSectionWriter(BiFunction<Outputter, WriterState, SectionHtmlWriter> func) {
            this.sectionWriterFunc = func;
            return this;
        }

        public Builder withHorizontalRuleWriter(BiFunction<Outputter, WriterState, HorizontalRuleHtmlWriter> func) {
            this.horizontalRuleWriterFunc = func;
            return this;
        }

        public Builder withParagraphWriter(BiFunction<Outputter, WriterState, ParagraphHtmlWriter> func) {
            this.paragraphWriterFunc = func;
            return this;
        }

        public Builder withListWriter(BiFunction<Outputter, WriterState, ListHtmlWriter> func) {
            this.listWriterFunc = func;
            return this;
        }

        public Builder withListItemWriter(BiFunction<Outputter, WriterState, ListItemHtmlWriter> func) {
            this.listItemWriterFunc = func;
            return this;
        }

        public Builder withDescriptionListWriter(BiFunction<Outputter, WriterState, DescriptionListHtmlWriter> func) {
            this.descriptionListWriterFunc = func;
            return this;
        }

        public Builder withDescriptionListItemWriter(BiFunction<Outputter, WriterState, DescriptionListItemHtmlWriter> func) {
            this.descriptionListItemWriterFunc = func;
            return this;
        }

        public Builder withTableWriter(BiFunction<Outputter, WriterState, TableHtmlWriter> func) {
            this.tableWriterFunc = func;
            return this;
        }

        public Builder withTableRowWriter(BiFunction<Outputter, WriterState, TableRowHtmlWriter> func) {
            this.tableRowWriterFunc = func;
            return this;
        }

        public Builder withTableCellWriter(BiFunction<Outputter, WriterState, TableCellHtmlWriter> func) {
            this.tableCellWriterFunc = func;
            return this;
        }

        public Builder withListingWriter(BiFunction<Outputter, WriterState, ListingHtmlWriter> func) {
            this.listingWriterFunc = func;
            return this;
        }

        public Builder withQuoteWriter(BiFunction<Outputter, WriterState, QuoteHtmlWriter> func) {
            this.quoteWriterFunc = func;
            return this;
        }

        public Builder withExampleWriter(BiFunction<Outputter, WriterState, ExampleHtmlWriter> func) {
            this.exampleWriterFunc = func;
            return this;
        }

        public Builder withLiteralWriter(BiFunction<Outputter, WriterState, LiteralHtmlWriter> func) {
            this.literalWriterFunc = func;
            return this;
        }

        public Builder withSidebarWriter(BiFunction<Outputter, WriterState, SidebarHtmlWriter> func) {
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
                .withDocumentWriter((outputter, state) -> new DiapoDocumentHtmlWriter(outputter, state))
                .withHeaderWriter((outputter, state) -> new DiapoHeaderHtmlWriter(outputter, state))
                .withRevisionInfoWriter((outputter, state) -> new DiapoRevisionInfoHtmlWriter(outputter, state))
                .withAuthorsWriter((outputter, state) -> new DiapoAuthorsHtmlWriter(outputter, state))
                .withContentWriter((outputter, state) -> new DiapoContentHtmlWriter(outputter, state))
                .withPreambleWriter((outputter, state) -> new DiapoPreambleHtmlWriter(outputter, state))
                .withSectionWriter((outputter, state) -> new DiapoSectionHtmlWriter(outputter, state))
                .withHorizontalRuleWriter((outputter, state) -> new DiapoHorizontalRuleHtmlWriter(outputter, state))
                .withParagraphWriter((outputter, state) -> new DiapoParagraphHtmlWriter(outputter, state))
                .withListWriter((outputter, state) -> new DiapoListHtmlWriter(outputter, state))
                .withListItemWriter((outputter, state) -> new DiapoListItemHtmlWriter(outputter, state))
                .withDescriptionListWriter((outputter, state) -> new DiapoDescriptionListHtmlWriter(outputter, state))
                .withDescriptionListItemWriter((outputter, state) -> new DiapoDescriptionListItemHtmlWriter(outputter, state))
                .withTableWriter((outputter, state) -> new DiapoTableHtmlWriter(outputter, state))
                .withTableRowWriter((outputter, state) -> new DiapoTableRowHtmlWriter(outputter, state))
                .withTableRowWriter((outputter, state) -> new DiapoTableRowHtmlWriter(outputter, state))
                .withTableCellWriter((outputter, state) -> new DiapoTableCellHtmlWriter(outputter, state))
                .withListingWriter((outputter, state) -> new DiapoListingHtmlWriter(outputter, state))
                .withQuoteWriter((outputter, state) -> new DiapoQuoteHtmlWriter(outputter, state))
                .withExampleWriter((outputter, state) -> new DiapoExampleHtmlWriter(outputter, state))
                .withLiteralWriter((outputter, state) -> new DiapoLiteralHtmlWriter(outputter, state))
                .withSidebarWriter((outputter, state) -> new DiapoSidebarHtmlWriter(outputter, state));
    }

}
