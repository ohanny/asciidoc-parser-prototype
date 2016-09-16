package fr.icodem.asciidoc.parser.peg.example.asciidoc;

import fr.icodem.asciidoc.parser.peg.BaseRules;
import fr.icodem.asciidoc.parser.peg.Chars;
import fr.icodem.asciidoc.parser.peg.rules.Rule;
import fr.icodem.asciidoc.parser.peg.rules.RulesFactory;

public class BlockRules extends BaseRules {

    private CommonRules commonRules = new CommonRules();

    @Override
    public void useFactory(RulesFactory factory) {
        super.useFactory(factory);
        commonRules.useFactory(factory);
    }

    // imported rules from common
    private Rule attributeList() {
        return commonRules.attributeList(false);
    }

    private Rule macro() {
        return commonRules.macro(false);
    }

    private Rule blank() {
        return commonRules.blank();
    }

    private Rule newLine() {
        return commonRules.newLine();
    }

    private Rule digits() {
        return commonRules.digits();
    }

    // block rules
    public Rule document() {
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
    private Rule blockMacro() {// TODO factorize with inline
        return node("blockMacro", sequence(
                macroName(),
                string("::"), // TODO should accept single : ?
                optional(macroTarget()),
                attributeList()
        ));
    }

    private Rule macroName() {// TODO factorize with inline
        return node("macroName", oneOrMore(firstOf(
                charRange('A', 'Z'),
                charRange('a', 'z'),
                charRange('0', '9')
        )));
    }

    private Rule macroTarget() {// TODO factorize with inline
        return node("macroTarget", oneOrMore(firstOf(
                charRange('A', 'Z'),
                charRange('a', 'z'),
                charRange('0', '9'),
                ch('.')
        )));
    }
    */

//    public static void main(String[] args) {
////        for (char c = 'A'; c < 'z'; c++) {
////            System.out.println(c + " => " + (int)c);
////        }
//
//
//        AsciidocPegParser parser = new AsciidocPegParser();
//        ToStringTreeBuilder treeBuilder = new ToStringTreeBuilder();
//
//        AsciidocParsingResult result = new AsciidocParsingResult();
//        result.matched = new ParseRunner(parser.tableCellSpecifiers()).parse("<", treeBuilder, new ToStringAnalysisBuilder()).matched;
//        result.tree = treeBuilder.getStringTree();
//
//    }

/*
    private Rule positionalAttribute() {
        return node("positionalAttribute", attributeValue());
    }

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

    private Rule namedAttribute() {// TODO factorize
        return node("namedAttribute", sequence(
                attributeName(),
                ch('='),
                attributeValue()
        ));
    }*/

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

    private Rule anchor() {
        return node("anchor", sequence(
                test(sequence(firstOf(any(), eoi()), isFirstCharInLine())), // TODO replace
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

    private Rule paragraph() {
        return node("paragraph", sequence(
                testNot(sectionTitle()),
                optional(admonition()),
                oneOrMore(firstOf(
                    noneOf("= \t/+\r\n"),
                    blank(),
                    sequence(isBlankInParagraph(), newLine()),
                    sequence(isNotStartOfComment(), ch('/')),
                    sequence(test(() -> ctx -> ctx.getBooleanAttribute("fromList", false)), testNot(listContinuation()), ch('+')), // TODO optimize
                    ch('=')
                )),
                optional(eoi())
        ));
    }

    private Rule admonition() {
        return node("admonition",
                    firstOf(
                        string("NOTE: "),
                        string("TIP: "),
                        string("IMPORTANT: "),
                        string("CAUTION: "),
                        string("WARNING: ")
                    )
                );
    }

    private Rule isBlankInParagraph() {
        return testNot(sequence(firstOf(any(), eoi()), bl(true)));
    }

    private Rule content() {
        return node("content", oneOrMore(firstOf(
                sequence(isCurrentCharNotEOI(), bl(false)),
                horizontalRule(),
                attributeEntry(),
                attributeList(),
                anchor(),
                blockTitle(),
                //blockMacro(),
                macro(),
                section(),
                block(false),
                nl()
        )));
    }

    private Rule documentTitle() {
        return node("documentTitle", true,
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

    private Rule sectionTitle() {
        return node("sectionTitle", sequence(
                action(oneOrMore('='), ctx -> ctx.exportAttributesToParentNode("eqs")), oneOrMore(blank()), title(),
                zeroOrMore(blank()), firstOf(newLine(), eoi())
        ));
    }

    private Rule bl(boolean withEOI) {
        Rule setWithEOI = () -> ctx -> {
            ctx.setAttribute("withEOI", withEOI);
            return true;
        };

        return wrap(setWithEOI, bl());
    }
    private Rule bl() {
        if (isCached("bl")) return cached("bl");
        Rule checkWithEOI = () -> ctx -> ctx.getBooleanAttribute("withEOI", false);
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

    private Rule attributeEntry() {
        return node("attributeEntry", sequence(
                ch(':'), optional(attributeEntryDisabled()), attributeName(), optional(attributeEntryDisabled()), ch(':'),
                //ch(':'), optional('!'), attributeName(), optional('!'), ch(':'),
                zeroOrMore(blank()), optional(attributeValueParts()), zeroOrMore(blank()),
                firstOf(newLine(), eoi())
        ));
    }

    private Rule attributeEntryDisabled() {
        return action(ch('!'), ctx -> {
                ctx.setAttribute("disabled", true);
                ctx.exportAttributesToParentNode(null);
        });
    }

    private Rule attributeValueParts() {
        return node("attributeValueParts", sequence(attributeValuePart(),
                zeroOrMore(sequence(ch('+'), ch('\n'), zeroOrMore(blank()), attributeValuePart()))));
    }

    private Rule attributeValuePart() {
        return node("attributeValuePart", oneOrMore(noneOf("\r\n\t+")));
    }

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
    }

    private Rule authors() {
        return node("authors", sequence(author(), zeroOrMore(sequence(ch(';'), optional(blank()), author())), optional(blank()), firstOf(newLine(), eoi())));
    }

    private Rule author() {
//        return node("author", sequence(authorName(), optional(sequence(ch('<'), authorAddress(), ch('>')))));
        return node("author", sequence(authorName(), zeroOrMore(' '), optional(sequence(ch('<'), authorAddress(), ch('>')))));
    }

    private Rule authorName() {
//        return node("authorName", oneOrMore(noneOf(";:<>{}[]=\r\n\t")));
        return node("authorName", oneOrMore(firstOf(noneOf(" ;:<>{}[]=\r\n\t"), sequence(oneOrMore(' '), test(noneOf(";:<>{}[]=\r\n\t"))))));
    }

    private Rule authorAddress() {
        return node("authorAddress", oneOrMore(noneOf("<>{}[]=\r\n\t")));
    }

    private Rule singleComment() {
        return node("singleComment", sequence(
                test(sequence(firstOf(any(), eoi()), isFirstCharInLine())),
                ch('/'), ch('/'),// TODO ntimes
                zeroOrMore(noneOf("\r\n")),
                firstOf(newLine(), eoi())// TODO replace
        ));
    }

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

    private Rule multiCommentDelimiter() {
        return node("multiCommentDelimiter", sequence(
                test(sequence(firstOf(any(), eoi()), isFirstCharInLine())), // TODO add nextCharIsBeginOfLine
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

    private Rule listItem() {
        return node("listItem", sequence(
                firstOf(action(oneOrMore('*'), ctx -> ctx.exportAttributesToParentNode("times")),
                        action(oneOrMore('.'), ctx -> ctx.exportAttributesToParentNode("dots"))),
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

    private Rule listContinuation() {
        Rule setFromList = () -> ctx -> {
            ctx.setAttribute("fromList", true);// TODO create standard rule ?
            return true;
        };

        return node("listContinuation", sequence(
                ch('+'), optional(blank()), newLine(), wrap(setFromList, proxy("block"))
        ));
    }

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
                    test(sequence(firstOf(any(), eoi()), isFirstCharInLine())), // TODO replace
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
                    test(sequence(firstOf(any(), eoi()), isFirstCharInLine())), // TODO replace
                    ch('.'), ch('.'), ch('.'), ch('.'), // TODO ntimes
                    optional(blank()), // TODO replace with blanks
                    firstOf(newLine(), eoi())
                ));
    }

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

    // utils rules
//    private Rule blank() {
//        return oneOrMore(firstOf(' ', '\t'));
//    }
//
//    private Rule newLine() {
//        if (isCached("newLine")) return cached("newLine");
//        return cached("newLine", sequence(optional('\r'), ch('\n')));
//    }

//    private Rule digits() {
//        return oneOrMore(charRange('0', '9'));
//    }

//    private Rule digit() {
//        return charRange('0', '9');
//    }

    private Rule isFirstCharInLine() {// TODO store rule in cache
        return () -> ctx -> ctx.getPositionInLine() == 0;
    }

    private Rule isNextCharAtBeginningOfLine() {
        return test(sequence(firstOf(any(), eoi()), isFirstCharInLine()));
    }

    private Rule isCurrentCharNotEOI() {
        return testNot(ch(Chars.EOI));
    }

    private Rule isNotStartOfComment() {
        return testNot(sequence(test(sequence(firstOf(any(), eoi()), isFirstCharInLine())), ch('/'), ch('/')));//TODO replace with ntimes
    }

}
