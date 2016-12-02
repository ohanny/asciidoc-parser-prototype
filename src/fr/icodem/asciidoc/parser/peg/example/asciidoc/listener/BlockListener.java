package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import fr.icodem.asciidoc.parser.peg.NodeContext;
import fr.icodem.asciidoc.parser.peg.listeners.ParseTreeListener;

import static java.lang.Math.min;

/**
 * PEG listener
 * New approach : no use of AST
 * Runs in main thread.
 */
public class BlockListener implements ParseTreeListener {

    private BlockListenerDelegate delegate;

    public BlockListener(AsciidocHandler handler) {
        this.delegate = new BlockListenerDelegate(handler);
    }

    @Override
    public void characters(NodeContext context, char[] chars, int startIndex, int endIndex) {// TODO recycle char tab => add offset + length (cf SAX)
        //System.out.println("CHARS => " + new String(chars));
        //final Text text = textObjects.peek();
        //text.offer(new String(chars));
        //if (text instanceof Text.FormattedText) { // TODO remplacer par isFormattedText ?
            //textProcessor.parse((Text.FormattedText) text);
        //}

        switch (context.getNodeName()) {
            case "title":
                delegate.text(new String(chars));
                break;
            case "paragraph":
            case "listItemValue":
                delegate.formattedText(chars);
                break;
            case "authorName":
                delegate.authorName(new String(chars));
                break;
            case "authorAddress":
                delegate.authorAddress(new String(chars));
                break;
            case "authorAddressLabel":
                delegate.authorAddressLabel(new String(chars));
                break;
            case "name":
            case "attributeValue":
                delegate.attibuteValue(new String(chars));
                break;
        }

    }

    @Override
    public void enterNode(NodeContext context) {
        //System.out.println(context.getNodeName());
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

            // document and header
            case "document" :
                delegate.enterDocument();
                break;
            case "header" :
                delegate.enterHeader();
                break;
            case "documentTitle" :
                delegate.enterDocumentTitle();
                break;
            case "authors" :
                delegate.enterAuthors();
                break;
            case "author" :
                delegate.enterAuthor();
                break;
            case "preamble" :
                delegate.enterPreamble();
                break;

            // content and sections
            case "content" :
                delegate.enterContent();
                break;
            case "section" :
                delegate.enterSection();
                break;
            case "sectionTitle" :
                delegate.enterSectionTitle(context);
                break;

            // blocks
            case "horizontalRule":
                delegate.horizontalRule();
                break;
            case "paragraph" :
                delegate.enterParagraph();
                break;
            case "list" :
                delegate.enterList();
                break;
            case "listItem" :
                delegate.enterListItem(context);
                break;
            case "listItemValue" :
                delegate.enterListItemValue();
                break;


            case "title" :
//                enterTitle();
                break;
//                enterDocumentTitle();


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

            // document and header
            case "document" :
                delegate.exitDocument();
                break;
            case "header" :
                delegate.exitHeader();
                break;
            case "documentTitle" :
                delegate.exitDocumentTitle();
                break;
            case "authors" :
                delegate.exitAuthors();
                break;
            case "author" :
                delegate.exitAuthor();
                break;
            case "preamble" :
                delegate.exitPreamble();
                break;

            // content and sections
            case "content" :
                delegate.exitContent();
                break;
            case "section" :
                delegate.exitSection();
                break;
            case "sectionTitle" :
                delegate.exitSectionTitle(context);
                break;

            // blocks
            case "paragraph" :
                delegate.exitParagraph();
                break;
            case "list" :
                delegate.exitList();
                break;
            case "listItem" :
                delegate.exitListItem();
                break;
            case "listItemValue" :
                delegate.exitListItemValue();
                break;
            case "listContinuation" :
                break;

            case "nl" :
            case "bl" :
            case "title" :
                break;
        }
    }
}
