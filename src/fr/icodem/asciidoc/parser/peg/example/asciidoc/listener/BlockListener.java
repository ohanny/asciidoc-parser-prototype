package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import fr.icodem.asciidoc.parser.peg.NodeContext;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.FormattedTextRules;
import fr.icodem.asciidoc.parser.peg.listeners.ParseTreeListener;
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner;
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;

import java.io.StringReader;
import java.util.Deque;
import java.util.LinkedList;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.AsciidocHandler.DOCUMENT_TITLE;
import static fr.icodem.asciidoc.parser.peg.rules.RulesFactory.defaultRulesFactory;

/**
 * PEG listener
 * New approach : no use of AST
 * Runs in main thread.
 */
public class BlockListener implements ParseTreeListener {

    private AsciidocHandler handler;

    //private Deque<Text> textObjects;
    private Deque<String> nodes; // TODO rename variable

    public BlockListener(AsciidocHandler handler) {
        this.handler = handler;
        this.nodes = new LinkedList<>();
        this.nodes.add("");
        //this.textObjects = new LinkedList<>();
    }

    private void parseFormattedText(char[] chars) {
        //System.out.println("parseFormattedText() => " + new String(chars));
        FormattedTextRules rules = new FormattedTextRules();// TODO inject rules
        rules.useFactory(defaultRulesFactory());
        ParsingResult result = new ParseRunner(rules, rules::formattedText)
                //.trace()
                .parse(new StringReader(new String(chars)), new FormattedTextListener(handler), null, null);
        // TODO optimize new StringReader(new String(chars))

    }

    @Override
    public void characters(NodeContext context, char[] chars, int startIndex, int endIndex) {// TODO recycle char tab => add offset + length (cf SAX)
        System.out.println("CHARS => " + new String(chars));
        //final Text text = textObjects.peek();
        //text.offer(new String(chars));
        //if (text instanceof Text.FormattedText) { // TODO remplacer par isFormattedText ?
            //textProcessor.parse((Text.FormattedText) text);
        //}

        switch (context.getNodeName()) {
            case "title":
                handler.writeText(nodes.peekLast(), new String(chars));
                break;
            case "paragraph":
                //handler.writeText(nodes.peekLast(), new String(chars));
                parseFormattedText(chars);
                break;
            case "authorName":
                handler.writeAuthorName(new String(chars));
                break;
            case "authorAddress":
                handler.writeAuthorAddress(new String(chars));
                break;
            case "authorAddressLabel":
                handler.writeAuthorAddressLabel(new String(chars));
                break;
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
                handler.startDocumentTitle();
                nodes.addLast(DOCUMENT_TITLE);
                //textObjects.push(Text.dummy());
                break;
            case "authors" :
                handler.startAuthors();
                break;
            case "author" :
                handler.startAuthor();
                break;
            case "preamble" :
                handler.startPreamble();
                break;
            case "content" :
                handler.startContent();
                break;
            case "section" :
                handler.startSection();
                break;

            case "paragraph" :
                handler.startParagraph();
                break;


            case "title" :
//                enterTitle();
                break;
//                enterDocumentTitle();
            case "attributeList" :
//                enterAttributeList();
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
            case "idName" :
//                enterIdName();
                break;
            case "roleName" :
//                enterRoleName();
                break;
            case "optionName" :
//                enterOptionName();
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
                handler.endDocumentTitle();
                nodes.removeLast();
                break;
            case "authors" :
                handler.endAuthors();
                break;
            case "author" :
                handler.endAuthor();
                break;
            case "preamble" :
                handler.endPreamble();
                break;
            case "content" :
                handler.endContent();
                break;
            case "section" :
                handler.endSection();
                break;

            case "paragraph" :
                handler.endParagraph();
                break;

            case "sectionTitle" :
//                exitSectionTitle();
                break;
            case "list" :
//                exitList();
                break;
            case "idName" :
            case "roleName" :
            case "optionName" :
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
