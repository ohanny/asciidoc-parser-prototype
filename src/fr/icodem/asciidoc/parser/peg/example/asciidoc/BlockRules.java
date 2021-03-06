package fr.icodem.asciidoc.parser.peg.example.asciidoc;

import fr.icodem.asciidoc.parser.peg.BaseRules;
import fr.icodem.asciidoc.parser.peg.Chars;
import fr.icodem.asciidoc.parser.peg.action.ActionContext;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.AttributeEntries;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.SourceResolver;
import fr.icodem.asciidoc.parser.peg.rules.Rule;
import fr.icodem.asciidoc.parser.peg.rules.RulesFactory;

public class BlockRules extends BaseRules {

    private CommonRules commonRules;
    private AttributeEntries attributeEntries;

    public BlockRules(AttributeEntries attributeEntries) {
        this.attributeEntries = attributeEntries;
        this.commonRules = new CommonRules(attributeEntries);
    }

    @Override
    public void withFactory(RulesFactory factory) {
        super.withFactory(factory);
        commonRules.withFactory(factory);
    }

    public void withSourceResolver(SourceResolver resolver) {
        commonRules.withSourceResolver(resolver);
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
                               ))//,
                               //optional(preamble())
                           )
                        ), optional(content()), optional(bl(true))));
    }

    private Rule header() {
        return node("header", sequence(
                documentSection(),
                zeroOrMore(firstOf(multiComment(), singleComment())),
                optional(sequence(
                        authors(),
                        zeroOrMore(firstOf(multiComment(), singleComment())),
                        optional(firstOf(attributeEntry(), revisionInfo()))
                )),
                zeroOrMore(attributeEntry())
        ));
    }

    private Rule documentSection() {
        return node("documentSection", true,
                sequence(ch('='), ch(' '), optional(documentTitle()), optional(blank()), firstOf(newLine(), eoi())));
    }

    private Rule documentTitle() {
        return node("documentTitle", title());
    }

    private Rule title() {
        if (isCached("title")) return cached("title");

        return cached("title", sequence(noneOf(" \n\t\r"), zeroOrMore(noneOf('\n'))));
    }


    private Rule preamble() {
        return node("preamble", sequence(
                firstOf(horizontalRule(), attributeEntry(), attributeList(), anchor(), blockTitle(), macro(), block(false)),
                zeroOrMore(firstOf(
                        sequence(isCurrentCharNotEOI(), bl(false)),
                        nl(),
                        horizontalRule(),
                        attributeEntry(), attributeList(), anchor(), blockTitle(),
                        macro(),
                        block(false)
                ))//, optional(bl(true))
        ));

    }

    private Rule block(boolean fromList) {
        return wrap(setAttribute("fromList", fromList), block());
    }

    private Rule block() {
        return node("block", firstOf(
                multiComment(),
                singleComment(),
                list(),
                listingBlock(),
                literalBlock(),
                exampleBlock(),
                table(),
                descriptionList(),
                sidebarBlock(),
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
        return node("paragraph",
                 sequence(
                   testNot(sequence(optional(attributeList()), section())),
                   //testNot(section()),
                   optional(admonition()),
                   oneOrMore(
                     firstOf(
                       noneOf("*= \t/+\r\n"),
                       blank(),
                       sequence(
                         newLine(),
                         isNewLineInParagraph()
                       ),
                       sequence(
                         isNotStartOfComment(),
                         ch('/')
                       ),
                       sequence(// TODO optimize
                         test(() -> ctx -> ctx.getBooleanAttribute("fromList", false)),
                         testNot(listContinuation()),
                         ch('+')
                       ),
                       sequence(testNot(exampleBlockDelimiter()), ch('=')),
                       sequence(testNot(sidebarBlockDelimiter()), ch('*')),
                       sequence(testNot(() -> ctx -> ctx.getBooleanAttribute("fromList", false)), ch('+'))
                     )
                   ),
                   optional(eoi())
                 )
               )
        ;
    }

    private Rule admonition() {
        return node("admonition",
                 firstOf(
                   sequence(
                     string("NOTE: "),
                     setAttributeOnParent("paragraph", "admonition", "NOTE")
                   ),
                   sequence(
                     string("TIP: "),
                     setAttributeOnParent("paragraph", "admonition", "TIP")
                   ),
                   sequence(
                     string("IMPORTANT: "),
                     setAttributeOnParent("paragraph", "admonition", "IMPORTANT")
                   ),
                   sequence(
                     string("CAUTION: "),
                     setAttributeOnParent("paragraph","admonition", "CAUTION")
                   ),
                   sequence(
                     string("WARNING: "),
                     setAttributeOnParent("paragraph","admonition", "WARNING")
                   )
                 )
               );
    }

    private Rule isNewLineInParagraph() {
        return testNot(
                 firstOf(
                   eoi(),
                   bl(true)
                 )
               )
        ;
    }

    private Rule content() {
        return node("content", atLeastSequence(1, preamble(), oneOrMore(firstOf(
                sequence(isCurrentCharNotEOI(), bl(false)),
                horizontalRule(),
                attributeEntry(),
                attributeList(),
                anchor(),
                blockTitle(),
                macro(),
                section(),
                block(false),
                nl()
        ))));
    }

    private Rule section() {
        return node("section",
                 sequence(
                   action(oneOrMore('='), this::exportLevelOnSection),
                   oneOrMore(blank()),
                   sectionTitle(),
                   zeroOrMore(blank()),
                   firstOf(
                     newLine(),
                     eoi()
                   )
                 )
               )
        ;
    }

    private Rule sectionTitle() {
        return node("sectionTitle", title());
    }

    private void exportLevelOnSection(ActionContext context) {
        int level = context.getIntAttribute("count", 0);
        context.setAttributeOnParent("section", "level", level);

        context.exportAttributesToParentNode("eqs");
    }


    private Rule bl(boolean withEOI) {
        return wrap(setAttribute("withEOI", withEOI), bl());
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
                blockTitleValue(),
                firstOf(newLine(), eoi()) // TODO replace
        ));
    }

    private Rule blockTitleValue() {
        return node("blockTitleValue", title());
    }

    private Rule horizontalRule() {
        return node("horizontalRule", sequence(string("'''"), optional(blank()), firstOf(newLine(), eoi())));
    }

    private Rule attributeEntryName() {
        return node("attributeEntryName", oneOrMore(firstOf(
                charRange('A', 'Z'),
                charRange('a', 'z'),
                charRange('0', '9'),
                ch(' '), ch('-')
        )));
    }

    private Rule attributeEntry() {
        return node("attributeEntry", sequence(
                ch(':'), optional(attributeEntryDisabled()), attributeEntryName(), optional(attributeEntryDisabled()), ch(':'),
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
                testNot(firstOf(newLine(), section())),
//                testNot(firstOf(newLine(), sectionTitle())),
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
        return node("authors", true, sequence(author(), zeroOrMore(sequence(ch(';'), optional(blank()), author())), optional(blank()), firstOf(newLine(), eoi())));
    }

    private Rule author() {
//        return node("author", sequence(authorName(), optional(sequence(ch('<'), authorAddress(), ch('>')))));
        return node("author", true,
                   sequence(
                       authorName(),
                       zeroOrMore(' '),
                       optional(
                           sequence(
                               ch('<'),
                               authorAddress(),
                               optional(
                                   sequence(
                                       ch('['),
                                       authorAddressLabel(),
                                       ch(']')
                                   )
                               ),
                               ch('>')
                           )
                       )
                   )
               );
    }

    private Rule authorName() {
//        return node("authorName", oneOrMore(noneOf(";:<>{}[]=\r\n\t")));
        return node("authorName", oneOrMore(firstOf(noneOf(" ;:<>{}[]=\r\n\t"), sequence(oneOrMore(' '), test(noneOf(";:<>{}[]=\r\n\t"))))));
    }

    private Rule authorAddress() {
        return node("authorAddress", oneOrMore(noneOf("<>{}[]=\r\n\t")));
    }

    private Rule authorAddressLabel() {
        return node("authorAddressLabel", oneOrMore(noneOf("<>{}[]=\r\n\t")));
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
        return
          node("list",
            sequence(
              listItem(),
              zeroOrMore(
                sequence(
                  zeroOrMore(
                    firstOf(
                      sequence(
                        isCurrentCharNotEOI(),
                        bl(false)
                      ),
                      attributeList() // TODO add attribute list
                    )
                  ),
                  listItem()
                )
              )
            )
          );
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
        return node("listContinuation", sequence(
                ch('+'), optional(blank()), newLine(), wrap(setAttribute("fromList", true), sequence(optional(attributeList()), proxy("block")))
        ));
    }

    private Rule descriptionList() {
        return node("descriptionList",
                 sequence(
                   descriptionListItem(),
                   zeroOrMore(
                     sequence(
                       zeroOrMore(
                         firstOf(
                           sequence(
                             isCurrentCharNotEOI(),
                             bl(false)
                           ),
                           attributeList()
                         )
                       ),
                       descriptionListItem()
                     )
                   )
                 )
               )
        ;
    }

    private Rule descriptionListItem() {
        return node("descriptionListItem",
                 sequence(
                   optional(blank()),
                   descriptionListItemTitle(),
                   atLeast(':', 2),
                   firstOf(
                     sequence(
                       blank(),
                       optional(nl())
                     ),
                     nl()
                   ),
                   zeroOrMore(bl()),
                   descriptionListItemContent(),
                   optional(nl())
                 )
               )
        ;
    }

    private Rule descriptionListItemTitle() {
        return node("descriptionListItemTitle",
                 zeroOrMore(
                   sequence(
                     testNot(
                       sequence(
                         atLeast(':', 2),
                         firstOf(
                           sequence(
                             blank(),
                             optional(nl())
                           ),
                           nl()
                         )
                       )
                     ),
                     noneOf("\r\n")
                   )
                 )
               )
        ;
    }

    // Could be greatly simplified if content was always considered as block
    // => compatible with [horizontal] ?
    private Rule descriptionListItemContent() {
        return node("descriptionListItemContent",
                 firstOf(
                   sequence(
                     isNextCharAtBeginningOfLine(),
                     listContinuation()
                   ),
                   zeroOrMore(
                     sequence(
                       testNot(
                         sequence(
                           isNextCharAtBeginningOfLine(),
                           optional(blank()),
                           descriptionListItemTitle(),
                           atLeast(':', 2),
                           firstOf(
                             sequence(
                               blank(),
                               optional(nl())
                             ),
                             nl()
                           )
                         )
                       ),
                       firstOf(
                         noneOf("\r\n"),
                         sequence(
                           anyOf("\r\n"),
                           testNot(
                             firstOf(
                               bl(),
                               bl(true),
                               sequence(
                                 descriptionListItemTitle(),
                                 atLeast(':', 2),
                                 firstOf(
                                   sequence(
                                     blank(),
                                     optional(nl())
                                   ),
                                   nl()
                                 )
                               )
                             )
                           )
                         )
                       )
                     )
                   )
                 )
               )
        ;
    }

    private Rule listingBlock() {
        return node("listingBlock",
                 sequence(
                   listingBlockDelimiter(),
                   zeroOrMore(
                     firstOf(
                       noneOf("-"),
                       sequence(
                         testNot(listingBlockDelimiter()),
                         ch('-')
                       )
                     )
                   ),
                   listingBlockDelimiter(),
                   optional(callouts())
                 )
               )
        ;

    }

    private Rule listingBlockDelimiter() {
        return node("listingBlockDelimiter",
                 sequence(
                   test(sequence(
                     firstOf(
                       any(),
                       eoi()
                     ),
                     isFirstCharInLine())
                   ), // TODO replace
                   atLeast('-', 4),
                   optional(blank()), // TODO replace with blanks
                   firstOf(
                     newLine(),
                     eoi()
                   )
                 )
               )
        ;
    }

    private Rule callouts() {
        return node("callouts",
                 oneOrMore(callout())
               )
        ;
    }

    private Rule callout() {
        return node("callout",
                 sequence(
                   zeroOrMore(blank()),
                   ch('<'),
                   calloutNumber(),
                   ch('>'),
                   zeroOrMore(blank()),
                   calloutText(),
                   newLine()
                 )
               )
        ;
    }

    private Rule calloutNumber() {
        return node("calloutNumber",
                 digits()
               )
        ;
    }

    private Rule calloutText() {
        return node("calloutText",
                 zeroOrMore(noneOf("\r\n"))
               )
        ;
    }

    private Rule literalBlock() {
        return node("literalBlock",
                 sequence(
                   literalBlockDelimiter(),
                   zeroOrMore(
                     firstOf(
                       noneOf("."),
                       sequence(
                         testNot(literalBlockDelimiter()),
                         ch('.')
                       )
                     )
                   ),
                  literalBlockDelimiter()
                )
              )
        ;
    }

    private Rule literalBlockDelimiter() {
        return node("literalBlockDelimiter", sequence(
                    test(sequence(firstOf(any(), eoi()), isFirstCharInLine())), // TODO replace
                    string("...."), // TODO ntimes
                    optional(blank()), // TODO replace with blanks
                    firstOf(newLine(), eoi())
                ));
    }

    private Rule exampleBlock() {
        return
          node("exampleBlock",
            sequence(
              exampleBlockDelimiter(),
              zeroOrMore(
                firstOf(
                  bl(),
                  attributeList(),
                  list(),
                  listingBlock(),
                  literalBlock(),
                  //exampleBlock(),
                  table(),
                  descriptionList(),
                  sequence(paragraph(), optional(nl()))
                )
              ),
              exampleBlockDelimiter()
            )
          );
    }

    private Rule exampleBlockDelimiter() {
        return node("exampleBlockDelimiter", sequence(
                    test(sequence(firstOf(any(), eoi()), isFirstCharInLine())), // TODO replace
                    string("===="), // TODO ntimes
                    optional(blank()), // TODO replace with blanks
                    firstOf(newLine(), eoi())
                ));
    }

    private Rule sidebarBlock() {
        return
          node("sidebarBlock",
            sequence(
              sidebarBlockDelimiter(),
              zeroOrMore(
                firstOf(
                  bl(),
                  attributeList(),
                  list(),
                  listingBlock(),
                  literalBlock(),
                  exampleBlock(),
                  table(),
                  descriptionList(),
                  sequence(paragraph(), optional(nl()))
                )
              ),
              sidebarBlockDelimiter()
            )
          );
    }

    private Rule sidebarBlockDelimiter() {
        return node("sidebarBlockDelimiter", sequence(
                    test(sequence(firstOf(any(), eoi()), isFirstCharInLine())), // TODO replace
                    string("****"), // TODO ntimes
                    optional(blank()), // TODO replace with blanks
                    firstOf(newLine(), eoi())
                ));
    }

    private Rule table() {
        return node("table", sequence(
                storeLineNumber(),
                tableDelimiter(),
                zeroOrMore(firstOf(
                    tableCell(),
                    bl(false)
                )),
                tableDelimiter()
        ));
    }

    private Rule tableDelimiter() {
        return node("tableDelimiter", sequence(
                ch('|'),
                isFirstCharInLine(),
                atLeast('=', 3),
                optional(blank()),// TODO replace
                firstOf(newLine(), eoi())// TODO replace
        ));
    }

//    private Rule tableRow() {
//        return node("tableRow", oneOrMore(tableCell()));
//    }

    private Rule tableCell() {
        return node("tableCell", sequence(
                storeLineNumber(),
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

    private Rule storeLineNumber() {
        return () -> ctx -> {
            ctx.findParentContextNode().setAttribute("lineNumber", ctx.getLineNumber());
            return true;
        };
    }

}
