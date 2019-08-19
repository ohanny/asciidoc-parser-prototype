package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener2;

import fr.icodem.asciidoc.parser.peg.NodeContext;
import fr.icodem.asciidoc.parser.peg.listeners.ParseTreeListener;

public class InlineListener2 implements ParseTreeListener {

    private InlineHandler2 handler;

    public InlineListener2(InlineHandler2 handler) {
        this.handler = handler;
    }

    @Override
    public void characters(NodeContext context, char[] chars, int startIndex, int endIndex) {
        switch (context.getNodeName()) {
            case "text":
                handler.text(new String(chars));
                break;
            case "attributeName":
                handler.attributeName(new String(chars));
                break;
            case "attributeValue":
                handler.attributeValue(new String(chars));
                break;

            case "macroName":
                //handler.macroName(new String(chars)); TODO
                break;
            case "macroTarget":
                //handler.macroTarget(new String(chars)); TODO
                break;
            case "xrefValue":
                handler.xrefValue(new String(chars));
                break;
            case "xrefLabel":
                handler.xrefLabel(new String(chars));
                break;
            case "xmlEntity":
                handler.xmlEntity(new String(chars));
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
                handler.enterIdAttribute();
                break;
            case "roleAttribute" :
                handler.enterRoleAttribute();
                break;
            case "optionAttribute" :
                handler.enterOptionAttribute();
                break;
            case "positionalAttribute" :
                handler.enterPositionalAttribute();
                break;
            case "namedAttribute" :
                handler.enterNamedAttribute();
                break;
            case "attributeEntry" :
//                enterAttributeEntry(context);
                break;

            // macro
            case "macro":
//                handler.enterMacro(); TODO
                break;


            // markup
            case "bold" :
                handler.enterBold();
                break;
            case "italic" :
                handler.enterItalic();
                break;
            case "subscript" :
                handler.enterSubscript();
                break;
            case "superscript" :
                handler.enterSuperscript();
                break;
            case "monospace" :
                handler.enterMonospace();
                break;
            case "mark" :
                handler.enterMark();
                break;
            case "xref" :
                handler.enterXRef();
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
                //handler.exitMacro(); TODO
                break;

            // markup
            case "bold" :
                handler.exitBold();
                break;
            case "italic" :
                handler.exitItalic();
                break;
            case "subscript" :
                handler.exitSubscript();
                break;
            case "superscript" :
                handler.exitSuperscript();
                break;
            case "monospace" :
                handler.exitMonospace();
                break;
            case "mark" :
                handler.exitMark();
                break;
            case "xref" :
                handler.exitXRef();
                break;
        }
    }
}
