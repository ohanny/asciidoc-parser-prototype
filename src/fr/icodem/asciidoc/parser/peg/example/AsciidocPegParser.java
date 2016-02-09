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
        //result.matched = new ParseRunner(document()).parse(text, treeBuilder).matched;
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
                                   nl(),
                                   multiComment(),
                                   singleComment()
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
                        zeroOrMore(firstOf(multiComment(), singleComment())),
                        optional(firstOf(attributeEntry(), revisionInfo()))
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


    /*
    blockMacro
    : macroName COLON COLON macroTarget? attributeList
    ;

macroName
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

macroTarget
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
      |DOT)+
    ;

     */

    private Rule blockMacro() {
        return node("blockMacro", sequence(
                macroName(),
                string("::"),
                optional(macroTarget()),
                attributeList()
        ));
    }

    private Rule macroName() {
        return node("macroName", oneOrMore(firstOf(
                charRange('A', 'Z'),
                charRange('a', 'z'),
                charRange('0', '9')
        )));
    }

    private Rule macroTarget() {
        return node("macroTarget", oneOrMore(firstOf(
                charRange('A', 'Z'),
                charRange('a', 'z'),
                charRange('0', '9'),
                ch('.')
        )));
    }

    public static void main(String[] args) {
//        for (char c = 'A'; c < 'z'; c++) {
//            System.out.println(c + " => " + (int)c);
//        }


        AsciidocPegParser parser = new AsciidocPegParser();
        ToStringTreeBuilder treeBuilder = new ToStringTreeBuilder();

        AsciidocParsingResult result = new AsciidocParsingResult();
        result.matched = new ParseRunner(parser.tableCellSpecifiers()).parse("<", treeBuilder, new ToStringAnalysisBuilder()).matched;
        result.tree = treeBuilder.getStringTree();

    }

    private Rule attributeList() {
        return node("attributeList", sequence(
                ch('['),
                firstOf(
                    sequence(
                        firstOf(
                            namedAttribute(),
                            sequence(positionalAttribute(), optional(idAttribute()), zeroOrMore(firstOf(roleAttribute(), optionAttribute()))),
                            sequence(idAttribute(), zeroOrMore(firstOf(roleAttribute(), optionAttribute()))),
                            oneOrMore(firstOf(roleAttribute(), optionAttribute()))

                        ),
                        optional(blank()),// TODO replace,
                        zeroOrMore(sequence(
                            ch(','),
                            optional(blank()),// TODO replace,
                            firstOf(namedAttribute(), positionalAttribute()),
                            optional(blank())// TODO replace
                        ))
                    ),
                    empty() // TODO replace with optional(sequence) ?
                ),
                ch(']'),
                optional(blank()),// TODO replace
                firstOf(newLine(), eoi())// TODO replace
        ));
    }

    private Rule positionalAttribute() {
        return node("positionalAttribute", attributeValue());
    }
    /*
    attributeList
    : LSBRACK
      ((positionalAttribute idAttribute? (roleAttribute|optionAttribute)*
       |idAttribute (roleAttribute|optionAttribute)*
       |(roleAttribute|optionAttribute)+
       |namedAttribute) (SP|TAB)*
            (COMMA (SP|TAB)* (positionalAttribute|namedAttribute) (SP|TAB)*)*
      |)
      RSBRACK (SP|TAB)* (CR? NL|EOF)
    ;

positionalAttribute
    : attributeValue
    ;

idAttribute
    : POUND idName
    ;

idName
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

roleAttribute
    : DOT roleName
    ;

roleName
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

optionAttribute
    : PERCENT optionName
    ;

optionName
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

namedAttribute
    : attributeName EQ attributeValue?
    ;
    attributeValue
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
      |SP)+
    ;


     */

    private Rule idAttribute() {// TODO factorize
        return node("idAttribute", sequence(
                ch('#'), idName()
        ));
    }

    private Rule roleAttribute() {// TODO factorize
        return node("roleAttribute", sequence(
                ch('.'), roleName()
        ));
    }

    private Rule optionAttribute() {// TODO factorize
        return node("optionAttribute", sequence(
                ch('%'), optionName()
        ));
    }

    private Rule idName() {// TODO factorize
        return node("idName", oneOrMore(firstOf(
                charRange('A', 'Z'),
                charRange('a', 'z'),
                charRange('0', '9'),
                ch(' ')
        )));
    }

    private Rule roleName() {// TODO factorize
        return node("roleName", oneOrMore(firstOf(
                charRange('A', 'Z'),
                charRange('a', 'z'),
                charRange('0', '9'),
                ch(' ')
        )));
    }

    private Rule optionName() {// TODO factorize
        return node("optionName", oneOrMore(firstOf(
                charRange('A', 'Z'),
                charRange('a', 'z'),
                charRange('0', '9'),
                ch(' ')
        )));
    }

    private Rule attributeValue() {// TODO factorize ?
        return node("attributeValue", oneOrMore(firstOf(
                charRange('A', 'Z'),
                charRange('a', 'z'),
                charRange('0', '9'),
                ch(' ')
        )));
    }

    private Rule namedAttribute() {
        return node("namedAttribute", sequence(
                attributeName(),
                ch('='),
                attributeValue()
        ));
    }

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
                table(),
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
                    sequence(test(() -> ctx -> ctx.getBooleanAttribute("fromList")), testNot(listContinuation()), ch('+')), // TODO optimize
                    ch('=')
                )),
                optional(eoi())
        ));
    }

    private Rule isBlankInParagraph() {
        return testNot(sequence(any(), bl(true)));
    }

    /*
    {isPlusInParagraph($fromList)
     */

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
                attributeList(),
                anchor(),
                blockTitle(),
                blockMacro(),
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
                        attributeList(),
                        block(false)
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

