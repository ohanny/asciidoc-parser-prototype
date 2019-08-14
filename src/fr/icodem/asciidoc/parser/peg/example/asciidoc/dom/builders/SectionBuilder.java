package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SectionBuilder implements BlockContainer {
    private int level;
    private int position;
    private String title;
    private String ref;
    private AttributeList attList;

    private SectionBuilder next;
    private SectionBuilder parent;

    private List<BlockBuilder> blocks;

    public static SectionBuilder of(AttributeList attList, int level) {
        SectionBuilder builder = new SectionBuilder();
        builder.attList = attList;
        builder.level = level;
        builder.blocks = new ArrayList<>();
        return builder;
    }

    public static SectionBuilder of(AttributeList attList, int level, SectionBuilder previous, SectionBuilder parent) {
        SectionBuilder builder = new SectionBuilder();
        builder.attList = attList;
        builder.level = level;
        builder.position = previous.position + 1;
        builder.parent = parent;
        builder.blocks = new ArrayList<>();
        return builder;
    }

    public Section build() {
        List<Block> blocks = this.blocks
                                 .stream()
                                 .map(BlockBuilder::build)
                                 .collect(Collectors.toList());

        Section section = Section.of(attList, level, position, Title.of(title), blocks);
        return section;
    }

    @Override
    public void addBlock(BlockBuilder builder) {
        this.blocks.add(builder);
    }

    public int getLevel() {
        return level;
    }

    public void setTitle(String title) {
        if (title != null) {
            this.title = title.trim();
        }
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public SectionBuilder getNext() {
        return next;
    }

    public void setNext(SectionBuilder next) {
        this.next = next;
    }

    public SectionBuilder getParent() {
        return parent;
    }

    public void setParent(SectionBuilder parent) {
        this.parent = parent;
    }
}
