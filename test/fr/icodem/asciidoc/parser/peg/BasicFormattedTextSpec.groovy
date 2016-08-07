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
            String input = "it's a nice day";

        when:
            ParsingResult result = parse(input);

        then:
            result.tree == "(formattedText (text i t ' s   a   n i c e   d a y))";
    }

    def "it should return bold phrase"() {
        given:
            String input = "*it's a nice day*";

        when:
            ParsingResult result = parse(input);

        then:
            result.tree == "(formattedText (bold * (text i t ' s   a   n i c e   d a y) *))";
    }

    def "it should return italic phrase"() {
        given:
            String input = "_it's a nice day_";

        when:
            ParsingResult result = parse(input);

        then:
            result.tree == "(formattedText (italic _ (text i t ' s   a   n i c e   d a y) _))";
    }

    def "it should return bold word within a phrase"() {
        given:
        String input = "it's a *nice* day";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (text i t ' s   a  ) (bold * (text n i c e) *) (text   d a y))";
    }

    def "it should return italic word within a phrase"() {
        given:
        String input = "it's a _nice_ day";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (text i t ' s   a  ) (italic _ (text n i c e) _) (text   d a y))";
    }

    def "it should return bold italic word within a phrase"() {
        given:
        String input = "it's a *_nice_* day";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (text i t ' s   a  ) (bold * (italic _ (text n i c e) _) *) (text   d a y))";
    }

}