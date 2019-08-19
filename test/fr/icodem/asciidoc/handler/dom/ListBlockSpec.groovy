package fr.icodem.asciidoc.handler.dom


import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Document
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ElementType
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.ListBlock
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Paragraph

class ListBlockSpec extends DomHandlerBaseSpec {

    def "simple unordered list"() {
        given:
        String input = '''\
= Unordered List

== Section 1

* One
* Two
* Three
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

        doc.content.sections[0].blocks[0] instanceof ListBlock
        ListBlock block = doc.content.sections[0].blocks[0]
        block.title == null
        block.type == ElementType.UnorderedList
        block.items != null
        block.items.size() == 3

        block.items[0].text.source == 'One'
        block.items[0].type == ElementType.ListItem
        block.items[1].text.source == 'Two'
        block.items[1].type == ElementType.ListItem
        block.items[2].text.source == 'Three'
        block.items[2].type == ElementType.ListItem

    }

    def "a simple ordered list"() {
        given:
        String input = '''\
= Ordered List

== Section 1

. One
. Two
. Three
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

        doc.content.sections[0].blocks[0] instanceof ListBlock
        ListBlock block = doc.content.sections[0].blocks[0]
        block.title == null
        block.type == ElementType.OrderedList
        block.items != null
        block.items.size() == 3

        block.items[0].text.source == 'One'
        block.items[0].type == ElementType.ListItem
        block.items[1].text.source == 'Two'
        block.items[1].type == ElementType.ListItem
        block.items[2].text.source == 'Three'
        block.items[2].type == ElementType.ListItem

    }

    def "two levels unordered list"() {
        given:
        String input = '''\
= Unordered List

== Section 1

* One
** One A
** One B
* Two
* Three
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

        doc.content.sections[0].blocks[0] instanceof ListBlock
        ListBlock block = doc.content.sections[0].blocks[0]
        block.title == null
        block.type == ElementType.UnorderedList
        block.items != null
        block.items.size() == 3

        block.items[0].text.source == 'One'
        block.items[0].type == ElementType.ListItem

        block.items[0].blocks != null
        block.items[0].blocks.size() == 1
        block.items[0].blocks[0] instanceof ListBlock
        block.items[0].blocks[0].type == ElementType.UnorderedList
        block.items[0].blocks[0].items != null
        block.items[0].blocks[0].items.size() == 2
        block.items[0].blocks[0].items[0] != null
        block.items[0].blocks[0].items[0].text.source == 'One A'
        block.items[0].blocks[0].items[1] != null
        block.items[0].blocks[0].items[1].text.source == 'One B'

        block.items[1].text.source == 'Two'
        block.items[1].type == ElementType.ListItem
        block.items[2].text.source == 'Three'
        block.items[2].type == ElementType.ListItem

    }

    def "unordered list with nested paragraph"() {
        given:
        String input = '''\
= Unordered List

== Section 1

* One
** One A
** One B
* Two
+
Some text

* Three
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

        doc.content.sections[0].blocks[0] instanceof ListBlock
        ListBlock block = doc.content.sections[0].blocks[0]
        block.title == null
        block.type == ElementType.UnorderedList
        block.items != null
        block.items.size() == 3

        block.items[0].text.source == 'One'
        block.items[0].type == ElementType.ListItem

        block.items[0].blocks != null
        block.items[0].blocks.size() == 1
        block.items[0].blocks[0] instanceof ListBlock
        block.items[0].blocks[0].type == ElementType.UnorderedList
        block.items[0].blocks[0].items != null
        block.items[0].blocks[0].items.size() == 2
        block.items[0].blocks[0].items[0] != null
        block.items[0].blocks[0].items[0].text.source == 'One A'
        block.items[0].blocks[0].items[1] != null
        block.items[0].blocks[0].items[1].text.source == 'One B'

        block.items[1].text.source == 'Two'
        block.items[1].type == ElementType.ListItem
        block.items[1].blocks != null
        block.items[1].blocks.size() == 1
        block.items[1].blocks[0] instanceof Paragraph
        block.items[1].blocks[0].text.source == 'Some text'

        block.items[2].text.source == 'Three'
        block.items[2].type == ElementType.ListItem

    }


}