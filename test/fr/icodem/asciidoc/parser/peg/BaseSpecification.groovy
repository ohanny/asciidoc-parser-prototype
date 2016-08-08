package fr.icodem.asciidoc.parser.peg

import fr.icodem.asciidoc.parser.peg.example.FormattedTextParser
import fr.icodem.asciidoc.parser.peg.rules.RulesFactory
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult
import spock.lang.Specification

import static fr.icodem.asciidoc.parser.peg.rules.RulesFactory.defaultRulesFactory


class BaseSpecification extends Specification {

    protected RulesFactory factory = defaultRulesFactory()

    ParsingResult parse(String input) {
        FormattedTextParser parser = new FormattedTextParser();
        parser.useFactory(factory);
        new ParseRunner(parser, parser.&formattedText)
                .generateStringTree()
                //.trace()
                .parse(new StringReader(input), null, null, null);
    }

}