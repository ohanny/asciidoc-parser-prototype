package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeEntries;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class BuildState {
    private AttributeEntries attributeEntries;

    private Deque<BlockBuilder> blockBuilders;
    private Deque<TextContainer> textContainers;
    private Deque<BlockContainer> blockContainers;

    private char[] currentBlockTitle;

    // computed refs : helps avoid duplicates
    private Map<String, Integer> refs;

    public static BuildState newInstance(AttributeEntries attributeEntries) {
        BuildState state = new BuildState();
        state.attributeEntries = attributeEntries;
        state.blockBuilders = new LinkedList<>();
        state.textContainers = new LinkedList<>();
        state.blockContainers = new LinkedList<>();
        state.refs = new HashMap<>();

        return state;
    }

    public AttributeEntries getAttributeEntries() {
        return attributeEntries;
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

    public void setCurrentBlockTitle(char[] currentBlockTitle) {
        this.currentBlockTitle = currentBlockTitle;
    }

    public String consumeBlockTitle() {
        String title = currentBlockTitle == null?null:new String(currentBlockTitle);
        currentBlockTitle = null;

        return title;
    }

}
