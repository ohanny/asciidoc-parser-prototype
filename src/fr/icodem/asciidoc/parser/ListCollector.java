package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.elements.AbstractList;
import fr.icodem.asciidoc.parser.elements.ListItem;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.stream.Collector.Characteristics.IDENTITY_FINISH;

class ListCollector /*implements Collector<ListItemContext, MutableList, AbstractList>*/  {
/*

    @Override
    public Supplier<MutableList> supplier() {
        return MutableList::new;
    }

    @Override
    public BiConsumer<MutableList, ListItemContext> accumulator() {
        return this::addItem;
    }

    private void addItem(MutableList acc, ListItemContext liCtx) {
        ListItem item = new ListItem(liCtx.text);
        acc.addListItem(item);
    }

    @Override
    public BinaryOperator<MutableList> combiner() {
        return null;
    }

    @Override
    public Function<MutableList, AbstractList> finisher() {
        //return Function.identity();
        return acc -> acc;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(IDENTITY_FINISH));
    }

*/

}
