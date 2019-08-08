package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeEntries;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class BuildState {
    private AttributeEntries attributeEntries;

    private Deque<BlockBuilder> blockBuilders;
    //private TextBlockBuilder currentTextBlockBuilder;
    private Deque<TextBlockBuilder> textBlockBuilders; // TODO replace with interface TextContent
    private Deque<BlockContainer> blockContainers;

    private char[] currentBlockTitle;

    private Map<String, Integer> refs = new HashMap<>();

    public static BuildState newInstance(AttributeEntries attributeEntries) {
        BuildState state = new BuildState();
        state.attributeEntries = attributeEntries;
        state.blockBuilders = new LinkedList<>();
        state.textBlockBuilders = new LinkedList<>();
        state.blockContainers = new LinkedList<>();

        return state;
    }

    public AttributeEntries getAttributeEntries() {
        return attributeEntries;
    }

    public void pushBlock(BlockBuilder builder) {
        blockBuilders.addLast(builder);
    }

    public void popBlock() {
        blockBuilders.removeLast();
    }

    public void pushText(String text) {
        TextBlockBuilder builder = textBlockBuilders.peekLast();
        if (builder != null) {
            builder.setText(text);
        }
    }

    public void pushBlockToContainer(BlockBuilder builder) {
        blockContainers.peekLast().addBlock(builder);
    }

    public void pushContainer(BlockContainer container) {
        blockContainers.addLast(container);
    }

    public void popContainer() {
        blockContainers.removeLast();
    }

//    public BlockContainer peekContainer() {
//        return blockContainers.peekLast();
//    }

    public void pushTextBlock(TextBlockBuilder block) {
        textBlockBuilders.addLast(block);
    }

    public TextBlockBuilder popTextBlock() {
        return textBlockBuilders.removeLast();
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

    public void setCurrentBlockTitle(char[] currentBlockTitle) {
        this.currentBlockTitle = currentBlockTitle;
    }

    public String consumeBlockTitle() {
        String title = currentBlockTitle == null?null:new String(currentBlockTitle);
        currentBlockTitle = null;

        return title;
    }

}
