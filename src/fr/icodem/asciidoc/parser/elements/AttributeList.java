package fr.icodem.asciidoc.parser.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Deprecated
public class AttributeList {

    private String id;

    private String firstPositionalAttribute;
    private List<String> positionalAttributes;
    private Map<String, Attribute> attributes;

    public AttributeList(List<Attribute> attList) {
        if (attList == null) attList = new ArrayList<>();

        // collect positional attributes
        positionalAttributes = attList.stream()
                                .filter(att -> att.getName() == null)
                                .map(att -> (String)att.getValue())
                                .collect(Collectors.toList());

        // id attribute
        attList.stream()
               .filter(att -> "id".equals(att.getName()))
               .findFirst()
               .ifPresent(att -> id = (String)att.getValue());

        // first positional attribute
        positionalAttributes.stream()
                .findFirst()
                .ifPresent(value -> firstPositionalAttribute = value);

        // collect roles
        String roles = attList.stream()
                            .filter(att -> "role".equals(att.getName()))
                            .map(att -> (String)att.getValue())
                            .collect(Collectors.joining(","));

        // collect options
        String options = attList.stream()
                            .filter(att -> "options".equals(att.getName()))
                            .map(att -> (String)att.getValue())
                            .collect(Collectors.joining(","));

        // attribute list to map
        Predicate<Attribute> attPredicate = att -> !"role".equals(att.getName())
                                                        && !"options".equals(att.getName())
                                                        && att.getName() != null;
        attributes = attList.stream()
                            .filter(attPredicate)
                            .collect(Collectors.toMap(
                                    Attribute::getName,
                                    att -> att,
                                    (att1, att2) -> att2));// merger : last wins

        // add roles and options
        if (roles != null && !roles.isEmpty()) {
            attributes.put("role", new Attribute("role", roles));
        }
        if (options != null && !options.isEmpty()) {
            attributes.put("options", new Attribute("options", options));
        }

    }

    public String getFirstPositionalAttribute() {
        return firstPositionalAttribute;
    }

    public Attribute getAttribute(String name) {
        return attributes.get(name);
    }

    public boolean existsAttribute(String name) {
        return getAttribute(name) != null;
    }

    public String getId() {
        //if (!existsAttribute("id")) return null;
        //return (String)getAttribute("id").getValue();
        return id;
    }

    @Override
    public String toString() {
        return "AttributeList{" +
                "id='" + id + '\'' +
                ", firstPositionalAttribute='" + firstPositionalAttribute + '\'' +
                ", positionalAttributes=" + positionalAttributes +
                ", attributes=" + attributes +
                '}';
    }
}
