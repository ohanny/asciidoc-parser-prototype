package fr.icodem.asciidoc.parser.peg.example;

import fr.icodem.asciidoc.parser.peg.BaseParser;
import fr.icodem.asciidoc.parser.peg.Chars;
import fr.icodem.asciidoc.parser.peg.ParseRunner;
import fr.icodem.asciidoc.parser.peg.ParsingResult;
import fr.icodem.asciidoc.parser.peg.listeners.ToStringAnalysisBuilder;
import fr.icodem.asciidoc.parser.peg.listeners.ToStringTreeBuilder;
import fr.icodem.asciidoc.parser.peg.rules.Rule;
import fr.icodem.asciidoc.parser.peg.rules.RuleSupplier;

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
        //return node("document", sequence(optional(header()), optional(bl(true))));
        return node("document",
                sequence(optional(
                           sequence(
                               header(),
                               zeroOrMore(firstOf(
                                   sequence(isCurrentCharNotEOI(), bl(false)),
                                   nl()
                               ))
                           )
                        ), optional(content()), optional(bl(true))));
    }

    /*
    document
    : ({!isCurrentCharEOF()}? bl[false]
      |multiComment
      |singleComment
      )*
      (header ({!isCurrentCharEOF()}? bl[false]
               |nl
               |multiComment
               |singleComment
              )* preamble?)?
      content? bl[true]?
    ;

     */

    private Rule header() {
        return node("header", sequence(documentTitle()));
    }

    private Rule content() {
        return node("content", oneOrMore(firstOf(
                sequence(isCurrentCharNotEOI(), bl(false)),
                horizontalRule(),
                attributeEntry(),
                section(),
                nl()
        )));
    }

    /*
    content
    : ({!isCurrentCharEOF()}? bl[false]
      |horizontalRule
      |attributeEntry
      |attributeList
      |anchor
      |blockTitle
      |blockMacro
      |section
      |block[false]
      |nl
      )+
    ;

     */



    private Rule documentTitle() {
        return node("documentTitle",
                sequence(ch('='), ch(' '), optional(title()), optional(blank()), firstOf(newLine(), eoi())));
    }

    private Rule title() {
        return node("title", sequence(noneOf(" \n\t\r"), zeroOrMore(noneOf('\n'))));
    }

    private Rule section() {
        return node("section", sequence(
                sectionTitle(),
                zeroOrMore(firstOf(
                        sequence(isCurrentCharNotEOI(), bl(false)),
                        nl(),
                        attributeEntry()
                ))
        ));
    }

    /*
        section
    : sectionTitle ({!isCurrentCharEOF()}? bl[false]
                    |nl
                    |attributeEntry
                    |attributeList
                    |block[false])*
    ;

     */

    private Rule sectionTitle() {
        return node("sectionTitle", sequence(
                oneOrMore('='), oneOrMore(blank()), title(),
                zeroOrMore(blank()), firstOf(newLine(), eoi())
        ));
    }

    /*

sectionTitle :
    EQ+ (SP|TAB)+ title (SP|TAB)* (CR? NL|EOF)
    ;

     */

    private Rule bl(boolean withEOI) {
        Rule setWithEOI = () -> ctx -> {
            ctx.setAttribute("withEOI", withEOI);
            return true;
        };

        return wrap(setWithEOI, bl());
    }
    private Rule bl() {
        if (isCached("bl")) return cached("bl");
        Rule checkWithEOI = () -> ctx -> ctx.getBooleanAttribute("withEOI");
        return node("bl", sequence(
                isNextCharAtBeginningOfLine(),
                optional(blank()),
                firstOf(newLine(), sequence(checkWithEOI, eoi()))
        ));
    }
    /* TODO withEOF not implemented
    bl [boolean withEOF]
    : {isFirstCharInLine()}? (SP|TAB)* (CR? NL|{$withEOF}? EOF)
    ;
     */


    private Rule nl() {
        if (isCached("nl")) return cached("nl");
        return node("nl", newLine());
    }
    /*
    nl
    : CR? NL
    ;

     */


    private Rule horizontalRule() {
        return node("horizontalRule", sequence(string("'''"), optional(blank()), firstOf(newLine(), eoi())));
    }

    private Rule attributeName() {
        return node("attributeName", oneOrMore(noneOf("\r\n\t :!")));
    }

    /*
    attributeName
    : (OTHER
      |ALOWER
      |ELOWER
      |HLOWER
      |LLOWER
      |MLOWER
      |DLOWER
      |SLOWER
      |VLOWER
      |DIGIT
      )+
    ;

     */

    private Rule attributeEntry() {
        return node("attributeEntry", sequence(
                ch(':'), optional('!'), attributeName(), optional('!'), ch(':'),
                zeroOrMore(blank()), optional(attributeValueParts()), zeroOrMore(blank()),
                firstOf(newLine(), eoi())
        ));
    }

    private Rule attributeValueParts() {
        return node("attributeValueParts", sequence(attributeValuePart(),
                zeroOrMore(sequence(ch('+'), ch('\n'), zeroOrMore(blank()), attributeValuePart()))));
    }

    private Rule attributeValuePart() {
        return node("attributeValuePart", oneOrMore(noneOf("\r\n+")));
    }

    /*
    attributeEntry
    : COLON BANG? attributeName BANG? COLON SP* attributeValueParts? (SP|TAB)* (CR? NL|EOF)
    ;

attributeValueParts
    : attributeValuePart (PLUS NL SP* attributeValuePart)*
    ;

attributeValuePart
    : (OTHER
      |ALOWER
      |ELOWER
      |HLOWER
      |LLOWER
      |MLOWER
      |DLOWER
      |SLOWER
      |VLOWER
      |DIGIT
      )+
    ;

     */

    // utils rules
    private Rule blank() {
        return oneOrMore(firstOf(' ', '\t'));
    }

    private Rule newLine() {
        return sequence(optional('\r'), ch('\n'));
    }

    private Rule isFirstCharInLine() {// TODO store rule in cache
        //return () -> ctx -> ctx.getPositionInLine() == 0;
        return () -> ctx -> {
            System.out.println("POS IN LINE => " + ctx.getPositionInLine());
            return ctx.getPositionInLine() == 0;
        };
        //return node("isFirstCharInLine", () -> ctx -> ctx.getPositionInLine() == 0);
    }

    private Rule isNextCharAtBeginningOfLine() {
        return test(sequence(any(), isFirstCharInLine()));
    }

    private Rule isCurrentCharNotEOI() {
        return testNot(ch(Chars.EOI));
    }

}
