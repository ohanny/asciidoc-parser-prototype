package fr.icodem.asciidoc.handler.dom


import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Document
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.ExampleBlock
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Paragraph
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.Table

class TableSpec extends DomHandlerBaseSpec {

    def "one row"() {
        given:
        String input = '''\
= Table

== Section 1
|===

|Cell 1|Cell 2

|===
====
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
        doc.sections[0].blocks[0] instanceof Table

        doc.sections[0].blocks[0].columns != null
        doc.sections[0].blocks[0].columns.size() == 2
        doc.sections[0].blocks[0].columns[0] != null
        doc.sections[0].blocks[0].columns[0].width == 50
        doc.sections[0].blocks[0].columns[1] != null
        doc.sections[0].blocks[0].columns[1].width == 50

        doc.sections[0].blocks[0].header.empty
        doc.sections[0].blocks[0].footer.empty

        doc.sections[0].blocks[0].body != null
        doc.sections[0].blocks[0].body.size() == 1
        doc.sections[0].blocks[0].body[0] != null
        doc.sections[0].blocks[0].body[0].cells != null
        doc.sections[0].blocks[0].body[0].cells.size() == 2
        doc.sections[0].blocks[0].body[0].cells[0] != null
        doc.sections[0].blocks[0].body[0].cells[0].rowspan == 0
        doc.sections[0].blocks[0].body[0].cells[0].colspan == 0
        doc.sections[0].blocks[0].body[0].cells[0].text != null
        doc.sections[0].blocks[0].body[0].cells[0].text.content == 'Cell 1'
        doc.sections[0].blocks[0].body[0].cells[1] != null
        doc.sections[0].blocks[0].body[0].cells[1].rowspan == 0
        doc.sections[0].blocks[0].body[0].cells[1].colspan == 0
        doc.sections[0].blocks[0].body[0].cells[1].text != null
        doc.sections[0].blocks[0].body[0].cells[1].text.content == 'Cell 2'

    }

    def "two rows on consecutive lines"() {
        given:
        String input = '''\
= Table

== Section 1
|===

|Cell 1
|Cell 2

|===
====
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
        doc.sections[0].blocks[0] instanceof Table

        doc.sections[0].blocks[0].columns != null
        doc.sections[0].blocks[0].columns.size() == 1
        doc.sections[0].blocks[0].columns[0] != null
        doc.sections[0].blocks[0].columns[0].width == 100

        doc.sections[0].blocks[0].header.empty
        doc.sections[0].blocks[0].footer.empty

        doc.sections[0].blocks[0].body != null
        doc.sections[0].blocks[0].body.size() == 2
        doc.sections[0].blocks[0].body[0] != null
        doc.sections[0].blocks[0].body[0].cells != null
        doc.sections[0].blocks[0].body[0].cells.size() == 1
        doc.sections[0].blocks[0].body[0].cells[0] != null
        doc.sections[0].blocks[0].body[0].cells[0].rowspan == 0
        doc.sections[0].blocks[0].body[0].cells[0].colspan == 0
        doc.sections[0].blocks[0].body[0].cells[0].text != null
        doc.sections[0].blocks[0].body[0].cells[0].text.content == 'Cell 1'
        doc.sections[0].blocks[0].body[1].cells[0] != null
        doc.sections[0].blocks[0].body[1].cells[0].rowspan == 0
        doc.sections[0].blocks[0].body[1].cells[0].colspan == 0
        doc.sections[0].blocks[0].body[1].cells[0].text != null
        doc.sections[0].blocks[0].body[1].cells[0].text.content == 'Cell 2'

    }

