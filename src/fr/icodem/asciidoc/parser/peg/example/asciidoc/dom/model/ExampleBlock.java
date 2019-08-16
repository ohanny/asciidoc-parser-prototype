package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

import java.util.List;

public class ExampleBlock extends Block {
    private List<Block> blocks;
    private String admonition;

    public static ExampleBlock of(AttributeList attributes, Title title, List<Block> blocks, String admonition) {
        ExampleBlock block = new ExampleBlock();
        block.type = ElementType.Example;
        block.attributes = attributes;
        block.title = title;
        block.blocks = blocks;
        block.admonition = admonition;

        return block;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public String getAdmonition() {
        return admonition;
    }
}
