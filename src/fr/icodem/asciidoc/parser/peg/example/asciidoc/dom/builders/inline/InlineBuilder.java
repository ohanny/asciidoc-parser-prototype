package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders.inline;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.InlineListNode;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text.InlineNode;

import java.util.List;
import java.util.stream.Collectors;

public class InlineBuilder {

    private InlineBuildState state;

    public static InlineBuilder newBuilder(InlineBuildState state) {
        InlineBuilder builder = new InlineBuilder();
        builder.state = state;

        return builder;
    }

    public InlineNode build() {
        List<InlineNode> nodes = state.getBuilders()
                .stream()
                .map(InlineNodeBuilder::build)
                .collect(Collectors.toList());

        if (nodes.size() == 1) {
            return nodes.get(0);
        }

        return InlineListNode.of(nodes);
    }

    /*
    private Deque<FormattedText.Chunk> stack;

    public static InlineBuilder newBuilder() {
        InlineBuilder builder = new InlineBuilder();
        builder.stack = new LinkedList<>();

        return builder;
    }

    public FormattedText build() {
        if (stack.size() == 1) {
            return new FormattedText(pop());
        } else {
            FormattedText.Chunk first = stack.getFirst();
            FormattedText.CompositeChunk composite = compositeChunk(first.getType());
            first.setType(Normal);
            composite.addAll(stack);
            stack.clear();

            return new FormattedText(composite);
        }
    }

    private FormattedText.Chunk peek() {
        return stack.peekLast();
    }

    private FormattedText.Chunk pop() {
        return stack.pollLast();
    }

    private void push(FormattedText.Chunk chunk) {
        stack.addLast(chunk);
    }

    private void peekOrAddNew(FormattedText.ChunkType type) {
        peekOrAddNew(type, null);
    }

    private void peekOrAddNew(FormattedText.ChunkType type, AttributeList mark) {
        FormattedText.Chunk chunk = peek();
        if (chunk == null || !chunk.getType().equals(type)) {
            chunk = textChunk(type);
            chunk.setMark(mark);
            push(chunk);
        }
    }

    public void writeText(String text) {
        Chunk last = peek();
        TextChunk chunk = null;
        if (last instanceof TextChunk && ((TextChunk) last).getText() == null) {
            chunk = (TextChunk) last;
        }
        if (chunk == null) {
            chunk = normalTextChunk();
            push(chunk);
        }
        chunk.setText(text);
    }

    // markup methods
    public void enterBold() {
        peekOrAddNew(Bold);
    }

    public void exitBold() {
        exit(Bold);
    }

    private void exit(ChunkType type) {
        if (!peek().getType().equals(type)) {
            CompositeChunk composite = compositeChunk(type);
            do {
                composite.addFirst(pop());
            } while (composite.getFirst().getType() != type);
            composite.getFirst().normal();
            push(composite);
        }
    }

    public void enterItalic() {
        peekOrAddNew(Italic);
    }

    public void exitItalic() {
        exit(Italic);
    }

    public void enterSubscript() {
        peekOrAddNew(Subscript);
    }

    public void exitSubscript() {
        exit(Subscript);
    }

    public void enterSuperscript() {
        peekOrAddNew(Superscript);
    }

    public void exitSuperscript() {
        exit(Superscript);
    }

    public void enterMonospace() {
        peekOrAddNew(Monospace);
    }

    public void exitMonospace() {
        exit(Monospace);
    }

    /*
    public void enterMark() {
        super.enterMark();
        peekOrAddNew(Mark, currentMark);
    }


    public void exitMark() {
        super.exitMark();
        exit(Mark);
    }

    public void exitXRef() {
        XRefChunk chunk = xrefChunk(Normal);
        chunk.setXref(XRef.of(currentXRef.value, currentXRef.label, currentXRef.internal));
        push(chunk);
        super.exitXRef();
    }

     */

}
