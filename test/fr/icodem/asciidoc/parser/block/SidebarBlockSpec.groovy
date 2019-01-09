package fr.icodem.asciidoc.parser.block

import fr.icodem.asciidoc.parser.peg.runner.ParsingResult

class SidebarBlockSpec extends BlockSpecification {

    def "simple sidebar"() {
        given:
        String input = '''\
****
some content
****
'''
        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(document (content (block (sidebarBlock (sidebarBlockDelimiter * * * * \\n) (paragraph s o m e   c o n t e n t \\n) (sidebarBlockDelimiter * * * * \\n)))) (bl <EOI>))"
    }

    def "sidebar with list"() { // TODO new line required after list ?
        given:
        String input = '''\
****
* item 1
* item 2

****
'''
        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(document (content (block (sidebarBlock (sidebarBlockDelimiter * * * * \\n) (list (listItem *   (listItemValue i t e m   1) \\n) (listItem *   (listItemValue i t e m   2) \\n)) (bl \\n) (sidebarBlockDelimiter * * * * \\n)))) (bl <EOI>))"
    }

}