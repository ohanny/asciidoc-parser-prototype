package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AttributeList {
    private String id;

    private String firstPositionalAttribute;
    private List<String> positionalAttributes;

    private Set<String> options;
    private Set<String> roles;

    private Map<String, Attribute> attributes;

    public static AttributeList of(List<Attribute> attList) {
        return new AttributeList(attList);
    }

    private AttributeList(List<Attribute> attList) {

        // collect positional attributes
        positionalAttributes = attList.stream()
                .filter(Attribute::isPositionalAttribute)
                .map(Attribute::getValue)
                .collect(Collectors.toList());

        // id attribute
        attList.stream()
                .filter(Attribute::isIdAttribute)
                .findFirst()
                .ifPresent(att -> id = att.getName());

        // first positional attribute
        positionalAttributes.stream()
                .findFirst()
                .ifPresent(value -> firstPositionalAttribute = value);

        // collect roles
        roles = attList.stream()
                .filter(Attribute::isRoleAttribute)
                .map(Attribute::getName)
                .collect(Collectors.toSet());

        // collect options
        options = attList.stream()
                .filter(Attribute::isOptionAttribute)
                .map(Attribute::getName)
                .collect(Collectors.toSet());

        // attribute list to map
        attributes = attList.stream()
                .filter(Attribute::isNamedAttribute)
                .collect(Collectors.toMap(
                        Attribute::getName,
                        att -> att,
                        (att1, att2) -> att2)); // merger : last wins
    }

    public String getFirstPositionalAttribute() {
        return firstPositionalAttribute;
    }

    public String getSecondPositionalAttribute() {
        if (positionalAttributes != null && positionalAttributes.size() > 1) {
            return positionalAttributes.get(1);
        }
        return null;
    }

    public String getThirdPositionalAttribute() {
        if (positionalAttributes != null && positionalAttributes.size() > 2) {
            return positionalAttributes.get(2);
        }
        return null;
    }

    public Attribute getAttribute(String name) {
        return attributes.get(name);
    }

    public String getStringValue(String name, String defaultValue) {
        Attribute att = getAttribute(name);
        if (att != null) {
            return att.getValue();
        }

        return defaultValue;
    }

    public boolean hasOption(String option) {
        return options.contains(option);
    }

    public Set<String> getRoles() {
        return roles;
    }

    public Set<String> getOptions() {
        return options;
    }

    public List<String> getPositionalAttributes() {
        return positionalAttributes;
    }

    public boolean hasPositionalAttributes(String name) {
        return positionalAttributes != null && positionalAttributes.contains(name);
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "AttributeList{" +
                "id='" + id + '\'' +
                ", firstPositionalAttribute='" + firstPositionalAttribute + '\'' +
                ", positionalAttributes=" + positionalAttributes +
                ", options=" + options +
                ", roles=" + roles +
                ", attributes=" + attributes +
                '}';
    }


}
