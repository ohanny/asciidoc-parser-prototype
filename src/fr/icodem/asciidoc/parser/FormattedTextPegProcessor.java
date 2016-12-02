package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.elements.Text;
import fr.icodem.asciidoc.parser.peg.FormattedTextListener;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.TextRules;
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner;
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult;

import java.io.StringReader;

import static fr.icodem.asciidoc.parser.peg.rules.RulesFactory.defaultRulesFactory;

@Deprecated
public class FormattedTextPegProcessor {

    private TextRules rules = new TextRules();

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
