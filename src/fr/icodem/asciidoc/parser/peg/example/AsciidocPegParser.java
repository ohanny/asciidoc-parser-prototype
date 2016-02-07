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
                sequence(
                        zeroOrMore(firstOf(
                                sequence(isCurrentCharNotEOI(), bl(false)),
                                multiComment(),
                                singleComment()
                        )),
                        optional(
                           sequence(
                               header(),
                               zeroOrMore(firstOf(
                                   sequence(isCurrentCharNotEOI(), bl(false)),
                                   nl()
                               )),
                               optional(preamble())
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
        return node("header", sequence(
                documentTitle(),
                zeroOrMore(firstOf(multiComment(), singleComment())),
                optional(sequence(
                        authors(),
                        zeroOrMore(firstOf(multiComment(), singleComment()))
                )),
                zeroOrMore(attributeEntry())
        ));
    }



    /*
    header
    : documentTitle
      (multiComment|singleComment)*
      (authors
        (multiComment|singleComment)*
        (attributeEntry|revisionInfo)?
      )?
      attributeEntry*
    ;

     */

    private Rule preamble() {
        return node("preamble", sequence(
                block(false),
                zeroOrMore(firstOf(
                        sequence(isCurrentCharNotEOI(), bl(false)),
                        nl(),
                        block(false)
                ))
        ));
    }

    /*
    preamble
    :       //(attributeList   // TODO
            //|anchor
            //|blockTitle
            //|blockMacro
            //)

      block[false]
      ({!isCurrentCharEOF()}? bl[false]|nl|block[false])*
    ;

     */

    private Rule block(boolean fromList) {
        Rule setFromList = () -> ctx -> {
            ctx.setAttribute("fromList", fromList);// TODO create standard rule ?
            return true;
        };

        return wrap(setFromList, block());
    }

    private Rule block() {
        return node("block", firstOf(
                multiComment(),
                singleComment(),
                list(),
                sourceBlock(),
                literalBlock(),
                sequence(paragraph(), optional(nl()))
        ));
    }

    /*
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

     */


    /*
    block[boolean fromList]       // argument 'fromList' indicates that block is attached to a list item
    : (multiComment
      |singleComment
      |list
      |sourceBlock
      |literalBlock
      |table
      |paragraph[$fromList] nl?
      )
    ;

     */

    private Rule anchor() {
        return node("anchor", sequence(
                test(sequence(any(), isFirstCharInLine())), // TODO replace
                ch('['), ch('['), // TODO ntimes
                anchorId(),
                optional(sequence(ch(','), anchorLabel())),
                ch(']'), ch(']'), // TODO ntimes
                optional(newLine())
        ));
    }

    private Rule anchorId() {
        return node("anchorId", oneOrMore(noneOf(",[]\r\n")));
    }

    private Rule anchorLabel() {
        return node("anchorLabel", oneOrMore(noneOf("[]\r\n")));
    }

    /*
    anchor
    : {isFirstCharInLine()}?
      LSBRACK LSBRACK anchorId
      (COMMA anchorLabel)?
      RSBRACK RSBRACK NL?
    ;

anchorId
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

anchorLabel
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
      |SP
      |EQ
      |SLASH
      |COMMA
      )+
    ;

     */

    private Rule paragraph() {
        return node("paragraph", sequence(
                testNot(sectionTitle()),
                oneOrMore(firstOf(
                        noneOf("= \t/+\r\n"),
                        blank(),
                        sequence(isBlankInParagraph(), newLine()),
                        sequence(isNotStartOfComment(), ch('/')),
                        ch('+'), // TODO add from list
                        ch('=')
                )),
                optional(eoi())
        ));
    }

    private Rule isBlankInParagraph() {
        return testNot(sequence(any(), bl(true)));
    }

    /*
paragraph [boolean fromList] // argument 'fromList' indicates that paragraph is attached to a list item
    : {isStartOfParagraph()}?
      (OTHER
      |ALOWER
      |ELOWER
      |HLOWER
      |LLOWER
      |MLOWER
      |DLOWER
      |SLOWER
      |VLOWER
      |DIGIT
      |{isBlankInParagraph()}? SP
      |{isBlankInParagraph()}? TAB
      |EQ
      |{!isStartOfComment()}? SLASH
      |COMMA
      |LSBRACK
      |RSBRACK
      |LABRACK
      |RABRACK
      |CARET
      |MINUS
      |{isPlusInParagraph($fromList)}? PLUS
      |DOT
      |COLON
      |SEMICOLON
      |BANG
      |TIMES
      |{isBlankInParagraph()}? NL
      )+ EOF?
    ;

     */

    private Rule content() {
        return node("content", oneOrMore(firstOf(
                sequence(isCurrentCharNotEOI(), bl(false)),
                horizontalRule(),
                attributeEntry(),
                anchor(),
                section(),
                block(false),
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
                        attributeEntry(),
                        block(false)
                ))
        ));
    }

    /*
        section
    : sectionTitle ({!isCurrentCharEOF()}? bl[false]
                    |nl
                    |attributeEntry
                    |attributeList // TODO
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
        return node("attributeValuePart", oneOrMore(noneOf("\r\n\t+")));
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

    private Rule authors() {
        return node("authors", sequence(author(), zeroOrMore(sequence(ch(';'), author())), optional(blank()), firstOf(newLine(), eoi())));
    }

    private Rule author() {
        return node("author", sequence(authorName(), optional(sequence(ch('<'), authorAddress(), ch('>')))));
    }

    private Rule authorName() {
        return node("authorName", oneOrMore(noneOf(":<>{}[]=\r\n\t")));
    }

    private Rule authorAddress() {
        return node("authorAddress", oneOrMore(noneOf("<>{}[]=\r\n\t")));
    }

    /*
    authors
    : author
      (SEMICOLON author)*
      (SP|TAB)* (CR? NL|EOF)
    ;

author
    : authorName (LABRACK authorAddress RABRACK)?
    ;

authorName
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
      |SP
      |MINUS
      |DOT
      )+
    ;

authorAddress
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
      |MINUS
      |SLASH
      |DOT
      )+
    ;

     */

    private Rule singleComment() {
        return node("singleComment", sequence(
                test(sequence(any(), isFirstCharInLine())),
                ch('/'), ch('/'),// TODO ntimes
                zeroOrMore(noneOf("\r\n")),
                firstOf(newLine(), eoi())// TODO replace
        ));
    }


    /*
    singleComment
    : {isFirstCharInLine()}?
      SLASH SLASH
      (OTHER
      |ALOWER
      |ELOWER
      |HLOWER
      |LLOWER
      |MLOWER
      |DLOWER
      |SLOWER
      |VLOWER
      |DIGIT
      |SP
      |EQ
      |SLASH
      |COMMA
      |LSBRACK
      |RSBRACK
      |LABRACK
      |RABRACK
      |CARET
      |MINUS
      |PLUS
      |DOT
      |COLON
      |SEMICOLON
      |BANG
      |TIMES
      )*
      (CR? NL|EOF)
    ;
*/

    private Rule multiComment() {
        return node("multiComment", sequence(
                multiCommentDelimiter(),
                zeroOrMore(firstOf(
                    noneOf("/"),
                    sequence(testNot(multiCommentDelimiter()), ch('/'))
                )),
                multiCommentDelimiter()
        ));
    }

    /*
multiComment
    : multiCommentDelimiter
      (OTHER
      |ALOWER
      |ELOWER
      |HLOWER
      |LLOWER
      |MLOWER
      |DLOWER
      |SLOWER
      |VLOWER
      |DIGIT
      |SP
      |EQ
      |SLASH
      |COMMA
      |LSBRACK
      |RSBRACK
      |LABRACK
      |RABRACK
      |CARET
      |MINUS
      |PLUS
      |DOT
      |COLON
      |SEMICOLON
      |BANG
      |TIMES
      |QUOTE
      |NL
      )*?
      multiCommentDelimiter
    ;
*/


/*
multiCommentDelimiter
    : {isFirstCharInLine()}?
      SLASH SLASH SLASH SLASH (SP|TAB)* (CR? NL|EOF)
    ;

     */


    private Rule multiCommentDelimiter() {
        return node("multiCommentDelimiter", sequence(
                test(sequence(any(), isFirstCharInLine())), // TODO add nextCharIsBeginOfLine
                ch('/'), ch('/'), ch('/'), ch('/'), // TODO add ntimes rule
                zeroOrMore(blank()),// TODO blanks
                firstOf(newLine(), eoi()) // TODO newLineOrEOI
        ));
    }

    private Rule list() {
        return node("list", sequence(
                listItem(),
                zeroOrMore(sequence(
                        zeroOrMore(firstOf(sequence(isCurrentCharNotEOI(), bl(false)))),// TODO add attribute list
                        listItem()
                ))
        ));
    }

    /*
    list
    : listItem
      (({!isCurrentCharEOF()}? bl[false]|attributeList)* listItem)*
    ;

listItem
    : (TIMES+|DOT+) SP listItemValue (CR? NL listContinuation*|EOF)
    ;
*/
    private Rule listItem() {
        return node("listItem", sequence(
                firstOf(oneOrMore('*'), oneOrMore('.')),
                ch(' '), // TODO replace with blank rule
                listItemValue(),
                firstOf(
                        sequence(newLine(), zeroOrMore(listContinuation())),
                        eoi()
                )
        ));
    }

    private Rule listItemValue() {
        return node("listItemValue", zeroOrMore(firstOf(
                noneOf("\r\n")  // TODO {isNewLineInListItemValue()}?
        )));
    }

    /*
listItemValue
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
      |SP
      |EQ
      |SLASH
      |COMMA
      |LSBRACK
      |RSBRACK
      |LABRACK
      |RABRACK
      |CARET
      |MINUS
      |PLUS
      |TIMES
      |DOT
      |COLON
      |SEMICOLON
      |BANG
      |{isNewLineInListItemValue()}? (CR? NL)
      )*
    ;

listContinuation
    : PLUS (SP|TAB)* CR? NL block[true]
    ;

     */

    private Rule listContinuation() {
        Rule setFromList = () -> ctx -> {
            ctx.setAttribute("fromList", true);// TODO create standard rule ?
            return true;
        };

        return node("listContinuation", sequence(
                ch('+'), optional(blank()), newLine(), wrap(setFromList, proxy("block"))
        ));
    }

    /*
    sourceBlockDelimiter
    : {isFirstCharInLine()}?
      MINUS MINUS MINUS MINUS (SP|TAB)* (CR? NL|EOF)
    ;

     */

    private Rule sourceBlock() {
        return node("sourceBlock", sequence(
                sourceBlockDelimiter(),
                zeroOrMore(firstOf(
                        noneOf("-"),
                        sequence(testNot(sourceBlockDelimiter()), ch('-'))
                )),
                sourceBlockDelimiter()
        ));

    }

    private Rule sourceBlockDelimiter() {
        return node("sourceBlockDelimiter", sequence(
                    test(sequence(any(), isFirstCharInLine())), // TODO replace
                    ch('-'), ch('-'), ch('-'), ch('-'), // TODO ntimes
                    optional(blank()), // TODO replace with blanks
                    firstOf(newLine(), eoi())
                ));
    }

    private Rule literalBlock() {
        return node("literalBlock", sequence(
                literalBlockDelimiter(),
                zeroOrMore(firstOf(
                        noneOf("."),
                        sequence(testNot(literalBlockDelimiter()), ch('.'))
                )),
                literalBlockDelimiter()
        ));

    }


    private Rule literalBlockDelimiter() {
        return node("literalBlockDelimiter", sequence(
                    test(sequence(any(), isFirstCharInLine())), // TODO replace
                    ch('.'), ch('.'), ch('.'), ch('.'), // TODO ntimes
                    optional(blank()), // TODO replace with blanks
                    firstOf(newLine(), eoi())
                ));
    }

    /*
    sourceBlock
    : sourceBlockDelimiter
      (OTHER
      |ALOWER
      |ELOWER
      |HLOWER
      |LLOWER
      |MLOWER
      |DLOWER
      |SLOWER
      |VLOWER
      |DIGIT
      |SP
      |EQ
      |SLASH
      |COMMA
      |LSBRACK
      |RSBRACK
      |LABRACK
      |RABRACK
      |CARET
      |MINUS
      |PLUS
      |TIMES
      |DOT
      |COLON
      |SEMICOLON
      |BANG
      |QUOTE
      |NL
      )*?
      sourceBlockDelimiter
    ;


literalBlock
    : literalBlockDelimiter
      (OTHER
      |ALOWER
      |ELOWER
      |HLOWER
      |LLOWER
      |MLOWER
      |DLOWER
      |SLOWER
      |VLOWER
      |DIGIT
      |SP
      |EQ
      |SLASH
      |COMMA
      |LSBRACK
      |RSBRACK
      |LABRACK
      |RABRACK
      |CARET
      |MINUS
      |PLUS
      |TIMES
      |DOT
      |COLON
      |SEMICOLON
      |BANG
      |NL
      )*?
      literalBlockDelimiter
    ;

literalBlockDelimiter
    : {isFirstCharInLine()}?
      DOT DOT DOT DOT (SP|TAB)* (CR? NL|EOF)
    ;

     */

    // utils rules
    private Rule blank() {
        return oneOrMore(firstOf(' ', '\t'));
    }

    private Rule newLine() {
        if (isCached("newLine")) return cached("newLine");
        return cached("newLine", sequence(optional('\r'), ch('\n')));
    }

    private Rule isFirstCharInLine() {// TODO store rule in cache
        return () -> ctx -> ctx.getPositionInLine() == 0;
    }

    private Rule isNextCharAtBeginningOfLine() {
        return test(sequence(any(), isFirstCharInLine()));
    }

    private Rule isCurrentCharNotEOI() {
        return testNot(ch(Chars.EOI));
    }

    private Rule isNotStartOfComment() {
        return testNot(sequence(test(sequence(any(), isFirstCharInLine())), ch('/'), ch('/')));//TODO replace with ntimes
    }

}
