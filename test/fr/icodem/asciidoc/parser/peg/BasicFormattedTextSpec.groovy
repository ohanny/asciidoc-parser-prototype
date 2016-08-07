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

    def "normal phrase"() {
        given:
            String input = "it's a nice day";

        when:
            ParsingResult result = parse(input);

        then:
            result.tree == "(formattedText (text i t ' s   a   n i c e   d a y))";
    }

    def "bold phrase"() {
        given:
            String input = "*it's a nice day*";

        when:
            ParsingResult result = parse(input);

        then:
            result.tree == "(formattedText (bold * (text i t ' s   a   n i c e   d a y) *))";
    }

    def "italic phrase"() {
        given:
            String input = "_it's a nice day_";

        when:
            ParsingResult result = parse(input);

        then:
            result.tree == "(formattedText (italic _ (text i t ' s   a   n i c e   d a y) _))";
    }

    def "bold word within a phrase"() {
        given:
        String input = "it's a *nice* day";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (text i t ' s   a  ) (bold * (text n i c e) *) (text   d a y))";
    }

    def "italic word within a phrase"() {
        given:
        String input = "it's a _nice_ day";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (text i t ' s   a  ) (italic _ (text n i c e) _) (text   d a y))";
    }

    def "bold italic word within a phrase"() {
        given:
        String input = "it's a *_nice_* day";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (text i t ' s   a  ) (bold * (italic _ (text n i c e) _) *) (text   d a y))";
    }

    def "first letter of 'day' should be bold"() {
        given:
        String input = "it's a nice *d*ay";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (text i t ' s   a   n i c e  ) (bold * (text d) *) (text a y))";
    }

    def "last letter of 'nice' should be bold"() {
        given:
        String input = "it's a nic*e* day";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (text i t ' s   a   n i c) (bold * (text e) *) (text   d a y))";
    }

    def "hyphen before bold word"() {
        given:
        String input = "-*2016*";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (text -) (bold * (text 2 0 1 6) *))";
    }

    def "spaces inside formatting mark"() {
        given:
        String input = "*bold *";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (bold * (text b o l d  ) *))";
    }

    def "hyphen after and before formatting mark"() {
        given:
        String input = "*9*-to-*5*";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (bold * (text 9) *) (text - t o -) (bold * (text 5) *))";
    }

}