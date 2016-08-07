package fr.icodem.asciidoc.parser.peg

import fr.icodem.asciidoc.parser.peg.runner.ParsingResult

class BasicFormattedTextSpec extends BaseSpecification {

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

    def "subscript character"() {
        given:
            String input = "the H~2~O formula";

        when:
            ParsingResult result = parse(input);

        then:
            result.tree == "(formattedText (text t h e   H) (subscript ~ (text 2) ~) (text O   f o r m u l a))";
    }

    def "subscript word"() {
        given:
            String input = "the CO~2 (aq)~ formula";

        when:
            ParsingResult result = parse(input);

        then:
            result.tree == "(formattedText (text t h e   C O) (subscript ~ (text 2   ( a q )) ~) (text   f o r m u l a))";
    }

    def "superscript character"() {
        given:
            String input = "the E=MC^2^ formula";

        when:
            ParsingResult result = parse(input);

        then:
            result.tree == "(formattedText (text t h e   E = M C) (superscript ^ (text 2) ^) (text   f o r m u l a))";
    }

    def "superscript word"() {
        given:
            String input = "the 1^rst^ day";

        when:
            ParsingResult result = parse(input);

        then:
            result.tree == "(formattedText (text t h e   1) (superscript ^ (text r s t) ^) (text   d a y))";
    }

    def "monospace phrase"() {
        given:
            String input = "`it's a nice day`";

        when:
            ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (monospace ` (text i t ' s   a   n i c e   d a y) `))";
    }

    def "monospace word within a phrase"() {
        given:
            String input = "it's a `nice` day";

        when:
            ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (text i t ' s   a  ) (monospace ` (text n i c e) `) (text   d a y))";
    }

    def "monospace bold words within a phrase"() {
        given:
            String input = "Enter the `*adb devices*` command";

        when:
            ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (text E n t e r   t h e  ) (monospace ` (bold * (text a d b   d e v i c e s) *) `) (text   c o m m a n d))";
    }

    def "monospace italic words within a phrase"() {
        given:
            String input = "Enter the `_adb devices_` command";

        when:
            ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (text E n t e r   t h e  ) (monospace ` (italic _ (text a d b   d e v i c e s) _) `) (text   c o m m a n d))";
    }

}