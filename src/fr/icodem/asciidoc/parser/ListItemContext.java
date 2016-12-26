package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.elements.Block;
import fr.icodem.asciidoc.parser.elements.Text;

import java.util.LinkedList;
import java.util.List;

@Deprecated
class ListItemContext {

    Text text;
    ListContext nestedList;
    List<Block> blocks;

    void addBlock(Block block) {
        if (blocks == null) blocks = new LinkedList<>();
        blocks.add(block);
    }

    boolean hasNestedList() {
        return nestedList != null;
    }

}
