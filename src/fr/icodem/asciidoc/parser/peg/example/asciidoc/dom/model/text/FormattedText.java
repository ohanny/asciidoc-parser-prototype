package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.text;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.TextListenerDelegate;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.XRef;

import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;

public class FormattedText {
    public enum ChunkType {
        Normal, Bold, Italic, Monospace, Subscript, Superscript, Mark
    }

    public static TextChunk textChunk(ChunkType type) {
        return new TextChunk(type);
    }

    public static CompositeChunk compositeChunk(ChunkType type) {
        return new CompositeChunk(type);
    }

    public static XRefChunk xrefChunk(ChunkType type) {
        return new XRefChunk(type);
    }

    public static TextChunk normalTextChunk() {
        return new TextChunk(ChunkType.Normal);
    }

    public static class Chunk {
        protected ChunkType type;
        protected TextListenerDelegate.MarkContext mark;

        public Chunk(ChunkType type) {
            this.type = type;
        }

        <T extends Chunk> T  normal() {
            this.type = ChunkType.Normal;
            return (T)this;
        }

        <T extends Chunk> T bold() {
            this.type = ChunkType.Bold;
            return (T)this;
        }

        <T extends Chunk> T  italic() {
            this.type = ChunkType.Italic;
            return (T)this;
        }

        <T extends Chunk> T  monospace() {
            this.type = ChunkType.Monospace;
            return (T)this;
        }

        public ChunkType getType() {
            return type;
        }

        public void setType(ChunkType type) {
            this.type = type;
        }

        public TextListenerDelegate.MarkContext getMark() {
            return mark;
        }

        public void setMark(TextListenerDelegate.MarkContext mark) {
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

    private Chunk root;

    public FormattedText(Chunk root) {
        this.root = root;
    }

    public Chunk getRoot() {
        return root;
    }


}
