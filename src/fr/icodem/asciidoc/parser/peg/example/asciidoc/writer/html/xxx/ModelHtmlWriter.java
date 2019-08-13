package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.*;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlWriter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;

import java.io.IOException;
import java.util.List;

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

    protected void writeBlocks(List<Block> blocks) throws IOException {
        for (Block block : blocks) {
            writeBlock(block);
        }
    }

    protected void writeBlock(Block block) throws IOException {
        switch (block.getType()) {
            case Paragraph:
                writers.getParagraphWriter().write((Paragraph) block);
                break;
            case UnorderedList:
            case OrderedList:
                writers.getListWriter().write((ListBlock) block);
                break;
            case DescriptionList:
                writers.getDescriptionListWriter().write((DescriptionList) block);
                break;
            case Table:
                writers.getTableWriter().write((Table) block);
                break;
            case Quote:
                writers.getQuoteWriter().write((Quote) block);
                break;
            case Example:
                writers.getExampleWriter().write((ExampleBlock) block);
                break;
            case Literal:
                writers.getLiteralWriter().write((LiteralBlock) block);
                break;
            case Sidebar:
                writers.getSidebarWriter().write((Sidebar) block);
                break;
            case Listing:
                writers.getListingWriter().write((ListingBlock) block);
                break;
            case HorizontalRule:
                writers.getHorizontalRuleWriter().write((HorizontalRule) block);
                break;
        }
    }

    protected String replaceSpecialCharacters(String text) { // TODO temporary
        text = text.replaceAll("&", "&amp;");
        text = text.replaceAll("<", "&lt;");
        text = text.replaceAll(">", "&gt;");
        return text;
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

    public ListingHtmlWriter getListingWriter() {
        return writers.getListingWriter();
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
