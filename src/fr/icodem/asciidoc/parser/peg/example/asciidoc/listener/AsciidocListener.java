package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import fr.icodem.asciidoc.parser.elements.Text;
import fr.icodem.asciidoc.parser.peg.NodeContext;
import fr.icodem.asciidoc.parser.peg.listeners.ParseTreeListener;

import java.util.Deque;
import java.util.LinkedList;

/**
 * PEG listener
 * New approach : no use of AST
 * Runs in main thread.
 */
public class AsciidocListener implements ParseTreeListener {

    private AsciidocHandler handler;

    private Deque<Text> textObjects;

    public AsciidocListener(AsciidocHandler handler) {
        this.handler = handler;
        this.textObjects = new LinkedList<>();
    }

    @Override
    public void characters(char[] chars, int startIndex, int endIndex) {
        System.out.println("CHARS => " + new String(chars));
        final Text text = textObjects.peek();
        text.offer(new String(chars));
        if (text instanceof Text.FormattedText) { // TODO remplacer par isFormattedText ?
            //textProcessor.parse((Text.FormattedText) text);
        }


    }

    @Override
    public void enterNode(NodeContext context) {
        System.out.println(context.getNodeName());
        switch (context.getNodeName()) {
            case "document" :
                handler.startDocument();
                break;
            case "header" :
                handler.startHeader();
                break;
            case "documentTitle" :
                textObjects.push(Text.dummy());
                break;
            case "title" :
//                enterTitle();
                break;
//                enterDocumentTitle();
            case "attributeList" :
//                enterAttributeList();
                break;
            case "preamble" :
//                enterPreamble();
                break;
            case "content" :
//                enterContent();
                break;
            case "section" :
//                enterSection();
                break;
            case "sectionTitle" :
//                enterSectionTitle(context);
                break;
            case "list" :
//                enterList();
                break;
            case "listItem" :
//                enterListItem(context);
                break;
            case "listItemValue" :
//                enterListItemValue();
                break;
            case "author" :
//                enterAuthor();
                break;
            case "authorAddress" :
//                enterAuthorAddress();
                break;
            case "authorName" :
//                enterAuthorName();
                break;
            case "idName" :
//                enterIdName();
                break;
            case "roleName" :
//                enterRoleName();
                break;
            case "optionName" :
//                enterOptionName();
                break;
            case "paragraph" :
//                enterParagraph();
                break;
            case "positionalAttribute" :
//                enterPositionalAttribute();
                break;
            case "namedAttribute" :
//                enterNamedAttribute();
                break;
            case "attributeEntry" :
//                enterAttributeEntry(context);
                break;
            case "block" :
//                enterBlock(context.getBooleanAttribute("fromList", false));
                break;
            case "nl" :
            case "bl" :
            case "authors" :
            case "listContinuation" :
//                textObjects.push(Text.dummy());
                break;
        }

    }

    @Override
    public void exitNode(NodeContext context) {
        switch (context.getNodeName()) {
            case "document" :
                handler.endDocument();
                break;
            case "header" :
                handler.endHeader();
                break;
            case "documentTitle" :
                Text text = textObjects.pop();

                break;
            case "preamble" :
//                exitPreamble();
                break;
            case "section" :
//                exitSection();
                break;
            case "sectionTitle" :
//                exitSectionTitle();
                break;
            case "list" :
//                exitList();
                break;
            case "authors" :
            case "authorName" :
            case "authorAddress" :
            case "idName" :
            case "roleName" :
            case "optionName" :
            case "paragraph" :
            case "attributeEntry" :
                //case "attributeName" :
                //case "attributeValue" :
            case "positionalAttribute" :
            case "nl" :
            case "bl" :
            case "listItem" :
            case "listItemValue" :
            case "listContinuation" :
            case "title" :
            case "attributeList" :
//                textObjects.pop();
                break;
        }
    }
}
