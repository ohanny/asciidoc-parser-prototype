package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.elements.Text;
import fr.icodem.asciidoc.parser.peg.FormattedTextListener;
import fr.icodem.asciidoc.parser.peg.NodeContext;
import fr.icodem.asciidoc.parser.peg.example.FormattedTextRules;
import fr.icodem.asciidoc.parser.peg.listeners.ParseTreeListener;
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner;
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;

import java.io.StringReader;
import java.util.Deque;
import java.util.LinkedList;

import static fr.icodem.asciidoc.parser.peg.rules.RulesFactory.defaultRulesFactory;

public class FormattedTextPegProcessor {

    private FormattedTextRules rules = new FormattedTextRules();

    public FormattedTextPegProcessor() {
        rules.useFactory(defaultRulesFactory());
    }

    public void parse(Text.FormattedText formattedText) {
        FormattedTextListener listener = new FormattedTextListener(formattedText);

        ParsingResult result = new ParseRunner(rules, rules::formattedText)
                //.trace()
                .parse(new StringReader(formattedText.getValue()), listener, null, null);//TODO listener dans une autre classe

        formattedText.offer(null);// TODO
    }

}
