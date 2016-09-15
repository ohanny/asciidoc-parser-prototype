package fr.icodem.asciidoc.parser.block

import fr.icodem.asciidoc.parser.peg.runner.ParsingResult

class AdmonitionSpec extends BlockBaseSpecification {

    def "note admonition"() {
        given:
        String input = "NOTE: This is a note.";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(document (content (block (paragraph (admonition N O T E :  ) T h i s   i s   a   n o t e . <EOF>))))";
    }

    def "tip admonition"() {
        given:
        String input = "TIP: This is a tip.";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(document (content (block (paragraph (admonition T I P :  ) T h i s   i s   a   t i p . <EOF>))))";
    }

    def "important admonition"() {
        given:
        String input = "IMPORTANT: This is important.";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(document (content (block (paragraph (admonition I M P O R T A N T :  ) T h i s   i s   i m p o r t a n t . <EOF>))))";
    }

    def "caution admonition"() {
        given:
        String input = "CAUTION: Do this with caution.";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(document (content (block (paragraph (admonition C A U T I O N :  ) D o   t h i s   w i t h   c a u t i o n . <EOF>))))";
    }

    def "warning admonition"() {
        given:
        String input = "WARNING: This is a warning.";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(document (content (block (paragraph (admonition W A R N I N G :  ) T h i s   i s   a   w a r n i n g . <EOF>))))";
    }

}