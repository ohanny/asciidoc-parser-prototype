package fr.icodem.asciidoc.parser.block

import fr.icodem.asciidoc.parser.peg.runner.ParsingResult

class AdmonitionParagraphSpec extends BlockSpecification {

    def "note admonition"() {
        given:
        String input = "NOTE: This is a note."

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(document (content (preamble (block (paragraph (admonition N O T E :  ) T h i s   i s   a   n o t e . <EOI>)))))"
    }

    def "tip admonition"() {
        given:
        String input = "TIP: This is a tip."

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(document (content (preamble (block (paragraph (admonition T I P :  ) T h i s   i s   a   t i p . <EOI>)))))"
    }

    def "important admonition"() {
        given:
        String input = "IMPORTANT: This is important."

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(document (content (preamble (block (paragraph (admonition I M P O R T A N T :  ) T h i s   i s   i m p o r t a n t . <EOI>)))))"
    }

    def "caution admonition"() {
        given:
        String input = "CAUTION: Do this with caution."

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(document (content (preamble (block (paragraph (admonition C A U T I O N :  ) D o   t h i s   w i t h   c a u t i o n . <EOI>)))))"
    }

    def "warning admonition"() {
        given:
        String input = "WARNING: This is a warning."

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(document (content (preamble (block (paragraph (admonition W A R N I N G :  ) T h i s   i s   a   w a r n i n g . <EOI>)))))"
    }

}