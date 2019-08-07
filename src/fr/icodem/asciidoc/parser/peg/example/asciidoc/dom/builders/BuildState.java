package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class BuildState {
    private Deque<BlockBuilder> blockBuilders;
    //private TextBlockBuilder currentTextBlockBuilder;
    private Deque<TextBlockBuilder> textBlockBuilders; // TODO replace with interface TextContent
    private Deque<BlockContainer> blockContainers;

    private Map<String, Integer> refs = new HashMap<>();

    public static BuildState newInstance() {
        BuildState state = new BuildState();
        state.blockBuilders = new LinkedList<>();
        state.textBlockBuilders = new LinkedList<>();
        state.blockContainers = new LinkedList<>();

        return state;
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


}
