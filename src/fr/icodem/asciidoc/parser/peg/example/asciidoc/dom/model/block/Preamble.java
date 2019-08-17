package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block;

import java.util.List;

public class Preamble {

    private List<Block> blocks;

    public static Preamble of(List<Block> blocks) {
        Preamble preamble = new Preamble();
        preamble.blocks = blocks;

        return preamble;
    }

    public List<Block> getBlocks() {
        return blocks;
    }
}
