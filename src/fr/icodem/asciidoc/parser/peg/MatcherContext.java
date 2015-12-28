package fr.icodem.asciidoc.parser.peg;

public class MatcherContext {
    private int marker;
    private boolean shouldReset;
    private boolean shouldResetIfDirty; // est capable de faire un reset, intercepte dirty
    private boolean dirty; // marque le fait que l'input buffer est dirty

    private InputBuffer input;
    private MatcherContext parent;

    public MatcherContext(InputBuffer input) {
        this(input, null);
    }

    private MatcherContext(InputBuffer input, MatcherContext parent) {
        this.input = input;
        this.parent = parent;
        clearMarker();
    }

    public MatcherContext getSubContext() {
        return new MatcherContext(input, this);
    }

    public char getNextChar() {
        return input.getNextChar();
    }

    public void mark() {
        if (marker != -1) input.release(marker);
        marker = input.mark();
    }

    public void release() {
        if (marker != -1) {
            input.release(marker);
            clearMarker();
        }
    }

    public void reset() {
        if (dirty) {
            input.reset(marker);
        }
    }

    public void consume() {
        input.consume();
    }

    public void clearMarker() {
        marker = -1;
    }

    public boolean shouldReset() {
        return shouldReset;
    }

    public void resetRequired() {
        this.shouldReset = true;
    }

    public void shouldResetIfDirty() {
        shouldResetIfDirty = true;
    }

    public void dirty() {
        if (shouldResetIfDirty) {
            dirty = true;
        }
        else if (parent != null) {
            parent.dirty();
        }
    }

}
