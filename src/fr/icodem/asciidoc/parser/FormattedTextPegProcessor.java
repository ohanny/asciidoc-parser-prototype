package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.elements.Text;
import fr.icodem.asciidoc.parser.peg.NodeContext;
import fr.icodem.asciidoc.parser.peg.example.FormattedTextRules;
import fr.icodem.asciidoc.parser.peg.listeners.ParseTreeListener;
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner;
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;

import java.io.StringReader;
import java.util.Deque;
import java.util.LinkedList;

import static fr.icodem.asciidoc.parser.peg.rules.RulesFactory.defaultRulesFactory;

public class FormattedTextPegProcessor implements ParseTreeListener {

    private FormattedTextRules rules = new FormattedTextRules();

    private Text.FormattedText formattedText;//TODO pas champs instance ici

    private Text.TextItem currentItem;

    private Deque<Text.TextItem> items;

    private boolean skip;

    public FormattedTextPegProcessor() {
        rules.useFactory(defaultRulesFactory());
        items = new LinkedList<>();
        skip = true;
    }

    public void parse(Text.FormattedText formattedText) {
        this.formattedText = formattedText;

        ParsingResult result = new ParseRunner(rules, rules::formattedText)
                //.trace()
                .parse(new StringReader(formattedText.getValue()), this, null, null);//TODO listener dans une autre classe

        formattedText.offer(null);// TODO
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

            //items.add(item);
            if (currentItem != null) {
                currentItem.setNext(item);
            } else {
                formattedText.setFirstItem(item);
            }
            currentItem = item;
        } else {
            if (item.getText() == null) {
                item.setText(str);
            } else {
                Text.TextItem newItem = new Text.TextItem(); // TODO
                newItem.setText(str);

                newItem.setHead(item);// TODO

                currentItem.setNext(newItem);
                //items.add(item);
//                if (currentItem != null) {
//                    currentItem.setNext(item);
//                } else {
//                    formattedText.setFirstItem(item);
//                }
                currentItem = newItem;
            }
        }

        //nextItem.setText(new String(chars));
    }

    private void addItem() {

    }

    @Override
    public void enterNode(NodeContext context) {
        Text.TextItem item = null;
        switch (context.getNodeName()) {
            case "text":
                skip = false;
                break;

            case "bold":
                item = Text.BoldTextItem.newInstance();
                items.add(item);
                if (currentItem != null) {
                    currentItem.setNext(item);
                } else {
                    formattedText.setFirstItem(item);
                }
                currentItem = item;
                break;

            case "italic":
                item = Text.ItalicTextItem.newInstance();
                items.add(item);
                if (currentItem != null) {
                    currentItem.setNext(item);
                } else {
                    formattedText.setFirstItem(item);
                }
                currentItem = item;
                break;
        }
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
