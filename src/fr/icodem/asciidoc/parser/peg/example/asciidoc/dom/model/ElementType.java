package fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model;

public enum ElementType {
    Section,
    Paragraph, Quote, UnorderedList, OrderedList, ListItem, DescriptionList, DescriptionListItem, Example, Sidebar,
    Literal, Listing, Table, HorizontalRule,
    ImageBlock, Video,
    TextNode, BoldNode, ItalicNode, SuperscriptNode, SubscriptNode, MonospaceNode, MarkNode, XRefNode, InlineListNode
}
