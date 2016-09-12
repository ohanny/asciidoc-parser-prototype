package fr.icodem.asciidoc.parser.peg;

import fr.icodem.asciidoc.parser.elements.Text;
import fr.icodem.asciidoc.parser.peg.listeners.ParseTreeListener;

import java.util.Deque;
import java.util.LinkedList;

public class FormattedTextListener implements ParseTreeListener {

    private Text.FormattedText formattedText;//TODO pas champs instance ici

    private Text.TextItem currentItem;

    private Deque<Text.TextItem> items;

    private boolean skip;

    public FormattedTextListener(Text.FormattedText formattedText) {
        this.formattedText = formattedText;
        items = new LinkedList<>();
        skip = true;
    }

    @Override
    public void characters(char[] chars, int startIndex, int endIndex) {
        if (skip) return;

        String str = new String(chars);
        System.out.println("CHARS => " + str + " : ");

        Text.TextItem item = items.peekLast();

        if (item == null) {
            item = new Text.TextItem(); // TODO
            item.setText(str);
            item.setHead(item);// TODO new headItem

            chainNewItem(item);
        } else {
            if (item.getText() == null) {
                item.setText(str);
            } else {
                Text.TextItem newItem = new Text.TextItem(); // TODO
                newItem.setText(str);

                newItem.setHead(item);// TODO

                currentItem.setNext(newItem);
                currentItem = newItem;
            }
        }
    }

    @Override
    public void enterNode(NodeContext context) {
        Text.TextItem item = null;
        switch (context.getNodeName()) {
            case "text":
                skip = false;
                break;

            case "bold":
                chainAndAddNewItem(Text.BoldTextItem.newInstance());
//                item = Text.BoldTextItem.newInstance();
//                items.add(item);
//                if (currentItem != null) {
//                    currentItem.setNext(item);
//                } else {
//                    formattedText.setFirstItem(item);
//                }
//                currentItem = item;
                break;

            case "italic":
                chainAndAddNewItem(Text.ItalicTextItem.newInstance());
//                item = Text.ItalicTextItem.newInstance();
//                items.add(item);
//                if (currentItem != null) {
//                    currentItem.setNext(item);
//                } else {
//                    formattedText.setFirstItem(item);
//                }
//                currentItem = item;
                break;
        }
    }

    private void chainAndAddNewItem(Text.TextItem item) {
        items.add(item);
        chainNewItem(item);
    }

    private void chainNewItem(Text.TextItem item) {
        if (currentItem != null) {
            currentItem.setNext(item);
        } else {
            formattedText.setFirstItem(item);
        }
        currentItem = item;
    }

    @Override
    public void exitNode(String nodeName) {
        skip = true;

        switch (nodeName) {
            case "bold":
            case "italic":
                Text.TextItem head = items.removeLast();
                if (!currentItem.isTail()) {
                    currentItem.setTail(true);
                } else {
                    Text.TextItem item = new Text.TextItem();
                    item.setHead(head);
                    item.setTail(true);
                    currentItem.setNext(item);
                    currentItem = item;
                }
                break;
        }
    }

}
