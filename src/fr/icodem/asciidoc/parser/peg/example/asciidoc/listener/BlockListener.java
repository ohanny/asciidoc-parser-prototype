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

    public BlockListener(AsciidocHandler handler, AttributeEntries attributeEntries) {
        this.delegate = new BlockListenerDelegate(handler, attributeEntries);
    }

    @Override
    public void characters(NodeContext context, char[] chars, int startIndex, int endIndex) {// TODO recycle char tab => add offset + length (cf SAX)
        //System.out.println("CHARS => " + new String(chars));

        switch (context.getNodeName()) {
            case "attributeEntry":
                delegate.attributeEntry(new String(chars));
                break;

            case "attributeEntryName":
                delegate.attributeEntryName(new String(chars));
                break;

            case "attributeValuePart":
                delegate.attributeEntryValuePart(new String(chars));
                break;

            case "documentTitle":
                delegate.documentTitle(new String(chars));
                break;
            case "sectionTitle":
                delegate.sectionTitle(new String(chars));
                break;
            case "blockTitleValue":
                delegate.blockTitleValue(chars);
                break;
            case "paragraph":
            case "listItemValue":
            case "labeledListItemTitle":
                delegate.formattedText(chars);
                break;
            case "labeledListItemContent":
                delegate.enterLabeledListSimpleContent();
                delegate.formattedText(chars);
                delegate.exitLabeledListSimpleContent();
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

            case "tableBlock":
                delegate.tableBlock(new String(chars).trim());
                break;

            case "listingBlock" :
                delegate.listingBlock(chars);
                break;
            case "calloutNumber" :
                delegate.calloutNumber(new String(chars));
                break;
            case "calloutText" :
                delegate.enterCalloutText();
                delegate.formattedText(chars);
                delegate.exitCalloutText();
                break;
        }
    }

    @Override
    public void enterNode(NodeContext context) {
        //System.out.println(context.getNodeName());
        switch (context.getNodeName()) {
            // attributes
            case "attributeEntry" :
               delegate.enterAttributeEntry();
                break;
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

            // macro
            case "macro":
                delegate.enterMacro();
                break;

            // document and header
            case "document" :
                delegate.enterDocument();
                break;
            case "header" :
                delegate.enterHeader();
                break;
            case "documentSection" :
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
                delegate.enterSection(context);
                break;

            // blocks
            case "horizontalRule":
                delegate.horizontalRule();
                break;
            case "paragraph" :
                final String admonition = context.getStringAttribute("admonition", null);
                delegate.enterParagraph(admonition);
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
            case "labeledList" :
                delegate.enterLabeledList();
                break;
            case "labeledListItemTitle" :
                delegate.enterLabeledListTitle();
                break;
            case "labeledListItemContent" :
                delegate.enterLabeledListContent();
                break;
            case "table" :
                delegate.enterTable(context.getIntAttribute("lineNumber", -1));
                break;
            case "tableRow" :
                delegate.enterTableRow();
                break;
            case "tableCell" :
                delegate.enterTableCell(context.getIntAttribute("lineNumber", -1));
                break;
            case "callouts":
                delegate.enterCallouts();
                break;
            case "callout":
                delegate.enterCallout();
                break;
            case "exampleBlock":
                delegate.enterExample();
                break;
        }

    }

    @Override
    public void exitNode(NodeContext context) {
        switch (context.getNodeName()) {
            // attributes
            case "attributeEntry" :
                delegate.exitAttributeEntry();
                break;

            // macro
            case "macro":
                delegate.exitMacro();
                break;

            // document and header
            case "document" :
                delegate.exitDocument();
                break;
            case "header" :
                delegate.exitHeader();
                break;
            case "documentSection" :
                delegate.exitDocumentTitle();
                break;
            case "authors" :
                delegate.exitAuthors();
                break;
            case "preamble" :
                delegate.exitPreamble();
                break;

            // content and sections
            case "content" :
                delegate.exitContent();
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
            case "labeledList" :
                delegate.exitLabeledList();
                break;
            case "labeledListItemTitle" :
                delegate.exitLabeledListTitle();
                break;
            case "labeledListItemContent" :
                delegate.exitLabeledListContent();
                break;
            case "table" :
                delegate.exitTable();
                break;
            case "tableRow" :
                delegate.exitTableRow();
                break;
            case "tableCell" :
                delegate.exitTableCell();
                break;
            case "listingBlock" :
                delegate.exitListingBlock();
                break;
            case "callouts":
                delegate.exitCallouts();
                break;
            case "callout":
                delegate.exitCallout();
                break;
            case "exampleBlock":
                delegate.exitExample();
                break;

        }
    }

    public void postProcess() {
        delegate.postProcess();
    }
}
