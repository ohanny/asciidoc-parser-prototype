package fr.icodem.asciidoc.handler.dom

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Document
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.ListingBlock

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
        doc.content != null
        doc.content.sections != null
        doc.content.sections.size() == 1

        doc.content.sections[0].blocks != null
        doc.content.sections[0].blocks.size() == 1
        doc.content.sections[0].blocks[0] != null
        doc.content.sections[0].blocks[0] instanceof ListingBlock
        doc.content.sections[0].blocks[0].text != null
        doc.content.sections[0].blocks[0].text.content == 'int a = 10;\n'
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
        doc.content != null
        doc.content.sections != null
        doc.content.sections.size() == 1

        doc.content.sections[0].blocks != null
        doc.content.sections[0].blocks.size() == 1
        doc.content.sections[0].blocks[0] != null
        doc.content.sections[0].blocks[0] instanceof ListingBlock
        doc.content.sections[0].blocks[0].attributes != null
        doc.content.sections[0].blocks[0].attributes.firstPositionalAttribute == 'source'
        doc.content.sections[0].blocks[0].attributes.secondPositionalAttribute == 'java'
        doc.content.sections[0].blocks[0].text != null
        doc.content.sections[0].blocks[0].text.content == 'int a = 10;\n'
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
        doc.content != null
        doc.content.sections != null
        doc.content.sections.size() == 1

        doc.content.sections[0].blocks != null
        doc.content.sections[0].blocks.size() == 1
        doc.content.sections[0].blocks[0] != null
        doc.content.sections[0].blocks[0] instanceof ListingBlock
        doc.content.sections[0].blocks[0].attributes != null
        doc.content.sections[0].blocks[0].attributes.firstPositionalAttribute == 'source'
        doc.content.sections[0].blocks[0].attributes.secondPositionalAttribute == 'java'
        doc.content.sections[0].blocks[0].text != null
        doc.content.sections[0].blocks[0].text.content == 'int a = 10;\n'

        doc.content.sections[0].blocks[0].callouts != null
        doc.content.sections[0].blocks[0].callouts.size() == 2
        doc.content.sections[0].blocks[0].callouts[0] != null
        doc.content.sections[0].blocks[0].callouts[0].number == 1
        doc.content.sections[0].blocks[0].callouts[0].text == 'Note 1'
        doc.content.sections[0].blocks[0].callouts[1] != null
        doc.content.sections[0].blocks[0].callouts[1].number == 2
        doc.content.sections[0].blocks[0].callouts[1].text == 'Note 2'


    }


}