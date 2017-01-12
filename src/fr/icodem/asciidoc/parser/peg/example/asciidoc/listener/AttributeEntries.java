package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import java.util.HashMap;
import java.util.Map;

public class AttributeEntries {

    private Map<String, AttributeEntry> nameToAttributeMap = new HashMap<>();

    public static AttributeEntries newAttributeEntries() {
        return new AttributeEntries();
    }

    private AttributeEntries() {
        addAttribute("renderer", "html");
        addAttribute("doctype", "article");
        addAttribute("doctitle", "Untitled");
        addAttribute("toc", null, true);
        addAttribute("icons", null);
        addAttribute("toc-title", "Table of Contents");
        addAttribute("source-highlighter", null);
    }

    private void addAttribute(String name, String value, boolean disabled) {
        addAttribute(AttributeEntry.of(name, value, disabled));
    }

    private void addAttribute(String name, String value) {
        addAttribute(AttributeEntry.of(name, value));
    }

    public void addAttribute(AttributeEntry att) {
        nameToAttributeMap.put(att.getName(), att);
    }

    public AttributeEntry getAttribute(String name) {
        return nameToAttributeMap.get(name);
    }

    public boolean isAttributeValueEqualTo(String name, String value) {
        AttributeEntry att = getAttribute(name);
        return (att != null && value.equals(att.getValue()));
    }
}
