package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;


import java.util.Deque;
import java.util.LinkedList;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.FormattedText.ChunkType.*;
import static fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.FormattedText.*;

/**
 * Build a formatted text object
 */
public class TextListenerDelegate2 extends TextListenerDelegate {


    private Deque<Chunk> stack;

    private AsciidocHandler handler;

    public TextListenerDelegate2(AsciidocHandler handler, AttributeEntries attributeEntries) {
        super(attributeEntries);
        this.handler = handler;
        this.stack = new LinkedList<>();
    }

    private Chunk peek() {
        return stack.peekLast();
    }

    private Chunk pop() {
        return stack.pollLast();
    }

    private void push(Chunk chunk) {
        stack.addLast(chunk);
    }

    private void peekOrAddNew(ChunkType type) {
        peekOrAddNew(type, null);
    }

    private void peekOrAddNew(ChunkType type, MarkContext mark) {
        Chunk chunk = peek();
        if (chunk == null || !chunk.type.equals(type)) {
            chunk = textChunk(type);
            chunk.setMark(mark);
            push(chunk);
        }
    }

    public FormattedText getFormattedText() {
        if (stack.size() == 1) {
            return new FormattedText(pop());
        } else {
            Chunk first = stack.getFirst();
            CompositeChunk composite = compositeChunk(first.type);
            first.type = Normal;
            composite.addAll(stack);
            stack.clear();

            return new FormattedText(composite);
        }
    }

    @Override
    protected void writeText(String text) {
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
    @Override
    public void enterBold() {
        peekOrAddNew(Bold);
    }

    @Override
    public void exitBold() {
        exit(Bold);
    }

    private void exit(ChunkType type) {
        if (!peek().type.equals(type)) {
            CompositeChunk composite = compositeChunk(type);
            do {
                composite.addFirst(pop());
            } while (composite.getFirst().type != type);
            composite.getFirst().normal();
            push(composite);
        }
    }

    @Override
    public void enterItalic() {
        peekOrAddNew(Italic);
    }

    @Override
    public void exitItalic() {
        exit(Italic);
    }

    @Override
    public void enterSubscript() {
        peekOrAddNew(Subscript);
    }

    @Override
    public void exitSubscript() {
        exit(Subscript);
    }

    @Override
    public void enterSuperscript() {
        peekOrAddNew(Superscript);
    }

    @Override
    public void exitSuperscript() {
        exit(Superscript);
    }

    @Override
    public void enterMonospace() {
        peekOrAddNew(Monospace);
    }

    @Override
    public void exitMonospace() {
        exit(Monospace);
    }

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
}
