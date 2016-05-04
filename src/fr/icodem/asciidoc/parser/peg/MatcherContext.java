package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.peg.action.ActionContext;
import fr.icodem.asciidoc.parser.peg.buffers.InputBuffer;
import fr.icodem.asciidoc.parser.peg.listeners.ParseTreeListener;
import fr.icodem.asciidoc.parser.peg.listeners.ParsingProcessListener;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class MatcherContext {
    private int marker; // used for reset

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


    private int lastStartExtractPosition = -1; // marque la position de début du noeud ou la position après la fin d'un noeud enfant
    private int lastEndExtractPosition = -1; // marque la position en fin de noeud après le matching

    // un noeud enfant démarre : on extrait la partie déjà lue par le parent
    private void childFlushStartNode(int childPosition) {
        //extract and notify
        notifyCharacters(lastStartExtractPosition, childPosition - 1);
        lastStartExtractPosition = -1; // reset position début extraction car c'est l'enfant qui extrait maintenant
    }

    // le noeud enfant vient de terminer son flushing : on marque
    // un démarrage possible du flushing pour le parent
    private void childFlushEndNode(int childPosition) {
        lastStartExtractPosition = childPosition + 1;
    }

    // indique au parent que ce noeud enfant vient de commencer
    private void notifyParentFlushStartNode() {
        MatcherContext ctx = findParentContextNode();
        if (ctx != null) {
            ctx.childFlushStartNode(lastStartExtractPosition);
        }
    }

    // indique au parent que ce noeud enfant vient de se terminer
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

        // if current context is not a node context, we
        // request flushing starting from the parent node
        if (!isNode()) {
            MatcherContext ctx = findParentContextNode();
            if (ctx != null) {
                ctx.tryStartFlushing();
            }
            return;
        }

        tryStartFlushing();
    }

    private void tryStartFlushing() {
        // current context is a node, we can request flushing
        if (canStartFlushing) {
            MatcherContext ctx = findContextNodeToFlush();
            if (ctx != null) {
                ctx.flush();
                input.consume();// marker = avant début séquence - après fin node
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

    private ActionContext actionContext;
    public ActionContext getActionContext() {
        if (actionContext == null) {
            actionContext = new ActionContext(this);
        }
        return actionContext;
    }

    // émettre vers le listener ce qui n'a pas encore été émis
    private void flush() { // ne doit être invoqué que par node
        if (flushed) {
            return;
        }

        // un noeud
        if (isNode() && listener != null && !enterNodeNotified) { // TODO remove listener != null ?
            notifyParentFlushStartNode();
            listener.enterNode(getNodeContext());
            enterNodeNotified = true;
        }

        // on flush tous les enfants
        MatcherContext ctx = subContext;
        while (ctx != null) {
            ctx.flush();
            ctx = ctx.nextContext;
        }

        // un noeud vient de matcher : on extrait + notif fin de noeud
        if (isNode() && matched) {
            //extract and notify
            notifyCharacters(lastStartExtractPosition, lastEndExtractPosition);

            listener.exitNode(nodeName);
            notifyParentFlushEndNode();

            flushed = true;
        }

    }

    private void notifyCharacters(int start, int end) {
        if (end < start) return;
        char[] extracted = input.extract(start, end);
        if (extracted == null) return;
        listener.characters(extracted, start, end);
    }

    // extract data silently
    public char[] extract(int start, int end) {
        return input.extractSilently(start, end);
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

    public void include(Object source) {
        input.include(source);
    }
}
