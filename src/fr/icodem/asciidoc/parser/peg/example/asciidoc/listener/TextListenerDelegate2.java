package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;


import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Build a formatted text object
 */
public class TextListenerDelegate2 extends TextListenerDelegate {

    private enum ChunkType {
        Normal, Bold, Italic, Monospace, Subscript, Superscript, Mark
    }

    public static class Chunk {
        protected ChunkType type;
        protected MarkContext mark;

        public Chunk(ChunkType type) {
            this.type = type;
        }

        public ChunkType getType() {
            return type;
        }

        public void setType(ChunkType type) {
            this.type = type;
        }

        public MarkContext getMark() {
            return mark;
        }

        public void setMark(MarkContext mark) {
            this.mark = mark;
        }
    }

    public static class TextChunk extends Chunk {
        private String text;

        public TextChunk(ChunkType type) {
            super(type);
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class XRefChunk extends Chunk {
        private XRef xref;

        public XRefChunk(ChunkType type) {
            super(type);
        }

        public XRef getXref() {
            return xref;
        }

        public void setXref(XRef xref) {
            this.xref = xref;
        }
    }

    public static class CompositeChunk extends Chunk {
        private Deque<Chunk> chunks;

        public CompositeChunk(ChunkType type) {
            super(type);
            chunks = new LinkedList<>();
        }

        public Deque<Chunk> getChunks() {
            return chunks;
        }

        public void addFirst(Chunk chunk) {
            chunks.addFirst(chunk);
        }

        public Chunk getFirst() {
            return chunks.getFirst();
        }

        public void addAll(Collection<Chunk> chunks) {
            this.chunks.addAll(chunks);
        }
    }

    public static class FormattedText {
        private Chunk root;

        public FormattedText(Chunk root) {
            this.root = root;
        }

        public Chunk getRoot() {
            return root;
        }
    }

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
            chunk = new TextChunk(type);
            chunk.setMark(mark);
            push(chunk);
        }
    }

    public FormattedText getFormattedText() {
        if (stack.size() == 1) {
            return new FormattedText(pop());
        } else {
            Chunk first = stack.getFirst();
            CompositeChunk composite = new CompositeChunk(first.type);
            first.type = ChunkType.Normal;
            composite.addAll(stack);
            stack.clear();

            return new FormattedText(composite);
        }
    }

    @Override
    protected void writeText(String text) {
        Chunk last = peek();
        TextChunk chunk = null;
        if (last instanceof TextChunk && ((TextChunk) last).text == null) {
            chunk = (TextChunk) last;
        }
        if (chunk == null) {
            chunk = new TextChunk(ChunkType.Normal);
            push(chunk);
        }
        chunk.setText(text);
    }

    // markup methods
    @Override
    public void enterBold() {
        peekOrAddNew(ChunkType.Bold);
    }

    @Override
    public void exitBold() {
        exit(ChunkType.Bold);
    }

    private void exit(ChunkType type) {
        if (!peek().type.equals(type)) {
            CompositeChunk composite = new CompositeChunk(type);
            do {
                composite.addFirst(pop());
            } while (composite.getFirst().type != type);
            composite.getFirst().type = ChunkType.Normal;
            push(composite);
        }
    }

    @Override
    public void enterItalic() {
        peekOrAddNew(ChunkType.Italic);
    }

    @Override
    public void exitItalic() {
        exit(ChunkType.Italic);
    }

    @Override
    public void enterSubscript() {
        peekOrAddNew(ChunkType.Subscript);
    }

    @Override
    public void exitSubscript() {
        exit(ChunkType.Subscript);
    }

    @Override
    public void enterSuperscript() {
        peekOrAddNew(ChunkType.Superscript);
    }

    @Override
    public void exitSuperscript() {
        exit(ChunkType.Superscript);
    }

    @Override
    public void enterMonospace() {
        peekOrAddNew(ChunkType.Monospace);
    }

    @Override
    public void exitMonospace() {
        exit(ChunkType.Monospace);
    }

    public void enterMark() {
        super.enterMark();
        peekOrAddNew(ChunkType.Mark, currentMark);
    }

    public void exitMark() {
        super.exitMark();
        exit(ChunkType.Mark);
    }

    public void exitXRef() {
        XRefChunk chunk = new XRefChunk(ChunkType.Normal);
        chunk.setXref(XRef.of(currentXRef.value, currentXRef.label, currentXRef.internal));
        push(chunk);
        super.exitXRef();
    }
}
