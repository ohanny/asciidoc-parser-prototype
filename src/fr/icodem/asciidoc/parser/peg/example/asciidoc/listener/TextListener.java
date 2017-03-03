package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import fr.icodem.asciidoc.parser.peg.NodeContext;
import fr.icodem.asciidoc.parser.peg.listeners.ParseTreeListener;

public class TextListener implements ParseTreeListener {

    private TextListenerDelegate delegate;

    public TextListener(AsciidocHandler handler, AttributeEntries attributeEntries) {
        this.delegate = new TextListenerDelegate(handler, attributeEntries);
    }

    @Override
    public void characters(NodeContext context, char[] chars, int startIndex, int endIndex) {
        switch (context.getNodeName()) {
            case "text":
                delegate.text(new String(chars));
                break;
            case "attributeName":
                delegate.attributeName(new String(chars));
                break;
            case "attributeValue":
                delegate.attributeValue(new String(chars));
                break;

            case "macroName":
                delegate.macroName(new String(chars));
                break;
            case "macroTarget":
                delegate.macroTarget(new String(chars));
                break;
            case "xrefValue":
                delegate.xrefValue(new String(chars));
                break;
            case "xrefLabel":
                delegate.xrefLabel(new String(chars));
                break;
        }
    }

    @Override
    public void enterNode(NodeContext context) {
        switch (context.getNodeName()) {
            // attributes
            case "attributeList" :
//                enterAttributeList();
                break;
            case "idAttribute" :
                delegate.enterIdAttribute();
                break;
            case "roleAttribute" :
                delegate.enterRoleAttribute();
                break;
            case "optionAttribute" :
                delegate.enterOptionAttribute();
                break;
            case "positionalAttribute" :
                delegate.enterPositionalAttribute();
                break;
            case "namedAttribute" :
                delegate.enterNamedAttribute();
                break;
            case "attributeEntry" :
//                enterAttributeEntry(context);
                break;

            // macro
            case "macro":
                delegate.enterMacro();
                break;


            // markup
            case "bold" :
                delegate.enterBold();
                break;
            case "italic" :
                delegate.enterItalic();
                break;
            case "subscript" :
                delegate.enterSubscript();
                break;
            case "superscript" :
                delegate.enterSuperscript();
                break;
            case "monospace" :
                delegate.enterMonospace();
                break;
            case "mark" :
                delegate.enterMark();
                break;
            case "xref" :
                delegate.enterXRef();
                break;
        }
    }

    @Override
    public void exitNode(NodeContext context) {
        switch (context.getNodeName()) {
            // attributes
            case "attributeList" :
                break;
            case "idName" :
                break;
            case "roleName" :
                break;
            case "optionName" :
                break;
            case "positionalAttribute" :
                break;
            case "namedAttribute" :
                break;
            case "attributeEntry" :
                break;

            // macro
            case "macro":
                delegate.exitMacro();
                break;

            // markup
            case "bold" :
                delegate.exitBold();
                break;
            case "italic" :
                delegate.exitItalic();
                break;
            case "subscript" :
                delegate.exitSubscript();
                break;
            case "superscript" :
                delegate.exitSuperscript();
                break;
            case "monospace" :
                delegate.exitMonospace();
                break;
            case "mark" :
                delegate.exitMark();
                break;
            case "xref" :
                delegate.exitXRef();
                break;
        }
    }
}
