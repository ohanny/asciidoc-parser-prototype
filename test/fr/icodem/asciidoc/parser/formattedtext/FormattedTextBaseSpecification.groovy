package fr.icodem.asciidoc.parser.formattedtext

import fr.icodem.asciidoc.parser.peg.example.asciidoc.FormattedTextRules
import fr.icodem.asciidoc.parser.peg.rules.RulesFactory
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult
import spock.lang.Specification

import static fr.icodem.asciidoc.parser.peg.rules.RulesFactory.defaultRulesFactory


class FormattedTextBaseSpecification extends Specification {

    protected RulesFactory factory = defaultRulesFactory()

    ParsingResult parse(String input) {
        FormattedTextRules rules = new FormattedTextRules();
        rules.useFactory(factory);
        new ParseRunner(rules, rules.&formattedText)
                .generateStringTree()
                //.trace()
                .parse(new StringReader(input), null, null, null);
    }

}