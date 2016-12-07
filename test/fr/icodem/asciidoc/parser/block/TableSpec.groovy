package fr.icodem.asciidoc.parser.block

import fr.icodem.asciidoc.parser.peg.runner.ParsingResult
import spock.lang.Ignore

class TableSpec extends BlockSpecification {

    @Ignore("Until grammar is fixed")
    def "simple psv table"() {
        given:
        String input = '''\
|===
|A |B |C
|a |b |c
|1 |2 |3
|===
'''
        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "TO BE DEFINED"
    }

}