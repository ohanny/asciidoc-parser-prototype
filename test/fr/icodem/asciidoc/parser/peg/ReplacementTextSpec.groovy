package fr.icodem.asciidoc.parser.peg

import fr.icodem.asciidoc.parser.peg.runner.ParsingResult

class ReplacementTextSpec extends BaseSpecification {

    def "straight single quotes"() {
        given:
        String input = "'What time is it ?', she asked.";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (text ' W h a t   t i m e   i s   i t   ? ' ,   s h e   a s k e d .))";
    }

    def "straight double quotes"() {
        given:
        String input = "\"What time is it ?\", she asked.";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (text \" W h a t   t i m e   i s   i t   ? \" ,   s h e   a s k e d .))";
    }

    def "single curly quotes"() {
        given:
        String input = "'`What time is it ?`', she asked.";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (text (openingSingleQuote ' `) W h a t   t i m e   i s   i t   ? (closingSingleQuote ` ') ,   s h e   a s k e d .))";
    }

    def "double curly quotes"() {
        given:
        String input = "\"`What time is it ?`\", she asked.";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (text (openingDoubleQuote \" `) W h a t   t i m e   i s   i t   ? (closingDoubleQuote ` \") ,   s h e   a s k e d .))";
    }

    def "double curly quotes with nested single curly quotes"() {
        given:
        String input = "\"`That's a '`magic`' sock.`\"";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (text (openingDoubleQuote \" `) T h a t ' s   a   (openingSingleQuote ' `) m a g i c (closingSingleQuote ` ')   s o c k . (closingDoubleQuote ` \")))";
    }

    def "monospaced text inside quotes"() {
        given:
        String input = "\"``magic``\"";

        when:
        ParsingResult result = parse(input);

        then:
        result.tree == "(formattedText (text (openingDoubleQuote \" `)) (monospace ` (text m a g i c) `) (text (closingDoubleQuote ` \")))";
    }

}