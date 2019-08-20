package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers.*;

public class WriterSet {
    // inline
    private InlineNodeHtmlWriter inlineNodeWriter;
    private InlineListNodeHtmlWriter inlineListNodeWriter;
    private StringHtmlWriter stringWriter;
    private TextHtmlWriter textWriter;
    private TextNodeHtmlWriter textNodeWriter;
    private DecoratorNodeHtmlWriter boldNodeWriter;
    private DecoratorNodeHtmlWriter italicNodeWriter;
    private DecoratorNodeHtmlWriter superscriptNodeWriter;
    private DecoratorNodeHtmlWriter subscriptNodeWriter;
    private DecoratorNodeHtmlWriter monospaceNodeWriter;
    private DecoratorNodeHtmlWriter markNodeWriter;
    private XRefNodeHtmlWriter xRefNodeWriter;

    // block
    private BlockHtmlWriter blockWriter;
    private BlockTitleHtmlWriter blockTitleWriter;
    private DocumentHtmlWriter documentWriter;

    private HeaderHtmlWriter headerWriter;
    private AuthorsHtmlWriter authorsWriter;
    private RevisionInfoHtmlWriter revisionInfoWriter;

    private ContentHtmlWriter contentWriter;
    private PreambleHtmlWriter preambleWriter;
    private SectionHtmlWriter sectionWriter;

    private HorizontalRuleHtmlWriter horizontalRuleWriter;
    private ImageBlockHtmlWriter imageBlockWriter;
    private VideoBlockHtmlWriter videoBlockWriter;

    private ParagraphHtmlWriter paragraphWriter;
    private AdmonitionHtmlWriter admonitionWriter;
    private ListHtmlWriter unorderedListWriter;
    private ListHtmlWriter orderedListWriter;
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

    // inline
    public InlineNodeHtmlWriter getInlineNodeWriter() {
        return inlineNodeWriter;
    }

    public void setInlineNodeWriter(InlineNodeHtmlWriter inlineNodeWriter) {
        this.inlineNodeWriter = inlineNodeWriter;
    }

    public InlineListNodeHtmlWriter getInlineListNodeWriter() {
        return inlineListNodeWriter;
    }

    public void setInlineListNodeWriter(InlineListNodeHtmlWriter inlineListNodeWriter) {
        this.inlineListNodeWriter = inlineListNodeWriter;
    }

    public StringHtmlWriter getStringWriter() {
        return stringWriter;
    }

    public void setStringWriter(StringHtmlWriter stringWriter) {
        this.stringWriter = stringWriter;
    }

    public TextHtmlWriter getTextWriter() {
        return textWriter;
    }

    public void setTextWriter(TextHtmlWriter textWriter) {
        this.textWriter = textWriter;
    }

    public TextNodeHtmlWriter getTextNodeWriter() {
        return textNodeWriter;
    }

    public void setTextNodeWriter(TextNodeHtmlWriter textNodeWriter) {
        this.textNodeWriter = textNodeWriter;
    }

    public DecoratorNodeHtmlWriter getBoldNodeWriter() {
        return boldNodeWriter;
    }

    public void setBoldNodeWriter(DecoratorNodeHtmlWriter boldNodeWriter) {
        this.boldNodeWriter = boldNodeWriter;
    }

    public DecoratorNodeHtmlWriter getItalicNodeWriter() {
        return italicNodeWriter;
    }

    public void setItalicNodeWriter(DecoratorNodeHtmlWriter italicNodeWriter) {
        this.italicNodeWriter = italicNodeWriter;
    }

    public DecoratorNodeHtmlWriter getSuperscriptNodeWriter() {
        return superscriptNodeWriter;
    }

    public void setSuperscriptNodeWriter(DecoratorNodeHtmlWriter superscriptNodeWriter) {
        this.superscriptNodeWriter = superscriptNodeWriter;
    }

    public DecoratorNodeHtmlWriter getSubscriptNodeWriter() {
        return subscriptNodeWriter;
    }

    public void setSubscriptNodeWriter(DecoratorNodeHtmlWriter subscriptNodeWriter) {
        this.subscriptNodeWriter = subscriptNodeWriter;
    }

    public DecoratorNodeHtmlWriter getMonospaceNodeWriter() {
        return monospaceNodeWriter;
    }

    public void setMonospaceNodeWriter(DecoratorNodeHtmlWriter monospaceNodeWriter) {
        this.monospaceNodeWriter = monospaceNodeWriter;
    }

    public DecoratorNodeHtmlWriter getMarkNodeWriter() {
        return markNodeWriter;
    }

    public void setMarkNodeWriter(DecoratorNodeHtmlWriter markNodeWriter) {
        this.markNodeWriter = markNodeWriter;
    }

    public XRefNodeHtmlWriter getxRefNodeWriter() {
        return xRefNodeWriter;
    }

    public void setxRefNodeWriter(XRefNodeHtmlWriter xRefNodeWriter) {
        this.xRefNodeWriter = xRefNodeWriter;
    }

    // block

    public BlockHtmlWriter getBlockWriter() {
        return blockWriter;
    }

    public void setBlockWriter(BlockHtmlWriter blockWriter) {
        this.blockWriter = blockWriter;
    }

    public BlockTitleHtmlWriter getBlockTitleWriter() {
        return blockTitleWriter;
    }

    public void setBlockTitleWriter(BlockTitleHtmlWriter blockTitleWriter) {
        this.blockTitleWriter = blockTitleWriter;
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

    public ImageBlockHtmlWriter getImageBlockWriter() {
        return imageBlockWriter;
    }

    public void setImageBlockWriter(ImageBlockHtmlWriter imageBlockWriter) {
        this.imageBlockWriter = imageBlockWriter;
    }

    public VideoBlockHtmlWriter getVideoBlockWriter() {
        return videoBlockWriter;
    }

    public void setVideoBlockWriter(VideoBlockHtmlWriter videoBlockWriter) {
        this.videoBlockWriter = videoBlockWriter;
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

    public AdmonitionHtmlWriter getAdmonitionWriter() {
        return admonitionWriter;
    }

    public void setAdmonitionWriter(AdmonitionHtmlWriter admonitionHtmlWriter) {
        this.admonitionWriter = admonitionHtmlWriter;
    }

    public ListHtmlWriter getUnorderedListWriter() {
        return unorderedListWriter;
    }

    public void setUnorderedListWriter(ListHtmlWriter listWriter) {
        this.unorderedListWriter = listWriter;
    }

    public ListHtmlWriter getOrderedListWriter() {
        return orderedListWriter;
    }

    public void setOrderedListWriter(ListHtmlWriter orderedListWriter) {
        this.orderedListWriter = orderedListWriter;
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
