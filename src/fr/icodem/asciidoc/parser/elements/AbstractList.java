package fr.icodem.asciidoc.parser.elements;

import java.util.List;
import java.util.stream.Collectors;

public class AbstractList extends Block {
    protected List<ListItem> items;
    protected int level;

    public AbstractList(AttributeList attList, List<ListItem> items, int level) {
        super(attList);
        this.items = items;
        this.level = level;
    }

    public List<ListItem> getItems() {
        return items;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringList(this, sb, 1);

        return sb.toString();
    }

    private void toStringList(AbstractList list, StringBuilder sb, int level) {
        String tabs = "";
        for (int i = 1; i < level ; i++) {
            tabs += "\t";
        }

        sb.append(tabs + "[" + list.getClass().getSimpleName() + " {level=" + level + ", type=" + list.getFirstPositionalAttribute() + "}]\r\n");
        for (ListItem li: list.items) {
            sb.append(tabs + "# " + li.text + "\r\n");
            if (li.nestedList != null) {
                toStringList(li.nestedList, sb, ++level);
                level--;
            }
            if (li.blocks != null && !li.blocks.isEmpty()) {
                sb.append(tabs + "  blocks : [");
                String blocks = li.blocks.stream()
                        .map(b -> b.getClass().getSimpleName())
                        .collect(Collectors.joining(", "));
                sb.append(blocks).append("]\r\n");
            }

        }
    }

}
