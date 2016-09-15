package fr.icodem.asciidoc.parser.block

import fr.icodem.asciidoc.parser.formattedtext.FormattedTextBaseSpecification
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult

class AdmonitionSpec extends FormattedTextBaseSpecification {

    def "note admonition"() {
        given:
        String input = "NOTE: xxx";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(paragraph (admonition N O T E : ) (text I x x) )";
    }

}