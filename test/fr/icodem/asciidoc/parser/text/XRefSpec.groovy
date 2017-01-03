package fr.icodem.asciidoc.parser.text

import fr.icodem.asciidoc.parser.peg.runner.ParsingResult

class XRefSpec extends TextSpecification {

    def "xref using angle bracket syntax"() {
        given:
        String input = "<<tigers>>"

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(formattedText (xref < < (xrefValue t i g e r s) > >))"
    }

    def "xref using angle bracket syntax with label"() {
        given:
        String input = "<<tigers,About Tigers>>"

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(formattedText (xref < < (xrefValue t i g e r s) , (xrefLabel A b o u t   T i g e r s) > >))"
    }

}