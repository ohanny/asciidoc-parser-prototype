package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.*;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.*;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.HtmlWriter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterSet;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

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

    /*
    protected void writeBlocks(List<Block> blocks) {
        if (blocks == null) return;;
        for (Block block : blocks) {
            writeBlock(block);
        }
    }

    protected void writeBlock(Block block) {
        switch (block.getType()) {
            case Paragraph:
                Paragraph p = (Paragraph)block;
                if (p.getAdmonition() == null) {
                    getParagraphWriter().write((Paragraph) block);
                } else {
                    getAdmonitionWriter().write(p);
                }
                break;
            case UnorderedList:
                getUnorderedListWriter().write((ListBlock) block);
                break;
            case OrderedList:
                getOrderedListWriter().write((ListBlock) block);
                break;
            case DescriptionList:
                getDescriptionListWriter().write((DescriptionList) block);
                break;
            case Table:
                getTableWriter().write((Table) block);
                break;
            case Quote:
                getQuoteWriter().write((Quote) block);
                break;
            case Example:
                ExampleBlock example = (ExampleBlock)block;
                if (example.getAdmonition() == null) {
                    getExampleWriter().write((ExampleBlock) block);
                } else {
                    getAdmonitionWriter().write(example);
                }
                break;
            case Literal:
                getLiteralWriter().write((LiteralBlock) block);
                break;
            case Sidebar:
                getSidebarWriter().write((Sidebar) block);
                break;
            case Listing:
                getListingWriter().write((ListingBlock) block);
                break;
            case HorizontalRule:
                getHorizontalRuleWriter().write((HorizontalRule) block);
                break;
            case ImageBlock:
                getImageBlockWriter().write((ImageBlock) block);
                break;
            case Video:
                getVideoBlockWriter().write((VideoBlock) block);
                break;
        }
    }

     */

    public MHW writeBlockTitle(Block block) {
        if (block.getTitle() != null) {
            getBlockTitleWriter().write(block.getTitle());
        }

        return (MHW) this;
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


    // inline
    public InlineNodeHtmlWriter getInlineNodeWriter() {
        return state.getWriterSet().getInlineNodeWriter();
    }

    public InlineListNodeHtmlWriter getInlineListNodeWriter() {
        return state.getWriterSet().getInlineListNodeWriter();
    }

    public StringHtmlWriter getStringWriter() {
        return state.getWriterSet().getStringWriter();
    }

    public TextHtmlWriter getTextWriter() {
        return state.getWriterSet().getTextWriter();
    }

    public TextNodeHtmlWriter getTextNodeWriter() {
        return state.getWriterSet().getTextNodeWriter();
    }

    public DecoratorNodeHtmlWriter getBoldNodeWriter() {
        return state.getWriterSet().getBoldNodeWriter();
    }

    public DecoratorNodeHtmlWriter getItalicNodeWriter() {
        return state.getWriterSet().getItalicNodeWriter();
    }

    public DecoratorNodeHtmlWriter getSuperscriptNodeWriter() {
        return state.getWriterSet().getSuperscriptNodeWriter();
    }

    public DecoratorNodeHtmlWriter getSubscriptNodeWriter() {
        return state.getWriterSet().getSubscriptNodeWriter();
    }

    public DecoratorNodeHtmlWriter getMonospaceNodeWriter() {
        return state.getWriterSet().getMonospaceNodeWriter();
    }

    public DecoratorNodeHtmlWriter getMarkNodeWriter() {
        return state.getWriterSet().getMarkNodeWriter();
    }

    public XRefNodeHtmlWriter getXRefNodeWriter() {
        return state.getWriterSet().getxRefNodeWriter();
    }


    // block
    public BlockHtmlWriter getBlockWriter() {
        return state.getWriterSet().getBlockWriter();
    }

    public BlockTitleHtmlWriter getBlockTitleWriter() {
        return state.getWriterSet().getBlockTitleWriter();
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

    public ImageBlockHtmlWriter getImageBlockWriter() {
        return state.getWriterSet().getImageBlockWriter();
    }

    public VideoBlockHtmlWriter getVideoBlockWriter() {
        return state.getWriterSet().getVideoBlockWriter();
    }

    public ParagraphHtmlWriter getParagraphWriter() {
        return state.getWriterSet().getParagraphWriter();
    }

    public AdmonitionHtmlWriter getAdmonitionWriter() {
        return state.getWriterSet().getAdmonitionWriter();
    }

    public ListHtmlWriter getUnorderedListWriter() {
        return state.getWriterSet().getUnorderedListWriter();
    }

    public ListHtmlWriter getOrderedListWriter() {
        return state.getWriterSet().getOrderedListWriter();
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
