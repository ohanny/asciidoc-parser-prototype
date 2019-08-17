package fr.icodem.asciidoc.handler.dom

import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.DocumentModelBuilder
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Author
import fr.icodem.asciidoc.parser.peg.example.asciidoc.dom.model.block.Document

class AuthorSpec extends DomHandlerBaseSpec {

    def "header with one author name"() {
        given:
        DocumentModelBuilder builder = getBuilder()
        String input = '''\
= Hello, AsciiDoc!
John Doe
'''

        when:
        Document doc = builder.build(input)

        then:
        doc != null
        doc.header != null
        doc.header.authors != null
        doc.header.authors.size() == 1

        Author author = doc.header.authors.get(0)
        author.fullName == "John Doe"
        author.firstName == "John"
        author.lastName == "Doe"
        author.email == null
        author.initials == "jd"
    }

    def "header with one author name and author address"() {
        given:
        DocumentModelBuilder builder = getBuilder()
        String input = '''\
= Hello, AsciiDoc!
John Doe <jd@mail.com>
'''

        when:
        Document doc = builder.build(input)

        then:
        doc != null
        doc.header != null
        doc.header.authors != null
        doc.header.authors.size() == 1

        Author author = doc.header.authors.get(0)
        author.fullName == "John Doe"
        author.firstName == "John"
        author.lastName == "Doe"
        author.email == "jd@mail.com"
        author.initials == "jd"
    }

    def "header with two authors name and address"() {
        given:
        DocumentModelBuilder builder = getBuilder()
        String input = '''\
= Hello, AsciiDoc!
John Doe <jd@mail.com>; Janie Roe <janie@mail.com>
'''

        when:
        Document doc = builder.build(input)

        then:
        doc != null
        doc.header != null
        doc.header.authors != null
        doc.header.authors.size() == 2

        Author author1 = doc.header.authors.get(0)
        author1.fullName == "John Doe"
        author1.firstName == "John"
        author1.lastName == "Doe"
        author1.email == "jd@mail.com"
        author1.initials == "jd"

        Author author2 = doc.header.authors.get(1)
        author2.fullName == "Janie Roe"
        author2.firstName == "Janie"
        author2.lastName == "Roe"
        author2.email == "janie@mail.com"
        author2.initials == "jr"
    }

}