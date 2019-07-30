package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener2;

import fr.icodem.asciidoc.parser.peg.NodeContext;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.AttributeEntries;
import fr.icodem.asciidoc.parser.peg.listeners.ParseTreeListener;

/**
 * PEG listener
 */
public class BlockListener2 implements ParseTreeListener {

    private AsciidocHandler2 handler;
    private AttributeEntries attributeEntries;

    public BlockListener2(AsciidocHandler2 handler, AttributeEntries attributeEntries) {
        this.handler = handler;
        this.attributeEntries = attributeEntries;
    }

    @Override
    public void characters(NodeContext context, char[] chars, int startIndex, int endIndex) {// TODO recycle char tab => add offset + length (cf SAX)
        //System.out.println("CHARS => " + new String(chars));

        switch (context.getNodeName()) {
            case "attributeEntry":
                handler.attributeEntry(new String(chars));
                break;

            case "attributeEntryName":
                handler.attributeEntryName(new String(chars));
                break;

            case "attributeValuePart":
                handler.attributeEntryValuePart(new String(chars));
                break;

            case "documentTitle":
                handler.documentTitle(new String(chars));
                break;
            case "sectionTitle":
                handler.sectionTitle(new String(chars));
                break;
            case "blockTitleValue":
                handler.blockTitleValue(chars);
                break;
            case "paragraph":
            case "listItemValue":
            case "labeledListItemTitle":
            case "literalBlock":
            case "listingBlock":
                handler.formattedText(chars);
                break;
            case "labeledListItemContent":
                handler.enterLabeledListSimpleContent();
                handler.formattedText(chars);
                handler.exitLabeledListSimpleContent();
                break;
            case "authorName":
                handler.authorName(new String(chars));
                break;
            case "authorAddress":
                handler.authorAddress(new String(chars));
                break;
            case "authorAddressLabel":
                handler.authorAddressLabel(new String(chars));
                break;
            case "revisionInfo":
                handler.revisionInfo(new String(chars));
                break;
            case "attributeName":
                handler.attributeName(new String(chars));
                break;
            case "attributeValue":
                handler.attributeValue(new String(chars));
                break;

            case "macroName":
                handler.macroName(new String(chars));
                break;
            case "macroTarget":
                handler.macroTarget(new String(chars));
                break;

            case "tableBlock":
                handler.tableBlock(new String(chars).trim());
                break;

//            case "listingBlock" :
//                handler.listingBlock(chars);
//                break;
            case "calloutNumber" :
                handler.calloutNumber(new String(chars));
                break;
            case "calloutText" :
                handler.enterCalloutText();
                handler.formattedText(chars);
                handler.exitCalloutText();
                break;
        }
    }

    @Override
    public void enterNode(NodeContext context) {
        //System.out.println(context.getNodeName());
        switch (context.getNodeName()) {
            // attributes
            case "attributeEntry" :
                handler.enterAttributeEntry();
                break;
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

            // macro
            case "macro":
                handler.enterMacro();
                break;

            // document and header
            case "document" :
                handler.enterDocument();
                break;
            case "header" :
                handler.enterHeader();
                break;
            case "documentSection" :
                handler.enterDocumentTitle();
                break;
            case "authors" :
                handler.enterAuthors();
                break;
            case "author" :
                handler.enterAuthor();
                break;
            case "revisionInfo" :
                handler.enterRevisionInfo();
                break;
            case "preamble" :
                handler.enterPreamble();
                break;

            // content and sections
            case "content" :
                handler.enterContent();
                break;
            case "section" :
                handler.enterSection(context);
                break;

            // blocks
            case "horizontalRule":
                handler.horizontalRule();
                break;
            case "paragraph" :
                final String admonition = context.getStringAttribute("admonition", null);
                handler.enterParagraph(admonition);
                break;
            case "list" :
                handler.enterList();
                break;
            case "listItem" :
                handler.enterListItem(context);
                break;
            case "listItemValue" :
                handler.enterListItemValue();
                break;
            case "labeledList" :
                handler.enterLabeledList();
                break;
            case "labeledListItemTitle" :
                handler.enterLabeledListTitle();
                break;
            case "labeledListItemContent" :
                handler.enterLabeledListContent();
                break;
            case "table" :
                handler.enterTable(context.getIntAttribute("lineNumber", -1));
                break;
            case "tableRow" :
                handler.enterTableRow();
                break;
            case "tableCell" :
                handler.enterTableCell(context.getIntAttribute("lineNumber", -1));
                break;
            case "listingBlock" :
                handler.enterListingBlock();
                break;
            case "callouts":
                handler.enterCallouts();
                break;
            case "callout":
                handler.enterCallout();
                break;
            case "exampleBlock":
                handler.enterExample();
                break;
            case "sidebarBlock":
                handler.enterSidebar();
                break;
            case "literalBlock":
                handler.enterLiteralBlock();
                break;
        }

    }

    @Override
    public void exitNode(NodeContext context) {
        switch (context.getNodeName()) {
            // attributes
            case "attributeEntry" :
                handler.exitAttributeEntry();
                break;

            // macro
            case "macro":
                handler.exitMacro();
                break;

            // document and header
            case "document" :
                handler.exitDocument();
                break;
            case "header" :
                handler.exitHeader();
                break;
            case "documentSection" :
                handler.exitDocumentTitle();
                break;
            case "authors" :
                handler.exitAuthors();
                break;
            case "author" :
                handler.exitAuthor();
                break;
            case "revisionInfo" :
                handler.exitRevisionInfo();
                break;
            case "preamble" :
                handler.exitPreamble();
                break;

            // content and sections
            case "content" :
                handler.exitContent();
                break;

            // blocks
            case "paragraph" :
                handler.exitParagraph();
                break;
            case "list" :
                handler.exitList();
                break;
            case "listItem" :
                handler.exitListItem();
                break;
            case "listItemValue" :
                handler.exitListItemValue();
                break;
            case "labeledList" :
                handler.exitLabeledList();
                break;
            case "labeledListItemTitle" :
                handler.exitLabeledListTitle();
                break;
            case "labeledListItemContent" :
                handler.exitLabeledListContent();
                break;
            case "table" :
                handler.exitTable();
                break;
            case "tableRow" :
                handler.exitTableRow();
                break;
            case "tableCell" :
                handler.exitTableCell();
                break;
            case "listingBlock" :
                handler.exitListingBlock();
                break;
            case "callouts":
                handler.exitCallouts();
                break;
            case "callout":
                handler.exitCallout();
                break;
            case "exampleBlock":
                handler.exitExample();
                break;
            case "sidebarBlock":
                handler.exitSidebar();
                break;
            case "literalBlock":
                handler.exitLiteralBlock();
                break;

        }
    }

    public void postProcess() {
        handler.postProcess();
    }
}
