package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.*;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlWriter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ModelHtmlWriter<MHW extends ModelHtmlWriter<MHW>> extends HtmlWriter<MHW> {

    public ModelHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

    protected Document getDocument() {
        return state.getDocument();
    }

    protected WriterSet getWriters() {
        return state.getWriterSet();
    }

    protected AttributeEntry getAttributeEntry(String name) {
        return getDocument().getAttributes().getAttribute(name);
    }

    protected String getAttributeEntryValue(String name) {
        return getDocument().getAttributes().getAttribute(name).getValue();
    }

    protected boolean isAttributeValueEqualTo(String name, String value) {
        return getDocument().getAttributes().isAttributeValueEqualTo(name, value);
    }

    protected boolean isAttributeEnabled(String name) {
        return getDocument().getAttributes().isAttributeEnabled(name);
    }

    protected void writeBlocks(List<Block> blocks) throws IOException {
        for (Block block : blocks) {
            writeBlock(block);
        }
    }

    protected void writeBlock(Block block) throws IOException {
        WriterSet writers = state.getWriterSet();
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

    /* **********************************************/
    // Attributes methods
    /* **********************************************/

    protected String getMoreClasses(String baseClass, AttributeList attList) {
        String moreClasses = getCssClasses(attList);
        if (baseClass == null) return moreClasses;
        return baseClass + (moreClasses == null?"":" " + moreClasses);
    }

    protected String getCssClasses(AttributeList attList) {
        String cssClasses = null;
        if (attList != null && attList.getRoles() != null && !attList.getRoles().isEmpty()) {
            cssClasses = attList.getRoles()
                    .stream()
                    .collect(Collectors.joining(" "));
        }

        return cssClasses;
    }


    public DocumentHtmlWriter getDocumentWriter() {
        return state.getWriterSet().getDocumentWriter();
    }

    public SectionHtmlWriter getSectionWriter() {
        return state.getWriterSet().getSectionWriter();
    }

    public static WriterSet newInstance() {
        return WriterSet.newInstance();
    }

    public HeaderHtmlWriter getHeaderWriter() {
        return state.getWriterSet().getHeaderWriter();
    }

    public RevisionInfoHtmlWriter getRevisionInfoWriter() {
        return state.getWriterSet().getRevisionInfoWriter();
    }

    public ContentHtmlWriter getContentWriter() {
        return state.getWriterSet().getContentWriter();
    }

    public PreambleHtmlWriter getPreambleWriter() {
        return state.getWriterSet().getPreambleWriter();
    }

    public HorizontalRuleHtmlWriter getHorizontalRuleWriter() {
        return state.getWriterSet().getHorizontalRuleWriter();
    }

    public ParagraphHtmlWriter getParagraphWriter() {
        return state.getWriterSet().getParagraphWriter();
    }

    public ListHtmlWriter getListWriter() {
        return state.getWriterSet().getListWriter();
    }

    public ListItemHtmlWriter getListItemWriter() {
        return state.getWriterSet().getListItemWriter();
    }

    public DescriptionListHtmlWriter getDescriptionListWriter() {
        return state.getWriterSet().getDescriptionListWriter();
    }

    public DescriptionListItemHtmlWriter getDescriptionListItemWriter() {
        return state.getWriterSet().getDescriptionListItemWriter();
    }

    public TableHtmlWriter getTableWriter() {
        return state.getWriterSet().getTableWriter();
    }

    public TableRowHtmlWriter getTableRowWriter() {
        return state.getWriterSet().getTableRowWriter();
    }

    public TableCellHtmlWriter getTableCellWriter() {
        return state.getWriterSet().getTableCellWriter();
    }

    public ListingHtmlWriter getListingWriter() {
        return state.getWriterSet().getListingWriter();
    }

    public QuoteHtmlWriter getQuoteWriter() {
        return state.getWriterSet().getQuoteWriter();
    }

    public ExampleHtmlWriter getExampleWriter() {
        return state.getWriterSet().getExampleWriter();
    }

    public LiteralHtmlWriter getLiteralWriter() {
        return state.getWriterSet().getLiteralWriter();
    }

    public SidebarHtmlWriter getSidebarWriter() {
        return state.getWriterSet().getSidebarWriter();
    }
}
