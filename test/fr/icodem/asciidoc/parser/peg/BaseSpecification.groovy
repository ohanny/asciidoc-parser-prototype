package fr.icodem.asciidoc.parser.peg

import fr.icodem.asciidoc.parser.peg.example.FormattedTextParser
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult
import spock.lang.Specification


class BaseSpecification extends Specification {
    ParsingResult parse(String input) {
        FormattedTextParser parser = new FormattedTextParser();
        new ParseRunner(parser, parser.&formattedText)
                .generateStringTree()
                //.trace()
                .parse(new StringReader(input), null, null, null);
    }

}