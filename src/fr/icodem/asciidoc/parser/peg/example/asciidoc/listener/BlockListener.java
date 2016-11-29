package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener;

import fr.icodem.asciidoc.parser.peg.NodeContext;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.FormattedTextRules;
import fr.icodem.asciidoc.parser.peg.listeners.ParseTreeListener;
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner;
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;

import java.io.StringReader;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import static fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.AsciidocHandler.DOCUMENT_TITLE;
import static fr.icodem.asciidoc.parser.peg.rules.RulesFactory.defaultRulesFactory;
import static java.lang.Math.min;

/**
 * PEG listener
 * New approach : no use of AST
 * Runs in main thread.
 */
public class BlockListener implements ParseTreeListener {

    private AsciidocHandler handler;

    private Deque<Text> textObjects;
    private Deque<String> nodes; // TODO rename variable
    private List<Attribute> attList;

    private enum ListType {Ordered, Unordered}
    private static class ListContext {
        int level;
        int bullets;
        ListType type;
        ListContext parent;
        ListContext root;

        static ListContext empty() {
            ListContext ctx = new ListContext();
            ctx.level = 1;
            ctx.root = ctx;
            return ctx;
        }

        static ListContext withParent(ListContext parent) {
            ListContext context = new ListContext();
            if (parent != null) {
                context.parent = parent;
                context.level = parent.level + 1;
            } else {
                context.level = 1;
            }
            return context;
        }
    }
    private ListContext currentList;


    public BlockListener(AsciidocHandler handler) {
        this.handler = handler;
        this.nodes = new LinkedList<>();
        this.nodes.add("");
        this.textObjects = new LinkedList<>();
        this.attList = new LinkedList<>();
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

    private AttributeList consumeAttList() {
        if (this.attList.isEmpty()) return null;
        System.out.println(attList);
        AttributeList attList = AttributeList.of(this.attList);
        clearAttList();
        return attList;
    }

    private void clearAttList() {
        attList.clear();
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
                handler.writeText(nodes.peekLast(), new String(chars));
                break;
            case "paragraph":
            case "listItemValue":
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
            case "name":
            case "attributeValue":
                textObjects.pop()
                           .setValue(new String(chars));
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
                Text text = Text.empty();
                attList.add(Attribute.of("id", text));
                textObjects.push(text);
                break;
            case "roleAttribute" :
                text = Text.empty();
                attList.add(Attribute.of("role", text));
                textObjects.push(text);
                break;
            case "optionAttribute" :
                text = Text.empty();
                attList.add(Attribute.of("options", text));
                textObjects.push(text);
                break;
            case "positionalAttribute" :
                Text value = Text.empty();
                attList.add(Attribute.of((String)null, value));
                textObjects.push(value);
                break;
            case "namedAttribute" :
                Text name = Text.empty();
                value = Text.empty();
                attList.add(Attribute.of(name, value));

                textObjects.push(value);
                textObjects.push(name);
                break;
            case "attributeEntry" :
//                enterAttributeEntry(context);
                break;

            // document and header
            case "document" :
                handler.startDocument();
                break;
            case "header" :
                handler.startHeader();
                break;
            case "documentTitle" :
                handler.startDocumentTitle();
                nodes.addLast(DOCUMENT_TITLE);
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

            // content and sections
            case "content" :
                handler.startContent();
                break;
            case "section" :
                handler.startSection();
                break;
            case "sectionTitle" :
                int level = min(context.getIntAttribute("eqs.count", -1), 6);
                handler.startSectionTitle(level);
                break;

            // blocks
            case "paragraph" :
                handler.startParagraph();
                break;
            case "list" :
                currentList = ListContext.empty();
                handler.startList();
                break;
            case "listItem" :
                int times, dots = 0;
                if ((times = context.getIntAttribute("times.count", -1)) > 0) {
                    if (currentList.type == ListType.Unordered) {
                        if (currentList.bullets == times) {

                        } else if (currentList.bullets < times) {
                            currentList = ListContext.withParent(currentList);
                            currentList.bullets = times;
                        } else if (currentList.bullets > times) {
                            while (currentList.bullets > times && currentList.level > 1) {
                                handler.endUnorderedList(currentList.level);
                                currentList = currentList.parent;
                            }
                        }
                    } else if (currentList.type == ListType.Ordered) {
                        currentList = ListContext.withParent(currentList);
                        currentList.bullets = times;
                    }
                } else if ((dots = context.getIntAttribute("dots.count", -1)) > 0) {
                    if (currentList.type == ListType.Ordered) {
                        if (currentList.bullets == dots) {

                        } else if (currentList.bullets < dots) {
                            currentList = ListContext.withParent(currentList);
                            currentList.bullets = dots;
                        } else if (currentList.bullets > dots) {
                            while (currentList.bullets > times && currentList.level > 1) {
                                handler.endOrderedList(currentList.level);
                                currentList = currentList.parent;
                            }
                        }
                    } else if (currentList.type == ListType.Unordered) {
                        currentList = ListContext.withParent(currentList);
                        currentList.bullets = dots;
                    }
                }


                if (currentList.type == null) {
                    if (times > 0) {
                        currentList.type = ListType.Unordered;
                        currentList.bullets = times;
                        handler.startUnorderedList(currentList.level, consumeAttList());
                    } else if (dots > 0) {
                        currentList.type = ListType.Ordered;
                        currentList.bullets = dots;
                        handler.startOrderedList(currentList.level, consumeAttList());
                    }
                }
                handler.startListItem(currentList.level);
                clearAttList();
                break;
            case "listItemValue" :
                handler.startListItemValue();
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

            // content and sections
            case "content" :
                handler.endContent();
                break;
            case "section" :
                handler.endSection();
                break;
            case "sectionTitle" :
                int level = min(context.getIntAttribute("eqs.count", -1), 6);
                handler.endSectionTitle(level);
                break;

            // blocks
            case "paragraph" :
                handler.endParagraph();
                break;
            case "list" :
                while (currentList != null) {
                    if (currentList.type == ListType.Unordered) {
                        handler.endUnorderedList(currentList.level);
                    } else if (currentList.type == ListType.Ordered) {
                        handler.endOrderedList(currentList.level);
                    }
                    currentList = currentList.parent;
                }
                handler.endList();
                break;
            case "listItem" :
                handler.endListItem(currentList.level);
                break;
            case "listItemValue" :
                handler.endListItemValue();
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
