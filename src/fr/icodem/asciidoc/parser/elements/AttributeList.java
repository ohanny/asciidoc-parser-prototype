package fr.icodem.asciidoc.parser.elements;

import java.util.Collections;
import java.util.List;

public class AttributeList {

    private List<Attribute> attributes;

    public AttributeList(List<Attribute> attributes) {
        this.attributes = Collections.unmodifiableList(attributes);
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return "AttributeList{" +
                "attributes=" + attributes +
                '}';
    }
}