    def "two rows separated by a blank line"() {
        given:
        String input = '''\
= Table

== Section 1
|===

|Cell 1|Cell 2

|Cell 3|Cell 4

|===
====
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
        doc.sections[0].blocks[0] instanceof Table

        doc.sections[0].blocks[0].columns != null
        doc.sections[0].blocks[0].columns.size() == 2
        doc.sections[0].blocks[0].columns[0] != null
        doc.sections[0].blocks[0].columns[0].width == 50
        doc.sections[0].blocks[0].columns[1] != null
        doc.sections[0].blocks[0].columns[1].width == 50

        doc.sections[0].blocks[0].header.empty
        doc.sections[0].blocks[0].footer.empty

        doc.sections[0].blocks[0].body != null
        doc.sections[0].blocks[0].body.size() == 2

        doc.sections[0].blocks[0].body[0] != null
        doc.sections[0].blocks[0].body[0].cells != null
        doc.sections[0].blocks[0].body[0].cells.size() == 2
        doc.sections[0].blocks[0].body[0].cells[0] != null
        doc.sections[0].blocks[0].body[0].cells[0].rowspan == 0
        doc.sections[0].blocks[0].body[0].cells[0].colspan == 0
        doc.sections[0].blocks[0].body[0].cells[0].text != null
        doc.sections[0].blocks[0].body[0].cells[0].text.content == 'Cell 1'
        doc.sections[0].blocks[0].body[0].cells[1] != null
        doc.sections[0].blocks[0].body[0].cells[1].rowspan == 0
        doc.sections[0].blocks[0].body[0].cells[1].colspan == 0
        doc.sections[0].blocks[0].body[0].cells[1].text != null
        doc.sections[0].blocks[0].body[0].cells[1].text.content == 'Cell 2'

        doc.sections[0].blocks[0].body[1] != null
        doc.sections[0].blocks[0].body[1].cells != null
        doc.sections[0].blocks[0].body[1].cells.size() == 2
        doc.sections[0].blocks[0].body[1].cells[0] != null
        doc.sections[0].blocks[0].body[1].cells[0].rowspan == 0
        doc.sections[0].blocks[0].body[1].cells[0].colspan == 0
        doc.sections[0].blocks[0].body[1].cells[0].text != null
        doc.sections[0].blocks[0].body[1].cells[0].text.content == 'Cell 3'
        doc.sections[0].blocks[0].body[1].cells[1] != null
        doc.sections[0].blocks[0].body[1].cells[1].rowspan == 0
        doc.sections[0].blocks[0].body[1].cells[1].colspan == 0
        doc.sections[0].blocks[0].body[1].cells[1].text != null
        doc.sections[0].blocks[0].body[1].cells[1].text.content == 'Cell 4'

    }

    def "implicit header row"() {
        given:
        String input = '''\
= Table

== Section 1
|===
|Title 1|Title 2

|Cell 1|Cell 2

|===
====
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
        doc.sections[0].blocks[0] instanceof Table

        doc.sections[0].blocks[0].header.size() == 1

        doc.sections[0].blocks[0].header[0] != null
        doc.sections[0].blocks[0].header[0].cells != null
        doc.sections[0].blocks[0].header[0].cells.size() == 2
        doc.sections[0].blocks[0].header[0].cells[0] != null
        doc.sections[0].blocks[0].header[0].cells[0].rowspan == 0
        doc.sections[0].blocks[0].header[0].cells[0].colspan == 0
        doc.sections[0].blocks[0].header[0].cells[0].text != null
        doc.sections[0].blocks[0].header[0].cells[0].text.content == 'Title 1'
        doc.sections[0].blocks[0].header[0].cells[1] != null
        doc.sections[0].blocks[0].header[0].cells[1].rowspan == 0
        doc.sections[0].blocks[0].header[0].cells[1].colspan == 0
        doc.sections[0].blocks[0].header[0].cells[1].text != null
        doc.sections[0].blocks[0].header[0].cells[1].text.content == 'Title 2'

        doc.sections[0].blocks[0].footer.empty

        doc.sections[0].blocks[0].body.size() == 1

        doc.sections[0].blocks[0].body[0] != null
        doc.sections[0].blocks[0].body[0].cells != null
        doc.sections[0].blocks[0].body[0].cells.size() == 2
        doc.sections[0].blocks[0].body[0].cells[0] != null
        doc.sections[0].blocks[0].body[0].cells[0].rowspan == 0
        doc.sections[0].blocks[0].body[0].cells[0].colspan == 0
        doc.sections[0].blocks[0].body[0].cells[0].text != null
        doc.sections[0].blocks[0].body[0].cells[0].text.content == 'Cell 1'
        doc.sections[0].blocks[0].body[0].cells[1] != null
        doc.sections[0].blocks[0].body[0].cells[1].rowspan == 0
        doc.sections[0].blocks[0].body[0].cells[1].colspan == 0
        doc.sections[0].blocks[0].body[0].cells[1].text != null
        doc.sections[0].blocks[0].body[0].cells[1].text.content == 'Cell 2'

    }


}