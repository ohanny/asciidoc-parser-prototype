package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

import java.util.List;

public class ExampleBlock extends Block {
    protected List<Block> blocks;

    public static ExampleBlock of(List<Block> blocks) {
        ExampleBlock block = new ExampleBlock();
        block.type = ElementType.Example;
        block.blocks = blocks;

        return block;
    }

    public List<Block> getBlocks() {
        return blocks;
    }
}
