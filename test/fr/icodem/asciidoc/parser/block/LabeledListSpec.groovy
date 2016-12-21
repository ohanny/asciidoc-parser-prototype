package fr.icodem.asciidoc.parser.block

import fr.icodem.asciidoc.parser.peg.runner.ParsingResult

class LabeledListSpec extends BlockSpecification {

    def "one item"() {
        given:
        String input = '''\
title:: content
'''
        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(document (content (block (labeledList (labeledListItem (labeledListItemTitle t i t l e) : :   (labeledListItemContent c o n t e n t \\n))))) (bl <EOF>))"
    }

    def "two items"() {
        given:
        String input = '''\
title 1:: content 1
title 2:: content 2
'''

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(document (content (block (labeledList (labeledListItem (labeledListItemTitle t i t l e   1) : :   (labeledListItemContent c o n t e n t   1 \\n)) (labeledListItem (labeledListItemTitle t i t l e   2) : :   (labeledListItemContent c o n t e n t   2 \\n))))) (bl <EOF>))"
    }

}