package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.peg.buffers.InputBuffer;
import fr.icodem.asciidoc.parser.peg.listeners.ParseTreeListener;
import fr.icodem.asciidoc.parser.peg.listeners.ParsingProcessListener;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class MatcherContext {
    private int marker; // used for reset and consume

    private InputBuffer input;
    private MatcherContext root;// peut être pas utilisé
    private MatcherContext parent;

    // The level is mainly useful when analyzing the context at debug time.
    // It is not used during the normal process.
    private int level;

    // children
    private MatcherContext subContext; // first child

    private MatcherContext prevContext; // previous child stored in sub-context
    private MatcherContext nextContext; // next child stored in sub-context
    private MatcherContext lastContext; // last child stored in sub-context

    private boolean canStartFlushing; // signifie : les enfants, vous pouvez demander le flush si vous matchez
    public void canStartFlushing() {
        if (!canStartFlushing && (parent == null || parent.canStartFlushing)) {
            canStartFlushing = true;
        }
    }
    public boolean isCanStartFlushing() {
        return canStartFlushing;
    }

    // listeners
    private ParseTreeListener listener;
    private ParsingProcessListener parsingProcessListener;

    private String nodeName;
    private boolean requestFlushingDone;
    private boolean flushed;// node only
    private boolean matched;// node only - utile ???

    public void matched() {
        matched = true;
        lastEndExtractPosition = input.getPosition();
    }


    private int lastStartExtractPosition = -1;
    private int lastEndExtractPosition = -1;

    private void childFlushStartNode(int childPosition) {
        //extract and notify
        if (listener != null) {
            notifyCharacters(lastStartExtractPosition, childPosition - 1);
        }
        lastStartExtractPosition = -1;
        //lastStartExtractPosition = 0;
        //System.out.println(nodeName + " (childFlushStartNode) = " + lastStartExtractPosition);
    }
    private void childFlushEndNode(int childPosition) {
        lastStartExtractPosition = childPosition + 1;
        //System.out.println(nodeName + " (childFlushEndNode) = " + lastStartExtractPosition);
    }

    private void notifyParentFlushStartNode() {
        MatcherContext ctx = findParentContextNode();
        if (ctx != null) {
            ctx.childFlushStartNode(lastStartExtractPosition);
        }
    }

    private void notifyParentFlushEndNode() {
        MatcherContext ctx = findParentContextNode();
        if (ctx != null) {
            ctx.childFlushEndNode(lastEndExtractPosition);
        }
    }

    private MatcherContext findParentContextNode() {
        if (parent != null) {
            if (parent.isNode()) {
                return parent;
            }
            else {
                return parent.findParentContextNode();
            }
        }
        return null;
    }

    public void requestFlushing() {
        if (requestFlushingDone) return;
        requestFlushingDone = true;

        if (canStartFlushing) {
            MatcherContext ctx = findContextNodeToFlush();
            if (ctx != null) {
                ctx.flush();
                input.consume(marker);
            }
        }
    }

    private MatcherContext findContextNodeToFlush() {
        MatcherContext ctx = null;
        if (this == root) {
            ctx = root;
        }
        else if (parent != null && parent.isNode() && parent.enterNodeNotified) {
            return parent;
        }
        else if (parent != null) {
            ctx = parent.findContextNodeToFlush();
        }

        return ctx;
    }

    private boolean enterNodeNotified;

    private NodeContext nodeContext;
    private NodeContext getNodeContext() {
        if (nodeContext == null) {
            nodeContext = new NodeContext(nodeName, this);
        }
        return nodeContext;
    }

    // émettre vers le listener ce qui n'a pas encore été émis
    private void flush() { // ne doit être invoqué que par node
        //System.out.println("FLUSH : " + nodeName);

        if (flushed) {
            return;
        }

        // un noeud
        if (isNode() && listener != null && !enterNodeNotified) {
            notifyParentFlushStartNode();
            listener.enterNode(getNodeContext());
            enterNodeNotified = true;
        }

        MatcherContext ctx = subContext;
        while (ctx != null) {
            ctx.flush();
            ctx = ctx.nextContext;
        }

        if (isNode() && matched) {
            if (listener != null) {
                //extract and notify
                notifyCharacters(lastStartExtractPosition, lastEndExtractPosition);

                listener.exitNode(nodeName);
                notifyParentFlushEndNode();
            }

            flushed = true;
        }

    }

    private void notifyCharacters(int start, int end) {
        if (end < start) return;
        char[] extracted = input.extract(start, end);
        if (extracted == null) return;
        listener.characters(extracted, start, end);
    }

    public MatcherContext(InputBuffer input,
                           ParseTreeListener listener,
                           ParsingProcessListener parsingProcessListener) {
        this(input, null, listener, parsingProcessListener);
    }

    private MatcherContext(InputBuffer input, MatcherContext parent,
                           ParseTreeListener listener, ParsingProcessListener parsingProcessListener) {
        this.input = input;
        this.parent = parent;
        if (parent == null) {
            this.root = this;
        } else {
            this.root = parent.root;
            this.level = parent.level + 1;
        }
        this.listener = listener;
        this.parsingProcessListener = parsingProcessListener;
        this.marker = -1;
    }

    public MatcherContext getSubContext() {
        if (subContext == null) {
            subContext = new MatcherContext(input, this, listener, parsingProcessListener);
            subContext.lastContext = subContext;
        } else {
            MatcherContext ctx = new MatcherContext(input, this, listener, parsingProcessListener);
            subContext.lastContext.nextContext = ctx;
            ctx.prevContext = subContext.lastContext;
            subContext.lastContext = ctx;
        }
        return subContext.lastContext;
    }


    private void removeLastSubContext() {
        if (subContext != null) {
            if (subContext.lastContext.prevContext == null) { // first child
                subContext = null;
            } else { // next children
                subContext.lastContext.prevContext.nextContext = null;
                subContext.lastContext = subContext.lastContext.prevContext;
            }
        }
    }

    public char getNextChar() {
        char c = input.getNextChar();
        parsingProcessListener.nextChar(c);
        return c;
    }

    public void mark() {
        marker = input.getPosition();
    }

    public void reset() {
        input.reset(marker);
        removeLastSubContext();
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
        this.lastStartExtractPosition = input.getPosition() + 1;
        //System.out.println("input.getPosition() => "  + input.getPosition() +  ", lastStartExtractPosition => " + lastStartExtractPosition);
    }

    public boolean isNode() {
        return nodeName != null;
    }

    public ParsingProcessListener getParsingProcessListener() {
        return parsingProcessListener;
    }

    public int getLevel() {
        return level;
    }

    public int getPosition() {
        return input.getPosition();
    }

    public int getPositionInLine() {
        return input.getPositionInLine();
    }

    private Map<String, Object> attributes;

    public void setAttribute(String name, Object value) {
        if (attributes == null) {
            attributes = new HashMap<>(5);
        }
        attributes.put(name, value);
    }
    public Object getAttribute(String name) {
        if (attributes == null) {
            if (parent == null) return null;
            return parent.getAttribute(name);
        }
        return attributes.get(name);
    }
    public boolean getBooleanAttribute(String name) {
        return (Boolean)getAttribute(name);
    }

    public void include(Reader reader) {
        input.include(reader);
    }
}
