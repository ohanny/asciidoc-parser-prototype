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

}