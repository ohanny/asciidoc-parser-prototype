package fr.icodem.asciidoc.parser.block

import fr.icodem.asciidoc.parser.peg.runner.ParsingResult

class AdmonitionBlockSpec extends BlockSpecification {

    def "simple admonition block"() {
        given:
        String input = '''\
[NOTE]
====
a note
====
'''

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(document (content (attributeList [ (positionalAttribute (attributeValue N O T E)) ] \\n) (block (exampleBlock (exampleBlockDelimiter = = = = \\n) (paragraph a   n o t e \\n) (exampleBlockDelimiter = = = = \\n)))) (bl <EOI>))"
    }

    def "admonition block with list inside"() {
        given:
        String input = '''\
[NOTE]
====
a note

* one
* two

====
'''

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(document (content (attributeList [ (positionalAttribute (attributeValue N O T E)) ] \\n) (block (exampleBlock (exampleBlockDelimiter = = = = \\n) (paragraph a   n o t e) (nl \\n) (bl \\n) (list (listItem *   (listItemValue o n e) \\n) (listItem *   (listItemValue t w o) \\n)) (bl \\n) (exampleBlockDelimiter = = = = \\n)))) (bl <EOI>))"
    }

    def "admonition block with simple listing inside"() {
        given:
        String input = '''\
[NOTE]
====
a note

----
println('hello');
----

====
'''

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(document (content (attributeList [ (positionalAttribute (attributeValue N O T E)) ] \\n) (block (exampleBlock (exampleBlockDelimiter = = = = \\n) (paragraph a   n o t e) (nl \\n) (bl \\n) (listingBlock (listingBlockDelimiter - - - - \\n) p r i n t l n ( ' h e l l o ' ) ; \\n (listingBlockDelimiter - - - - \\n)) (bl \\n) (exampleBlockDelimiter = = = = \\n)))) (bl <EOI>))"
    }

    def "admonition block with listing inside"() {
        given:
        String input = '''\
[NOTE]
====
a note

[source]
----
println('hello');
----

====
'''

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(document (content (attributeList [ (positionalAttribute (attributeValue N O T E)) ] \\n) (block (exampleBlock (exampleBlockDelimiter = = = = \\n) (paragraph a   n o t e) (nl \\n) (bl \\n) (attributeList [ (positionalAttribute (attributeValue s o u r c e)) ] \\n) (listingBlock (listingBlockDelimiter - - - - \\n) p r i n t l n ( ' h e l l o ' ) ; \\n (listingBlockDelimiter - - - - \\n)) (bl \\n) (exampleBlockDelimiter = = = = \\n)))) (bl <EOI>))"
    }


}