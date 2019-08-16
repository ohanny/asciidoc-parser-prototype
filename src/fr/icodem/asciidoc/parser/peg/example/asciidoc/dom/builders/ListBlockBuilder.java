package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListBlockBuilder implements BlockBuilder {

    public enum Type { Ordered, Unordered }

    private int level;
    private int bullets;
    private int itemCount; // TODO replace with items.size() ?
    private Type type;
    private ListBlockBuilder parent;
    private ListBlockBuilder root;
    private ListBlockBuilder current;
    private AttributeList attList;
    //private FormattedText title;
    private String title;

    private List<ListItemBuilder> items;

    @Override
    public ListBlock build() {
        List<ListItem> items = this.items
                .stream()
                .map(ListItemBuilder::build)
                .collect(Collectors.toList());

        ElementType type = null;
        switch (this.type) {
            case Unordered:
                type = ElementType.UnorderedList;
                break;

            case Ordered:
                type = ElementType.OrderedList;
                break;
        }

        return ListBlock.of(attList, Title.of(title), type, items);
    }


//    public static ListBlockBuilder newBuilder() {
//        return new ListBlockBuilder();
//    }

    public static ListBlockBuilder root(String title) {
        ListBlockBuilder builder = new ListBlockBuilder();
        builder.title = title;
        builder.root = builder;
        builder.current = builder;
        builder.items = new ArrayList<>();
        builder.level = 1;

        return builder;
    }

    public static ListBlockBuilder withParent(ListBlockBuilder parent) {
        ListBlockBuilder builder = new ListBlockBuilder();
        builder.items = new ArrayList<>();

        if (parent != null) {
            builder.parent = parent;
            builder.level = parent.level + 1;
        } else { // TODO check case null
            builder.level = 1;
        }
        return builder;
    }

    public ListBlockBuilder findParentListWithTypeAndLevel(ListBlockBuilder.Type type, int level) {
        ListBlockBuilder parent = this.parent;
        while (parent != null) {
            if (parent.type.equals(type) && parent.level == level) {
                return parent;
            }
        }

        return null;
    }

    public ListItemBuilder newListItem(int times, int dots, AttributeList attList) {

        if (times  > 0) {
            if (current.isUnordered()) {
                if (times == current.bullets) {

                } else if (times > current.bullets) {
                    current = withParent(current);
                    current.bullets = times;
                    addCurrentToParentBlock();
                } else if (times < current.bullets) {
                    while (times < current.bullets && current.level > 1) {
                        //handler.endUnorderedList(currentList.level);
                        current = current.parent;
                    }
                }
            } else if (current.isOrdered()) {
                if (times > current.bullets) {
                    current = withParent(current);
                    current.bullets = times;
                    addCurrentToParentBlock();
                } else {
                    // find parent with same type and level
                    ListBlockBuilder ancestorWithSameLevel = current.findParentListWithTypeAndLevel(Type.Unordered, times);
                    if (ancestorWithSameLevel == null) {
                        current = withParent(current);
                        current.bullets = times;
                        addCurrentToParentBlock();
                    } else {
                        ListBlockBuilder parent = current.parent;
                        while (current != parent) {
                            //handler.endUnorderedList(currentList.level);
                            current = current.parent;
                        }
                    }
                }
            }
        } else if (dots > 0) {
            if (current.isOrdered()) {
                if (dots == current.bullets) {

                } else if (dots > current.bullets) {
                    current = withParent(current);
                    current.bullets = dots;
                    addCurrentToParentBlock();
                } else if (dots < current.bullets) {
                    while (dots < current.bullets && current.level > 1) {
                        // handler.endOrderedList(current.level);
                        current = current.parent;
                    }
                }
            } else if (current.isUnordered()) {
                if (dots > current.bullets) {
                    current = withParent(current);
                    current.bullets = dots;
                    addCurrentToParentBlock();
                } else {
                    // find parent with same type and level
                    ListBlockBuilder ancestorWithSameLevel = current.findParentListWithTypeAndLevel(Type.Ordered, dots);
                    if (ancestorWithSameLevel == null) {
                        current = withParent(current);
                        current.bullets = dots;
                        addCurrentToParentBlock();
                    } else {
                        ListBlockBuilder parent = current.parent;
                        while (current != parent) {
                            //handler.endUnorderedList(currentList.level);
                            current = current.parent;
                        }
                    }
                }
            }
        }


        if (current.type == null) {
            if (times > 0) {
                current.type = Type.Unordered;
                current.bullets = times;
                current.attList = attList;
                //current.attList = consumeAttList();
                //handler.startUnorderedList(currentList.level, currentList.attList, currentList.title);
            } else if (dots > 0) {
                current.type = Type.Ordered;
                current.bullets = dots;
                current.attList = attList;
                //current.attList = consumeAttList();
                //handler.startOrderedList(currentList.level, currentList.attList);
            }
        }


        ListItemBuilder builder = ListItemBuilder.newBuilder();
        current.items.add(builder);

        //handler.startListItem(currentList.level, ++currentList.itemCount, currentList.attList);
        //clearAttList();

        return builder;
    }

    private void addCurrentToParentBlock() {
        ListItemBuilder lastListItem = current.parent.items.get(current.parent.items.size() - 1);
        lastListItem.addBlock(current);
    }

    private boolean isOrdered() {
        return type == Type.Ordered;
    }

    private boolean isUnordered() {
        return type == Type.Unordered;
    }

//    public int getLevel() {
//        return level;
//    }
//
//    public void setLevel(int level) {
//        this.level = level;
//    }
//
//    public int getBullets() {
//        return bullets;
//    }
//
//    public void setBullets(int bullets) {
//        this.bullets = bullets;
//    }
//
//    public int getItemCount() {
//        return itemCount;
//    }
//
//    public void setItemCount(int itemCount) {
//        this.itemCount = itemCount;
//    }
//
//    public ListBlockBuilder getParent() {
//        return parent;
//    }
//
//    public void setParent(ListBlockBuilder parent) {
//        this.parent = parent;
//    }
//
//    public ListBlockBuilder getRoot() {
//        return root;
//    }
//
//    public void setRoot(ListBlockBuilder root) {
//        this.root = root;
//    }
//
//    public AttributeList getAttList() {
//        return attList;
//    }
//
//    public void setAttList(AttributeList attList) {
//        this.attList = attList;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
}
