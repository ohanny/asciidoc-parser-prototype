package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import fr.icodem.asciidoc.parser.peg.NodeContext;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractDelegate {
    protected Deque<Text> textObjects;
    protected List<Attribute> attList;

    private MacroContext currentMacro;
    protected static class MacroContext {
        String name;
        String target;

        static MacroContext empty() {
            return new MacroContext();
        }
    }


    public AbstractDelegate() {
        this.textObjects = new LinkedList<>();
        this.attList = new LinkedList<>();
    }

    // attributes methods
    protected AttributeList consumeAttList() {
        if (this.attList.isEmpty()) return null;
        AttributeList attList = AttributeList.of(this.attList);
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
        currentMacro = BlockListenerDelegate.MacroContext.empty();
    }

    public void exitMacro(NodeContext context) {
        switch (currentMacro.name) {
            case "image":
                ImageMacro macro = Macro.image(currentMacro.name, currentMacro.target, consumeAttList());
                //handler.writeImage(image);
                image(macro);
                break;
            case "include":
//    System.out.println("INCLUDE");
//                Reader reader = sourceResolver.resolve(currentMacro.target);
//                context.include(reader);
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
}
