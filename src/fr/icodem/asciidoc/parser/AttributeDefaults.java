package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.elements.AttributeEntry;
import fr.icodem.asciidoc.parser.elements.ElementFactory;

import java.util.HashMap;
import java.util.Map;

public enum AttributeDefaults {

    Instance;

    private Map<String, AttributeEntry> nameToAttributeMap = new HashMap<>();

    AttributeDefaults() {
        addEntry("backend", "html");
        addEntry("doctype", "article");
    }

    private void addEntry(String name, String value) {
        ElementFactory ef = new ElementFactory();
        nameToAttributeMap.put("doctype", ef.attributeEntry("doctype", "article"));
    }

    public Map<String, AttributeEntry> getAttributes() {
        Map<String, AttributeEntry> map = new HashMap<>();
        map.putAll(nameToAttributeMap);
        return map;
    }
}
