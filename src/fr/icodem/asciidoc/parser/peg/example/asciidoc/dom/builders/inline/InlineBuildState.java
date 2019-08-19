package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.inline;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeEntries;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class InlineBuildState {
    private AttributeEntries attributeEntries;

    private Deque<InlineNodeBuilder> stack;
    private List<InlineNodeBuilder> builders; // root builders

    public static InlineBuildState newInstance(AttributeEntries attributeEntries) {
        InlineBuildState state = new InlineBuildState();
        state.attributeEntries = attributeEntries;
        state.stack = new LinkedList<>();
        state.builders = new ArrayList<>();

        return state;
    }

    public AttributeEntries getAttributeEntries() {
        return attributeEntries;
    }

    public void pushNode(InlineNodeBuilder builder) {
        addNode(builder);
        stack.addLast(builder);
    }

    public void popNode() {
        stack.removeLast();
    }

//    public <T extends InlineNodeBuilder> T popNode() {
//        return (T) stack.removeLast();
//    }
//
    public <T extends InlineNodeBuilder> T peekNode() {
        return (T) stack.peekLast();
    }

    public void addNode(InlineNodeBuilder builder) {
        if (stack.isEmpty()) {
            builders.add(builder);
        } else {
            peekNode().addChild(builder);
        }
    }

    public List<InlineNodeBuilder> getBuilders() {
        return builders;
    }
}
