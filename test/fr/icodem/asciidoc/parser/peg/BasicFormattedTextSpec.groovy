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
            String input = "normal phrase";

        when:
            ParsingResult result = parse(input);

        then:
            result.tree == "(formattedText (text n o r m a l   p h r a s e))";
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

    def "it should return bold word within a phrase"() {
        given:
        String input = "a *bold* word";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (text a  ) (bold * (text b o l d) *) (text   w o r d))";
    }

    def "it should return italic word within a phrase"() {
        given:
        String input = "a _bold_ word";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (text a  ) (italic _ (text b o l d) _) (text   w o r d))";
    }

    def "it should return bold italic word within a phrase"() {
        given:
        String input = "a *_bold_* word";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (text a  ) (bold * (italic _ (text b o l d) _) *) (text   w o r d))";
    }

}