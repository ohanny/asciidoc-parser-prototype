package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeEntry;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Document;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlWriter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;

public class ModelHtmlWriter<MHW extends ModelHtmlWriter<MHW>> extends HtmlWriter<MHW> {
    protected Document document;
    protected WriterSet writers;

    public ModelHtmlWriter(Outputter outputter, WriterSet writers) {
        super(outputter);
        this.writers = writers;
    }

    public void setDocument(Document document) { // TODO change way to pass document
        this.document = document;
    }

    protected AttributeEntry getAttributeEntry(String name) {
        return document.getAttributes().getAttribute(name);
    }

    protected String getAttributeEntryValue(String name) {
        return document.getAttributes().getAttribute(name).getValue();
    }

    protected boolean isAttributeValueEqualTo(String name, String value) {
        return document.getAttributes().isAttributeValueEqualTo(name, value);
    }

    protected boolean isAttributeEnabled(String name) {
        return document.getAttributes().isAttributeEnabled(name);
    }


    public DocumentHtmlWriter getDocumentWriter() {
        return writers.getDocumentWriter();
    }

    public SectionHtmlWriter getSectionWriter() {
        return writers.getSectionWriter();
    }

    public static WriterSet newInstance() {
        return WriterSet.newInstance();
    }

    public HeaderHtmlWriter getHeaderWriter() {
        return writers.getHeaderWriter();
    }

    public RevisionInfoHtmlWriter getRevisionInfoWriter() {
        return writers.getRevisionInfoWriter();
    }

    public ContentHtmlWriter getContentWriter() {
        return writers.getContentWriter();
    }

    public PreambleHtmlWriter getPreambleWriter() {
        return writers.getPreambleWriter();
    }

    public HorizontalRuleHtmlWriter getHorizontalRuleWriter() {
        return writers.getHorizontalRuleWriter();
    }

    public ParagraphHtmlWriter getParagraphWriter() {
        return writers.getParagraphWriter();
    }

    public ListHtmlWriter getListWriter() {
        return writers.getListWriter();
    }

    public ListItemHtmlWriter getListItemWriter() {
        return writers.getListItemWriter();
    }

    public DescriptionListHtmlWriter getDescriptionListWriter() {
        return writers.getDescriptionListWriter();
    }

    public DescriptionListItemHtmlWriter getDescriptionListItemWriter() {
        return writers.getDescriptionListItemWriter();
    }

    public TableHtmlWriter getTableWriter() {
        return writers.getTableWriter();
    }

    public TableRowHtmlWriter getTableRowWriter() {
        return writers.getTableRowWriter();
    }

    public TableCellHtmlWriter getTableCellWriter() {
        return writers.getTableCellWriter();
    }

    public QuoteHtmlWriter getQuoteWriter() {
        return writers.getQuoteWriter();
    }

    public ExampleHtmlWriter getExampleWriter() {
        return writers.getExampleWriter();
    }

    public LiteralHtmlWriter getLiteralWriter() {
        return writers.getLiteralWriter();
    }

    public SidebarHtmlWriter getSidebarWriter() {
        return writers.getSidebarWriter();
    }
}
