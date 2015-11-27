package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.elements.AttributeEntry;
import fr.icodem.asciidoc.parser.elements.ElementFactory;

import java.util.HashMap;
import java.util.Map;

public enum AttributeDefaults {

    Instance;

    private Map<String, AttributeEntry> nameToAttributeMap = new HashMap<>();

    AttributeDefaults() {
        addEntry("backend", "html", false);
        addEntry("doctype", "article", false);
        addEntry("doctitle", "Untitled", false);
    }

    private void addEntry(String name, String value, boolean disabled) {
        ElementFactory ef = new ElementFactory();
        //nameToAttributeMap.put("doctype", ef.attributeEntry("doctype", "article", true));
        nameToAttributeMap.put(name, ef.attributeEntry(name, value, disabled));
    }

    public Map<String, AttributeEntry> getAttributes() {
        Map<String, AttributeEntry> map = new HashMap<>();
        map.putAll(nameToAttributeMap);
        return map;
    }
}
