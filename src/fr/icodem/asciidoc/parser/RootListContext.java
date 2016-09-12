package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.elements.AttributeList;
import fr.icodem.asciidoc.parser.elements.Block;
import fr.icodem.asciidoc.parser.elements.Text;

import java.util.stream.Collectors;

import static fr.icodem.asciidoc.parser.ListType.Ordered;
import static fr.icodem.asciidoc.parser.ListType.Unordered;

class RootListContext {

    ListContext listContext;

    void enterList() {
        listContext = new ListContext();
        listContext.root = listContext;
    }

    void exitList() {
        listContext = listContext.root;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringList(listContext, sb, 1);

        return sb.toString();
    }

    private void toStringList(ListContext ctx, StringBuilder sb, int level) {
        String tabs = "";
        for (int i = 1; i < level ; i++) {
            tabs += "\t";
        }

        sb.append(tabs + ctx.level + " - " + ctx.type + "\r\n");
        for (ListItemContext liCtx: ctx.items) {
            sb.append(tabs + "# " + liCtx.text + "\r\n");
            if (liCtx.nestedList != null) {
                toStringList(liCtx.nestedList, sb, ++level);
                level--;
            }
            if (liCtx.blocks != null && !liCtx.blocks.isEmpty()) {
                sb.append(tabs + "  blocks : [");
                String blocks = liCtx.blocks.stream()
                        .map(b -> b.getClass().getSimpleName())
                        .collect(Collectors.joining(", "));
                sb.append(blocks).append("]\r\n");
            }
        }
    }

    void addItem(String text, int times, int dots, AttributeList attList) {
        addItem(Text.withValue(text), times, dots, attList);
    }

    void addItem(Text text, int times, int dots, AttributeList attList) {
        ListItemContext liCtx = new ListItemContext();
        liCtx.text = text;

        // list item type and level
        ListType type = Unordered;
        int level = Math.max(times, 5);
        if (times == 0 && dots > 0) {
            type = Ordered;
            level = Math.max(dots, 5);
        }

        // determine current node
        if (listContext.isTypeUndefined()) { // root
            listContext.level = level;
            listContext.type = type;
            listContext.attributeList = attList;
        }
        else if (listContext.type != type || level > listContext.level) { // nested list
            listContext = newListContext(level, type, attList);
        }
        else if (level < listContext.level) {
            listContext = getParentListContext(level, type, attList);
        }

        listContext.addItem(liCtx);

    }

    Text getLastItemText() {
        return listContext.lastItem.text;
    }

    void addBlockToLastListItem(Block block) {
        listContext.lastItem.addBlock(block);
    }

    private ListContext newListContext(int level, ListType type, AttributeList attributeList) {
        ListContext ctx = new ListContext();
        ctx.attributeList = attributeList;
        ctx.type = type;
        ctx.level = level;
        ctx.root = listContext.root;
        ctx.parent = listContext;

        listContext.lastItem.nestedList = ctx;

        return ctx;
    }

    private ListContext getParentListContext(int level, ListType type, AttributeList attList) {
        // find parent list context corresponding to level
        ListContext ctx = listContext.parent;
        while (ctx != null && (level != ctx.level || type != ctx.type)) {
            ctx = ctx.parent;
        }

        // fallback if list item level is not consistent
        //if (parentListCtx == null) parentListCtx = listContext.root;
        if (ctx == null) {
            ctx = newListContext(level, type, attList);
        }

        return ctx;
    }

//    Stream<ListContext> flattened() {// TODO remove ?
//        return listContext.flattened();
//    }
}
