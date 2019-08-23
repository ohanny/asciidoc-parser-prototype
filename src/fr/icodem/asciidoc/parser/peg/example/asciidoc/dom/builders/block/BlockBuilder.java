package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Block;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Title;

public interface BlockBuilder {
    Block build();

    default Title buildTitle(BlockTitleBuilder builder) {
        return builder == null ? null : builder.build();
    }
}
