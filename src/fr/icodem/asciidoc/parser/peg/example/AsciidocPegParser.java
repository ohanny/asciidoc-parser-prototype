package fr.icodem.asciidoc.parser.peg.example;

import fr.icodem.asciidoc.parser.peg.BaseParser;
import fr.icodem.asciidoc.parser.peg.Chars;
import fr.icodem.asciidoc.parser.peg.ParseRunner;
import fr.icodem.asciidoc.parser.peg.ParsingResult;
import fr.icodem.asciidoc.parser.peg.listeners.ToStringAnalysisBuilder;
import fr.icodem.asciidoc.parser.peg.listeners.ToStringTreeBuilder;
import fr.icodem.asciidoc.parser.peg.rules.Rule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AsciidocPegParser extends BaseParser {

    public AsciidocParsingResult parse(String text) {

        ToStringTreeBuilder treeBuilder = new ToStringTreeBuilder();

        AsciidocParsingResult result = new AsciidocParsingResult();
        result.matched = new ParseRunner(document()).parse(text, treeBuilder, new ToStringAnalysisBuilder()).matched;
        result.tree = treeBuilder.getStringTree();

        return result;
    }


    // rules
    private Rule document() {
        return node("document", sequence(optional(header()), optional(bl())));
    }

    private Rule header() {
        return node("header", sequence(documentTitle()));
    }

    private Rule documentTitle() {
        return node("documentTitle",
                sequence(ch('='), ch(' '), optional(title()), optional(blank()), firstOf(newLine(), eoi())));
    }

    private Rule title() {
        return node("title", sequence(noneOf(' ', '\t', '\n', Chars.EOI), zeroOrMore(noneOf('\n', Chars.EOI))));
    }

    private Rule bl() {
        return node("bl", sequence(optional(blank()), firstOf(newLine(), eoi())));
    }

    // utils rules
    private Rule blank() {
        return oneOrMore(firstOf(' ', '\t'));
    }

    private Rule newLine() {
        return sequence(optional('\r'), ch('\n'));
    }

    /*
    bl [boolean withEOF]
    : {isFirstCharInLine()}? (SP|TAB)* (CR? NL|{$withEOF}? EOF)
    ;

     */

}
