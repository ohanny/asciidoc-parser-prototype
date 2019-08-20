package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.writers;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.*;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.Outputter;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html.WriterState;

import java.util.List;

public class BlockHtmlWriter extends ModelHtmlWriter<BlockHtmlWriter> {

    public BlockHtmlWriter(Outputter outputter, WriterState state) {
        super(outputter, state);
    }

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

}
