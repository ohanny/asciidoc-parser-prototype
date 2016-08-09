package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.peg.exception.LimitException;

public class LimitContext {
    private int limit;
    private int nbCharRead;

    private LimitContext parent;

    public static LimitContext newLimitContext(int limit) {
        final LimitContext ctx = new LimitContext();
        ctx.limit = limit;
        return ctx;
    }

    public static LimitContext withParent(int limit, LimitContext parent) {
        final LimitContext ctx = newLimitContext(limit);
        ctx.parent = parent;
        return ctx;
    }

    public void increment() {
        if (limit == -1) return; // limit disabled

        if (nbCharRead > limit) {
            throw new LimitException("Number of characters read exceeded : " + limit);
        }
        nbCharRead++;

        if (parent != null) {
            parent.increment();
        }
    }

    public void decrement(int nbChars) {
        if (this.parent != null) {
            decrement(nbChars);
        }
        nbCharRead -= nbChars;
    }

}
