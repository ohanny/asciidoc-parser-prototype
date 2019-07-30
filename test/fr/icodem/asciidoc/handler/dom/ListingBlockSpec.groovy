package fr.icodem.asciidoc.handler.dom

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Document
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ListingBlock

class ListingBlockSpec extends DomHandlerBaseSpec {

    def "listing block"() {
        given:
        String input = '''\
= Listing Block

== Section 1
----
int a = 10;
----
'''

        when:
        Document doc = getBuilder().build(input)

        then:
        doc != null
        doc.header != null
        doc.sections != null
        doc.sections.size() == 1

        doc.sections[0].blocks != null
        doc.sections[0].blocks.size() == 1
        doc.sections[0].blocks[0] != null
        doc.sections[0].blocks[0] instanceof ListingBlock
        doc.sections[0].blocks[0].text != null
        doc.sections[0].blocks[0].text.content == 'int a = 10;\n'
    }

    def "listing block with attribute list"() {
        given:
        String input = '''\
= Listing Block

== Section 1

[source,java]
----
int a = 10;
----
'''

        when:
        Document doc = getBuilder().build(input)

        then:
        doc != null
        doc.header != null
        doc.sections != null
        doc.sections.size() == 1

        doc.sections[0].blocks != null
        doc.sections[0].blocks.size() == 1
        doc.sections[0].blocks[0] != null
        doc.sections[0].blocks[0] instanceof ListingBlock
        doc.sections[0].blocks[0].attributeList != null
        doc.sections[0].blocks[0].attributeList.firstPositionalAttribute == 'source'
        doc.sections[0].blocks[0].attributeList.secondPositionalAttribute == 'java'
        doc.sections[0].blocks[0].text != null
        doc.sections[0].blocks[0].text.content == 'int a = 10;\n'
    }

    def "listing block with callouts"() {
        given:
        String input = '''\
= Listing Block

== Section 1

[source,java]
----
int a = 10;
----
<1> Note 1
<2> Note 2
'''

        when:
        Document doc = getBuilder().build(input)

        then:
        doc != null
        doc.header != null
        doc.sections != null
        doc.sections.size() == 1

        doc.sections[0].blocks != null
        doc.sections[0].blocks.size() == 1
        doc.sections[0].blocks[0] != null
        doc.sections[0].blocks[0] instanceof ListingBlock
        doc.sections[0].blocks[0].attributeList != null
        doc.sections[0].blocks[0].attributeList.firstPositionalAttribute == 'source'
        doc.sections[0].blocks[0].attributeList.secondPositionalAttribute == 'java'
        doc.sections[0].blocks[0].text != null
        doc.sections[0].blocks[0].text.content == 'int a = 10;\n'

        doc.sections[0].blocks[0].callouts != null
        doc.sections[0].blocks[0].callouts.size() == 2
        doc.sections[0].blocks[0].callouts[0] != null
        doc.sections[0].blocks[0].callouts[0].number == 1
        doc.sections[0].blocks[0].callouts[0].text == 'Note 1'
        doc.sections[0].blocks[0].callouts[1] != null
        doc.sections[0].blocks[0].callouts[1].number == 2
        doc.sections[0].blocks[0].callouts[1].text == 'Note 2'


    }


}