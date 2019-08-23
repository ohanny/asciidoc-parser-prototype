package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.block;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.AttributeListBuilder;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeEntries;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;

import java.util.*;

public class BlockBuildState {
    private AttributeEntries attributeEntries;
    private AttributeListBuilder attributeListBuilder;

    private Deque<BlockBuilder> blockBuilders;
    private Deque<TextContainer> textContainers;
    private Deque<BlockContainer> blockContainers;

    private List<TextContainer> textToParseList;

    private TitleBuilder currentBlockTitle;

    // computed refs : helps avoid duplicates
    private Map<String, Integer> refs;

    public static BlockBuildState newInstance(AttributeEntries attributeEntries) {
        BlockBuildState state = new BlockBuildState();
        state.attributeListBuilder = AttributeListBuilder.newBuilder();
        state.attributeEntries = attributeEntries;
        state.blockBuilders = new LinkedList<>();
        state.textContainers = new LinkedList<>();
        state.blockContainers = new LinkedList<>();
        state.textToParseList = new ArrayList<>();
        state.refs = new HashMap<>();

        return state;
    }

    public AttributeEntries getAttributeEntries() {
        return attributeEntries;
    }

    public AttributeListBuilder getAttributeListBuilder() {
        return attributeListBuilder;
    }

    public AttributeList consumeAttributeList() {
        return attributeListBuilder.consume();
    }

    public void pushBlock(BlockBuilder builder) {
        blockBuilders.addLast(builder);
    }

    public <T extends BlockBuilder> T popBlock() {
        return (T)blockBuilders.removeLast();
    }

    public <T extends BlockBuilder> T peekBlock() {
        return (T)blockBuilders.peekLast();
    }

    public void pushText(String text) {
        TextContainer container = textContainers.peekLast();
        if (container != null) {
            container.setText(text);
        }
    }

    public void pushBlockToContainer(BlockBuilder builder) {
        blockContainers.peekLast().addBlock(builder);
    }

    public void pushBlockContainer(BlockContainer container) {
        blockContainers.addLast(container);
    }

    public void popBlockContainer() {
        blockContainers.removeLast();
    }

    public void pushTextContainer(TextContainer container) {// TODO rename
        textContainers.addLast(container);
        textToParseList.add(container);
    }

    public void popTextContainer() {
        textContainers.removeLast();
    }

    public String textToRef(String text) {
        String ref = text.toLowerCase().replaceAll("\\s+", "_");
        int count = refs.getOrDefault(ref, 0);
        refs.put(ref, ++count);
        if (count > 1) {
            ref = ref + "_" + count;
        }
        return ref;
    }

    public void setCurrentBlockTitle(TitleBuilder title) {
        this.currentBlockTitle = title;
        //textToParseList.add(title);
        pushTextToParse(title);
    }

    public TitleBuilder consumeBlockTitle() {
        TitleBuilder title = currentBlockTitle;
        currentBlockTitle = null;
        return title;
    }


    public void pushTextToParse(TextContainer text) {
        textToParseList.add(text);
    }

    public List<TextContainer> getTextToParseList() {
        return textToParseList;
    }
}
