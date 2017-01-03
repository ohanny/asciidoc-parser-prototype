package fr.icodem.asciidoc.parser.text

import fr.icodem.asciidoc.parser.peg.example.asciidoc.TextRules
import fr.icodem.asciidoc.parser.peg.rules.RulesFactory
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult
import spock.lang.Specification

import static fr.icodem.asciidoc.parser.peg.rules.RulesFactory.defaultRulesFactory


class TextSpecification extends Specification {

    protected RulesFactory factory = defaultRulesFactory()

    ParsingResult parse(String input) {
        TextRules rules = new TextRules()
        rules.withFactory(factory)
        new ParseRunner(rules, rules.&formattedText)
                .generateStringTree()
                //.trace()
                .parse(new StringReader(input), null, null, null)
    }

}