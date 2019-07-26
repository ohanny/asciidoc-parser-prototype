package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.builders;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Attribute;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeList;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class AttributeListBuilder {
    protected Deque<AttributeBuilder> builders;

    public static AttributeListBuilder newBuilder() {
        AttributeListBuilder builder = new AttributeListBuilder();
        builder.builders = new LinkedList<>();

        return builder;
    }

    public AttributeList build() {
        List<Attribute> attributes = builders.stream()
                                             .map(AttributeBuilder::build)
                                             .collect(Collectors.toList());
        return AttributeList.of(attributes);
    }

    public void addIdAttribute() {
        builders.add(AttributeBuilder.idAttributeBuilder());
    }

    public void addRoleAttribute() {
        builders.add(AttributeBuilder.roleAttributeBuilder());
    }

    public void addOptionAttribute() {
        builders.add(AttributeBuilder.optionAttributeBuilder());
    }

    public void addPositionalAttribute() {
        builders.add(AttributeBuilder.positionelAttributeBuilder());
    }

    public void addNamedAttribute() {
        builders.add(AttributeBuilder.namedAttributeBuilder());
    }

    public void setAttributeName(String name) {
        builders.peekLast().setName(name);
    }

    public void setAttributeValue(String value) {
        builders.peekLast().setValue(value);
    }

    public AttributeList consume() {
        if (builders.isEmpty()) return null;

        AttributeList attList = build();
        builders.clear();

        return attList;
    }

}
