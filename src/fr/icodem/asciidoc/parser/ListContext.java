package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.elements.AttributeList;

import java.util.ArrayList;
import java.util.List;

import static fr.icodem.asciidoc.parser.ListType.Ordered;
import static fr.icodem.asciidoc.parser.ListType.Unordered;

class ListContext {

    AttributeList attributeList;
    ListType type;
    int level;
    ListContext parent;
    ListContext root;

    List<ListItemContext> items = new ArrayList<>();
    ListItemContext lastItem;

    void addItem(ListItemContext item) {
        items.add(item);
        lastItem = item;
    }

    boolean isTypeUndefined() {
        return type == null;
    }

    boolean isUnordered() {
        return type == Unordered;
    }

    boolean isOrdered() {
        return type == Ordered;
    }

//    Stream<ListContext> flattened() { // TODO remove ?
//        return Stream.concat(Stream.of(this),
//                items.stream()
//                     .filter(li -> li.hasNestedList())
//                     .map(li -> li.nestedList)
//                     .flatMap(ListContext::flattened));
//    }

}
