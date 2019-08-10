package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener2;

import fr.icodem.asciidoc.parser.peg.NodeContext;

public interface AsciidocHandler2 {

    // attribute entries
    default void enterAttributeEntry() {}

    default void attributeEntry(String delim) {}

    default void exitAttributeEntry() {}

    default void attributeEntryName(String name) {}

    default void attributeEntryValuePart(String value) {}

    // document and header methods
    default void enterDocument() {}

    default void exitDocument() {}

    default void enterHeader() {}

    default void exitHeader() {}

    default void enterDocumentTitle() {}

    default void documentTitle(String text) {}

    default void exitDocumentTitle() {}

    default void enterAuthors() {}

    default void exitAuthors() {}

    default void enterAuthor() {}

    default void authorName(String name) {}

    // TODO rename to email
    default void authorAddress(String address) {}

    @Deprecated
    default void authorAddressLabel(String label) {}

    default void exitAuthor() {}

    default void enterRevisionInfo() {}
    default void revisionInfo(String line) {}
    default void exitRevisionInfo() {}

    default void enterPreamble() {}

    default void exitPreamble() {}

    // attributes
    default void attributeName(String value) {}

    default void attributeValue(String value) {}

    default void enterIdAttribute() {}

    default void enterRoleAttribute() {}

    default void enterOptionAttribute() {}

    default void enterPositionalAttribute() {}

    default void enterNamedAttribute() {}

    // macro methods  // TODO rename to 'block macro'
    default void enterMacro() {}

    default void exitMacro() {}

    default void macroName(String name) {}

    default void macroTarget(String target) {}

    // content and sections methods
    default void enterContent() {}

    default void exitContent() {}

    default void enterSection(NodeContext context) {}

    default void sectionTitle(String title) {}

    // blocks methods
    default void blockTitleValue(char[] chars) {}

    default void horizontalRule() {}

    default void enterParagraph(String admonition) {}

    default void exitParagraph() {}

    default void enterList() {}

    default void exitList() {}

    default void enterListItem(NodeContext context) {}

    default void exitListItem() {}

    default void enterListItemValue() {}

    default void exitListItemValue() {}

    default void enterDescriptionList() {}

    default void exitDescriptionList() {}

    default void enterDescriptionListItem() {}

    default void exitDescriptionListItem() {}

    default void enterDescriptionListItemTitle() {}

    default void exitDescriptionListItemTitle() {}

    default void enterDescriptionListItemContent() {}

    default void exitDescriptionListItemContent() {}

    default void enterDescriptionListItemSimpleContent() {}

    default void exitDescriptionListItemSimpleContent() {}

    default void enterTable(int lineNumber) {}

    default void exitTable() {}

    //default void enterTableRow() {}

    //default void exitTableRow() {}

    default void enterTableCell(int lineNumber) {}

    default void exitTableCell() {}

    default void tableBlock(String text) {}

    // default void listingBlock(char[] chars) {}

    default void enterListingBlock() {}

    default void exitListingBlock() {}

    default void enterCallouts() {}

    default void exitCallouts() {}

    default void enterCallout() {}

    default void calloutNumber(String nb) {}

    default void enterCalloutText() {}

    default void exitCalloutText() {}

    default void exitCallout() {}

    default void enterExample() {}

    default void exitExample() {}

    default void enterSidebar() {}

    default void exitSidebar() {}

    default void enterLiteralBlock() {}

    default void exitLiteralBlock() {}


    // TODO to be refactored
    // inline text
    default void formattedText(char[] chars) {}
}