spaces
    : (SP|TAB)+
    ;

     */

    private Rule spaces() {// TODO replace with blanks ?
        return node("spaces", oneOrMore(firstOf(" \t")));
    }

    private Rule blockTitle() {
        return node("blockTitle", sequence(
                testNot(literalBlockDelimiter()),
                ch('.'),
                test(isFirstCharInLine()),
                title(),
                firstOf(newLine(), eoi()) // TODO replace
        ));
    }

    /*
    blockTitle
    : {isStartOfBlockTitle()}? DOT title (CR? NL|EOF)
    ;

     */

    private Rule horizontalRule() {
        return node("horizontalRule", sequence(string("'''"), optional(blank()), firstOf(newLine(), eoi())));
    }

    private Rule attributeName() {// TODO factorize ?
        return node("attributeName", oneOrMore(firstOf(
                charRange('A', 'Z'),
                charRange('a', 'z'),
                charRange('0', '9'),
                ch(' ')
        )));
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

    private Rule revisionInfo() {
        return node("revisionInfo", sequence(
                testNot(firstOf(newLine(), sectionTitle())),
                oneOrMore(firstOf(
                        noneOf("\r\n/"),
                        sequence(isNotStartOfComment(), ch('/')),
                        sequence(newLine(), testNot(firstOf(
                            newLine(),
                            eoi(),
                            attributeEntry()
                    )), test(isNotStartOfComment())))
                ),
                firstOf(newLine(), eoi()) // TODO replace
        ));

        /*
                return !nextCharIsNL && !nextCharIsEOF &&
            !nextCharIsBeginningOfAComment && ! nextCharIsBeginningOfAttributeEntry;

         */
    }

    /*
    revisionInfo
    : {isStartOfRevisionInfo()}?
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
      |{!isStartOfComment()}? SLASH
      |COMMA
      |LSBRACK
      |RSBRACK
      |LABRACK
      |RABRACK
      |MINUS
      |PLUS
      |DOT
      |COLON
      |SEMICOLON
      |BANG
      |{isNewLineInRevisionInfo()}? NL
      )+ (CR? NL|EOF)
    ;

     */

    private Rule authors() {
        return node("authors", sequence(author(), zeroOrMore(sequence(ch(';'), author())), optional(blank()), firstOf(newLine(), eoi())));
    }

    private Rule author() {
        return node("author", sequence(authorName(), optional(sequence(ch('<'), authorAddress(), ch('>')))));
    }

    private Rule authorName() {
        return node("authorName", oneOrMore(noneOf(";:<>{}[]=\r\n\t")));
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
                        zeroOrMore(firstOf(sequence(isCurrentCharNotEOI(), bl(false)), attributeList())),// TODO add attribute list
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

    /*
        private boolean isNewLineInListItemValue() {
        boolean nextCharIsBL = isStartOfBlankLineAtIndex(2, false);
        boolean nextCharIsListItem = isStartOfListItemAtIndex(2);
        boolean nextCharIsListContinuation = isStartOfListContinuationAtIndex(2);
        boolean nextCharIsAttributeList = isStartOfAttributeListAtIndex(2);

        return !nextCharIsBL && !nextCharIsListItem && !nextCharIsListContinuation
                && !nextCharIsAttributeList;
    }

     */

    private Rule listItemValue() {
        return node("listItemValue", zeroOrMore(firstOf(
                noneOf("\r\n"),
                sequence( // TODO optimize isNewLineInListItemValue()
                    newLine(),
                    testNot(bl(true)),
                    testNot(proxy("listItem")),
                    testNot(listContinuation()),
                    testNot(attributeList())
                )
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
//tableDelimiter (tableRow|bl[false])* tableDelimiter
    private Rule table() {
        return node("table", sequence(
                tableDelimiter(),
                zeroOrMore(firstOf(
                    tableRow(),
                    bl(false)
                )),
                tableDelimiter()
        ));
    }

    private Rule tableDelimiter() {
        return node("tableDelimiter", sequence(
                ch('|'),
                isFirstCharInLine(),
                string("==="),
                optional(blank()),// TODO replace
                firstOf(newLine(), eoi())// TODO replace
        ));
    }

    private Rule tableRow() {
        return node("tableRow", oneOrMore(tableCell()));
    }

    private Rule tableCell() {
        return node("tableCell", sequence(
                optional(tableCellSpecifiers()),
                testNot(tableDelimiter()),
                ch('|'),
                tableBlock(),
                zeroOrMore(sequence(oneOrMore(bl(false)), tableBlock()))
                //zeroOrMore(sequence(oneOrMore(bl(false)), testNot(sequence(optional(tableCellSpecifiers()), ch('|'))),tableBlock())) // TODO make it non greedy
        ));
    }

    private Rule tableCellSpecifiers() {
        return node("tableCellSpecifiers", firstOf(
                sequence(tableCellSpan(), tableCellAlign(), tableCellStyle()),
                sequence(tableCellSpan(), tableCellAlign()),
                sequence(tableCellSpan(), tableCellStyle()),
                sequence(tableCellAlign(), tableCellStyle()),
                tableCellSpan(),
                tableCellAlign(),
                tableCellStyle()
        ));
    }

    private Rule tableCellSpan() {
        return node("tableCellSpan", sequence(
                firstOf(
                    sequence(digits(), ch('.'), digits()),
                    sequence(ch('.'), digits()),
                    digits()
                ),
                firstOf("+*")
        ));
    }

    private Rule tableCellAlign() {
        return node("tableCellAlign", firstOf(
                sequence(firstOf("<^>"), firstOf(string(".<"), string(".^"), string(".>"))),
                firstOf(string(".<"), string(".^"), string(".>")),
                firstOf("<^>")
        ));
    }

    private Rule tableCellStyle() {
        return node("tableCellStyle", firstOf("aehlmdsv"));
    }



    /*
    table
    : tableDelimiter (tableRow|bl[false])* tableDelimiter

tableRow
    : tableCell+
    ;

tableCell
    : tableCellSpecifiers? PIPE tableBlock (bl[false]+ tableBlock)*?
    ;

tableCellSpecifiers
    : tableCellSpan
      |tableCellAlign
      |tableCellStyle
      |tableCellSpan tableCellAlign
      |tableCellSpan tableCellStyle
      |tableCellAlign tableCellStyle
      |tableCellSpan tableCellAlign tableCellStyle
    ;

tableCellSpan
    : (DIGIT+|DOT DIGIT+|DIGIT+ DOT DIGIT+) (PLUS|TIMES)
    ;

tableCellAlign
    : (LABRACK|CARET|RABRACK)
      |(DOT LABRACK|DOT CARET|DOT RABRACK)
      |(LABRACK|CARET|RABRACK) (DOT LABRACK|DOT CARET|DOT RABRACK)
    ;

tableCellStyle
    : (ALOWER
      |ELOWER
      |HLOWER
      |LLOWER
      |MLOWER
      |DLOWER
      |SLOWER
      |VLOWER
      )
    ;
*/

    private Rule tableBlock() {
        return node("tableBlock", sequence(
                optional(spaces()),
                oneOrMore(firstOf(
                    noneOf("|aehlmdsv0123456789<>^.* \t/\r\n"),
                    sequence(testNot(sequence(tableCellSpecifiers(), ch('|'))), firstOf("aehlmdsv0123456789<>^.*")),
                    sequence(firstOf(" \t")),// TODO replace add condition isBlankInTableBlock
                    sequence(ch('/')),// TODO add condition is not start of comment
                    sequence(newLine(), testNot(bl(false)))
                )),
                optional(nl())
        ));
    }

//    private Rule isBlankInTableBlock() {
//
//    }

     /*
tableBlock
    : spaces?
      (OTHER
      |{!isStartOfTableCellSpecifier()}? ALOWER
      |{!isStartOfTableCellSpecifier()}? ELOWER
      |{!isStartOfTableCellSpecifier()}? HLOWER
      |{!isStartOfTableCellSpecifier()}? LLOWER
      |{!isStartOfTableCellSpecifier()}? MLOWER
      |{!isStartOfTableCellSpecifier()}? DLOWER
      |{!isStartOfTableCellSpecifier()}? SLOWER
      |{!isStartOfTableCellSpecifier()}? VLOWER
      |{!isStartOfTableCellSpecifier()}? DIGIT
      |{isBlankInTableBlock()}? SP
      |{isBlankInTableBlock()}? TAB
      |EQ
      |{!isStartOfComment()}? SLASH
      |COMMA
      |LSBRACK
      |RSBRACK
      |{!isStartOfTableCellSpecifier()}? LABRACK
      |{!isStartOfTableCellSpecifier()}? RABRACK
      |{!isStartOfTableCellSpecifier()}? CARET
      |MINUS
      |{!isStartOfTableCellSpecifier()}? PLUS
      |{!isStartOfTableCellSpecifier()}? DOT
      |COLON
      |SEMICOLON
      |BANG
      |{!isStartOfTableCellSpecifier()}? TIMES
      |{isBlankInTableBlock()}? NL
      )+
      nl?
    ;

tableDelimiter
    : {isFirstCharInLine()}?
      PIPE EQ EQ EQ (SP|TAB)* (CR? NL|EOF)
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

    private Rule digits() {
        return oneOrMore(charRange('0', '9'));
    }

    private Rule digit() {
        return charRange('0', '9');
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
