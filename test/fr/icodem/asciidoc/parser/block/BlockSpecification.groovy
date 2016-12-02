package fr.icodem.asciidoc.parser.block

import fr.icodem.asciidoc.parser.peg.example.asciidoc.BlockRules
import fr.icodem.asciidoc.parser.peg.rules.RulesFactory
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult
import spock.lang.Specification

import static fr.icodem.asciidoc.parser.peg.rules.RulesFactory.defaultRulesFactory

class BlockSpecification extends Specification {

    protected RulesFactory factory = defaultRulesFactory()

    ParsingResult parse(String input) {
        BlockRules rules = new BlockRules();
        rules.useFactory(factory);
        new ParseRunner(rules, rules.&document)
                .generateStringTree()
                //.trace()
                .parse(new StringReader(input), null, null, null);
    }

}