package fr.icodem.asciidoc.parser.text

import fr.icodem.asciidoc.parser.peg.runner.ParsingResult

class BasicTextSpec extends TextSpecification {

    def "basic text"() {
        given:
        String input = "Some text"

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(formattedText (text S o m e   t e x t))"
    }

    def "programming instruction"() {
        given:
        String input = "a = b + c;"

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(formattedText (text a   =   b   +   c ;))"
    }

    def "text with monospace inside"() {
        given:
        String input = "`val2 += val1;` équivalent à `val2 = val2 + val1;`"

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(formattedText (monospace ` (text v a l 2   + =   v a l 1 ;) `) (text   é q u i v a l e n t   à  ) (monospace ` (text v a l 2   =   v a l 2   +   v a l 1 ;) `))"
    }

    def "text with special characters"() {
        given:
        String input = "+ - * / % _ # ~ ^"

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(formattedText (text +   -   *   /   %   _   #   ~   ^))"
    }

    def "text with special characters monospaced"() {
        given:
        String input = "`+` `-` `*` `/` `%` `_` `#` `~` `^`"

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(formattedText (monospace ` (text +) `) (text  ) (monospace ` (text -) `) (text  ) (monospace ` (text *) `) (text  ) (monospace ` (text /) `) (text  ) (monospace ` (text %) `) (text  ) (monospace ` (text _) `) (text  ) (monospace ` (text #) `) (text  ) (monospace ` (text ~) `) (text  ) (monospace ` (text ^) `))"
    }

    def "text with xml predefined entity"() {
        given:
        String input = "hello &nbsp; world"

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(formattedText (text h e l l o   (xmlEntity & n b s p ;)   w o r l d))"
    }

    def "text with xml decimal entity"() {
        given:
        String input = "hello &#160; world"

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(formattedText (text h e l l o   (xmlEntity & # 1 6 0 ;)   w o r l d))"
    }

    def "text with xml hexadecimal entity"() {
        given:
        String input = "hello &#xa9; world"

        when:
        ParsingResult result = parse(input)

        then:
        result.tree == "(formattedText (text h e l l o   (xmlEntity & # x a 9 ;)   w o r l d))"
    }

}