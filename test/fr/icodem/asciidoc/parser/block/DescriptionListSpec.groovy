package fr.icodem.asciidoc.parser.block

import fr.icodem.asciidoc.parser.peg.runner.ParsingResult

class DescriptionListSpec extends BlockSpecification {

    def "one item"() {
        given:
        String input = '''\
title:: content
'''
        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(document (content (preamble (block (descriptionList (descriptionListItem (descriptionListItemTitle t i t l e) : :   (descriptionListItemContent c o n t e n t) (nl \\n)))))) (bl <EOI>))"
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
        result.tree == "(document (content (preamble (block (descriptionList (descriptionListItem (descriptionListItemTitle t i t l e   1) : :   (descriptionListItemContent c o n t e n t   1) (nl \\n)) (descriptionListItem (descriptionListItemTitle t i t l e   2) : :   (descriptionListItemContent c o n t e n t   2) (nl \\n)))))) (bl <EOI>))"
//        result.tree == "(document (content (block (labeledList (labeledListItem (labeledListItemTitle t i t l e   1) : :   (labeledListItemContent c o n t e n t   1) (nl \\n)) (labeledListItem (labeledListItemTitle t i t l e   2) : :   (labeledListItemContent c o n t e n t   2 \\n))))) (bl <EOI>))"
    }

    def "content below title"() {
        given:
        String input = '''\
title 1::
content 1
title 2::

content 2
'''

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(document (content (preamble (block (descriptionList (descriptionListItem (descriptionListItemTitle t i t l e   1) : : (nl \\n) (descriptionListItemContent c o n t e n t   1) (nl \\n)) (descriptionListItem (descriptionListItemTitle t i t l e   2) : : (nl \\n) (bl \\n) (descriptionListItemContent c o n t e n t   2) (nl \\n)))))) (bl <EOI>))"
    }

}