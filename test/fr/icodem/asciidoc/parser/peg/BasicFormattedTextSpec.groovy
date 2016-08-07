package fr.icodem.asciidoc.parser.peg

import fr.icodem.asciidoc.parser.peg.example.FormattedTextParser
import fr.icodem.asciidoc.parser.peg.runner.ParseRunner
import fr.icodem.asciidoc.parser.peg.runner.ParsingResult
import spock.lang.Specification


class BasicFormattedTextSpec extends Specification {

    ParsingResult parse(String input) {
        FormattedTextParser parser = new FormattedTextParser();
        new ParseRunner(parser, parser.&formattedText)
                .generateStringTree()
                .trace()
                .parse(new StringReader(input), null, null, null);
    }

    def "it should return normal phrase"() {
        given:
            String input = "*normal phrase*";

        when:
            ParsingResult result = parse(input);

        then:
            result.tree == "(formattedText (bold * (text n o r m a l   p h r a s e) *))";
    }

    def "it should return bold phrase"() {
        given:
            String input = "*bold phrase*";

        when:
            ParsingResult result = parse(input);

        then:
            result.tree == "(formattedText (bold * (text b o l d   p h r a s e) *))";
    }

    def "it should return italic phrase"() {
        given:
            String input = "_italic phrase_";

        when:
            ParsingResult result = parse(input);

        then:
            result.tree == "(formattedText (italic _ (text i t a l i c   p h r a s e) _))";
    }

}