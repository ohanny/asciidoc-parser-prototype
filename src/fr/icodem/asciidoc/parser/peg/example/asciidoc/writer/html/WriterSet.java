package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.xxx.*;

public class WriterSet {
    private DocumentHtmlWriter documentWriter;

    private HeaderHtmlWriter headerWriter;
    private AuthorsHtmlWriter authorsWriter;
    private RevisionInfoHtmlWriter revisionInfoWriter;

    private ContentHtmlWriter contentWriter;
    private PreambleHtmlWriter preambleWriter;
    private SectionHtmlWriter sectionWriter;

    private HorizontalRuleHtmlWriter horizontalRuleWriter;

    private ParagraphHtmlWriter paragraphWriter;
    private ListHtmlWriter listWriter;
    private ListItemHtmlWriter listItemWriter;
    private DescriptionListHtmlWriter descriptionListWriter;
    private DescriptionListItemHtmlWriter descriptionListItemWriter;
    private TableHtmlWriter tableWriter;
    private TableRowHtmlWriter tableRowWriter;
    private TableCellHtmlWriter tableCellWriter;
    private ListingHtmlWriter listingWriter;
    private QuoteHtmlWriter quoteWriter;
    private ExampleHtmlWriter exampleWriter;
    private LiteralHtmlWriter literalWriter;
    private SidebarHtmlWriter sidebarWriter;


    public static WriterSet newInstance() {
        return new WriterSet();
    }

    public DocumentHtmlWriter getDocumentWriter() {
        return documentWriter;
    }

    public void setDocumentWriter(DocumentHtmlWriter documentWriter) {
        this.documentWriter = documentWriter;
    }

    public HeaderHtmlWriter getHeaderWriter() {
        return headerWriter;
    }

    public void setHeaderWriter(HeaderHtmlWriter headerWriter) {
        this.headerWriter = headerWriter;
    }

    public RevisionInfoHtmlWriter getRevisionInfoWriter() {
        return revisionInfoWriter;
    }

    public void setRevisionInfoWriter(RevisionInfoHtmlWriter revisionInfoWriter) {
        this.revisionInfoWriter = revisionInfoWriter;
    }

    public AuthorsHtmlWriter getAuthorsWriter() {
        return authorsWriter;
    }

    public void setAuthorsWriter(AuthorsHtmlWriter authorsWriter) {
        this.authorsWriter = authorsWriter;
    }

    public ContentHtmlWriter getContentWriter() {
        return contentWriter;
    }

    public void setContentWriter(ContentHtmlWriter contentWriter) {
        this.contentWriter = contentWriter;
    }

    public PreambleHtmlWriter getPreambleWriter() {
        return preambleWriter;
    }

    public void setPreambleWriter(PreambleHtmlWriter preambleWriter) {
        this.preambleWriter = preambleWriter;
    }

    public SectionHtmlWriter getSectionWriter() {
        return sectionWriter;
    }

    public void setSectionWriter(SectionHtmlWriter sectionWriter) {
        this.sectionWriter = sectionWriter;
    }

    public HorizontalRuleHtmlWriter getHorizontalRuleWriter() {
        return horizontalRuleWriter;
    }

    public void setHorizontalRuleWriter(HorizontalRuleHtmlWriter horizontalRuleWriter) {
        this.horizontalRuleWriter = horizontalRuleWriter;
    }

    public ParagraphHtmlWriter getParagraphWriter() {
        return paragraphWriter;
    }

    public void setParagraphWriter(ParagraphHtmlWriter paragraphWriter) {
        this.paragraphWriter = paragraphWriter;
    }

    public ListHtmlWriter getListWriter() {
        return listWriter;
    }

    public void setListWriter(ListHtmlWriter listWriter) {
        this.listWriter = listWriter;
    }

    public ListItemHtmlWriter getListItemWriter() {
        return listItemWriter;
    }

    public void setListItemWriter(ListItemHtmlWriter listItemWriter) {
        this.listItemWriter = listItemWriter;
    }

    public DescriptionListHtmlWriter getDescriptionListWriter() {
        return descriptionListWriter;
    }

    public void setDescriptionListWriter(DescriptionListHtmlWriter descriptionListWriter) {
        this.descriptionListWriter = descriptionListWriter;
    }

    public DescriptionListItemHtmlWriter getDescriptionListItemWriter() {
        return descriptionListItemWriter;
    }

    public void setDescriptionListItemWriter(DescriptionListItemHtmlWriter descriptionListItemWriter) {
        this.descriptionListItemWriter = descriptionListItemWriter;
    }

    public TableHtmlWriter getTableWriter() {
        return tableWriter;
    }

    public void setTableWriter(TableHtmlWriter tableWriter) {
        this.tableWriter = tableWriter;
    }

    public TableRowHtmlWriter getTableRowWriter() {
        return tableRowWriter;
    }

    public void setTableRowWriter(TableRowHtmlWriter tableRowWriter) {
        this.tableRowWriter = tableRowWriter;
    }

    public TableCellHtmlWriter getTableCellWriter() {
        return tableCellWriter;
    }

    public void setTableCellWriter(TableCellHtmlWriter tableCellWriter) {
        this.tableCellWriter = tableCellWriter;
    }

    public ListingHtmlWriter getListingWriter() {
        return listingWriter;
    }

    public void setListingWriter(ListingHtmlWriter listingWriter) {
        this.listingWriter = listingWriter;
    }

    public QuoteHtmlWriter getQuoteWriter() {
        return quoteWriter;
    }

    public void setQuoteWriter(QuoteHtmlWriter quoteWriter) {
        this.quoteWriter = quoteWriter;
    }

    public ExampleHtmlWriter getExampleWriter() {
        return exampleWriter;
    }

    public void setExampleWriter(ExampleHtmlWriter exampleWriter) {
        this.exampleWriter = exampleWriter;
    }

    public LiteralHtmlWriter getLiteralWriter() {
        return literalWriter;
    }

    public void setLiteralWriter(LiteralHtmlWriter literalWriter) {
        this.literalWriter = literalWriter;
    }

    public SidebarHtmlWriter getSidebarWriter() {
        return sidebarWriter;
    }

    public void setSidebarWriter(SidebarHtmlWriter sidebarWriter) {
        this.sidebarWriter = sidebarWriter;
    }
}
