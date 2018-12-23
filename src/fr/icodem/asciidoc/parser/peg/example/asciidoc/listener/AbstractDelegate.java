package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractDelegate {
    protected Deque<Text> textObjects;
    protected AttributeEntries attributeEntries;
    protected List<Attribute> attList;


    private MacroContext currentMacro;
    protected static class MacroContext {
        String name;
        String target;
        AttributeList attList; // block attribute list

        public MacroContext(AttributeList attList) {
            this.attList = attList;
        }

        static MacroContext of(AttributeList attList) {
            return new MacroContext(attList);
        }

    }


    public AbstractDelegate(AttributeEntries attributeEntries) {
        this.textObjects = new LinkedList<>();
        this.attList = new LinkedList<>();

        //this.attributeEntries = AttributeEntries.newAttributeEntries();
        this.attributeEntries = attributeEntries;
    }

    // attributes methods
    protected boolean hasAttributeEntry(String name) {
        return attributeEntries.getAttribute(name) != null;
    }

    protected AttributeEntry getAttributeEntry(String name) {
        return attributeEntries.getAttribute(name);
    }

    protected AttributeList consumeAttList() {
        if (this.attList.isEmpty()) return null;
        AttributeList attList = AttributeList.of(Collections.unmodifiableList(this.attList));
        clearAttList();
        return attList;
    }

    protected void clearAttList() {
        attList.clear();
    }

    public void attributeName(String value) {
        textObjects.pop()
                .setValue(value);
    }

    public void attributeValue(String value) {
        textObjects.pop()
                .setValue(value);
    }

    public void enterIdAttribute() {
        Text text = Text.empty();
        attList.add(Attribute.of("id", text));
        textObjects.push(text);
    }

    public void enterRoleAttribute() {
        Text text = Text.empty();
        attList.add(Attribute.of("role", text));
        textObjects.push(text);
    }

    public void enterOptionAttribute() {
        Text text = Text.empty();
        attList.add(Attribute.of("options", text));
        textObjects.push(text);
    }

    public void enterPositionalAttribute() {
        Text value = Text.empty();
        attList.add(Attribute.of((String)null, value));
        textObjects.push(value);
    }

    public void enterNamedAttribute() {
        Text name = Text.empty();
        Text value = Text.empty();
        attList.add(Attribute.of(name, value));

        textObjects.push(value);
        textObjects.push(name);
    }

    // macro methods
    public void enterMacro() {
        currentMacro = BlockListenerDelegate.MacroContext.of(consumeAttList());
    }

    public void exitMacro() {
        switch (currentMacro.name) {
            case "image":
                String target = currentMacro.target;
                if (hasAttributeEntry("imagesdir")) {
                    target = getAttributeEntry("imagesdir").getValue() + "/" + target; // TODO replace separator
                }
                ImageMacro image =
                        Macro.image(currentMacro.name, target,
                                consumeAttList(), currentMacro.attList);
                image(image);
                break;
            case "video":
                target = currentMacro.target;
                if (hasAttributeEntry("videosdir")) {
                    target = getAttributeEntry("videosdir").getValue() + "/" + target; // TODO replace separator
                }
                VideoMacro video =
                        Macro.video(currentMacro.name, target,
                                consumeAttList(), currentMacro.attList);
                video(video);
                break;
            case "include":
                break;
        }

        currentMacro = null;
    }

    public void macroName(String name) {
        currentMacro.name = name;
    }

    public void macroTarget(String target) {
        currentMacro.target = target;
    }

    // other methods
    protected abstract void image(ImageMacro macro);
    protected abstract void video(VideoMacro macro);
}
