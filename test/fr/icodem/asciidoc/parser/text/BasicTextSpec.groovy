package fr.icodem.asciidoc.parser.text

import fr.icodem.asciidoc.parser.peg.runner.ParsingResult

class BasicTextSpec extends TextSpecification {

    def "basic text"() {
        given:
        String input = "Some text"

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(formattedText (text S o m e   t e x t))"
    }

    def "text like programming instruction"() {
        given:
        String input = "a = b + c;"

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(formattedText (text a   =   b   +   c ;))"
    }

    def "text with monospace inside"() {
        given:
        String input = "`val2 += val1;` équivalent à `val2 = val2 + val1;`"

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(formattedText (monospace ` (text v a l 2   + =   v a l 1 ;) `) (text   é q u i v a l e n t   à  ) (monospace ` (text v a l 2   =   v a l 2   +   v a l 1 ;) `))"
    }

}