grammar Asciidoc;

@parser::header {
  import java.util.Arrays;
}

@parser::members {

    private boolean isFirstCharInLine() {
        //System.out.println(_input.LT(1) + " => " + (_input.LT(1).getCharPositionInLine() == 0));
        return _input.LT(1).getCharPositionInLine() == 0;
    }

    private boolean isNextCharEOF() {
        return _input.LA(2) == EOF;
    }

    private boolean isCurrentCharEOF() {
        return _input.LA(1) == EOF;
    }

    // ---------------------------------------------------------------
    // 'isCharacterIn' element methods
    // ---------------------------------------------------------------

    private boolean isNewLineInRevisionInfo() {
        boolean nextCharIsNL = (_input.LA(2) == NL);
        boolean nextCharIsEOF = (_input.LA(2) == EOF);
        boolean nextCharIsBeginningOfAComment = (_input.LA(2) == SLASH) && (_input.LA(3) == SLASH);
        boolean nextCharIsBeginningOfAttributeEntry = isStartOfAttributeEntryAtIndex(2);

        return !nextCharIsNL && !nextCharIsEOF &&
            !nextCharIsBeginningOfAComment && ! nextCharIsBeginningOfAttributeEntry;
    }

    private boolean isNewLineInListItemValue() {
        boolean nextCharIsBL = isStartOfBlankLineAtIndex(2, false);
        boolean nextCharIsListItem = isStartOfListItemAtIndex(2);
        boolean nextCharIsListContinuation = isStartOfListContinuationAtIndex(2);
        boolean nextCharIsAttributeList = isStartOfAttributeListAtIndex(2);

        return !nextCharIsBL && !nextCharIsListItem && !nextCharIsListContinuation
                && !nextCharIsAttributeList;
    }

    private boolean isBlankInParagraph() {
        int curChar = _input.LA(1);
        if (curChar == SP || curChar == TAB
                || curChar == NL || curChar == CR) {
            boolean curCharIsBL = isStartOfBlankLineAtIndex(1, true);
            boolean ok = !curCharIsBL;
            if (ok && curChar == NL) {// check that next char is not start of blank line
                boolean nextCharIsBL = isStartOfBlankLineAtIndex(2, false);
                ok = !nextCharIsBL;
            }
            return ok;
        }
        return true;
    }

    private boolean isBlankInTableBlock() {
        return isBlankInParagraph();
    }

    private boolean isPlusInParagraph(boolean fromList) {
        if (fromList) {
            return !isStartOfListContinuation();
        }
        return true;
    }

    // ---------------------------------------------------------------
    // 'isStartOf' element methods
    // ---------------------------------------------------------------

    private boolean isStartOfSection() {
        int i = 1;
        int nextChar = _input.LA(i);

        while (nextChar != EOF && nextChar != NL) {
            if (nextChar == EQ) {
                nextChar = _input.LA(++i);
                if (nextChar == SP) {
                    return true;
                }
                continue;
            }
            break;
        }
        return false;
    }

    private boolean isStartOfComment() {
        return _input.LT(1).getCharPositionInLine() == 0
                    && _input.LA(1) == SLASH && _input.LA(2) == SLASH;
    }

    private boolean isStartOfRevisionInfo() {
        boolean currentCharIsNL = (_input.LA(1) == NL);

        return !currentCharIsNL && !isStartOfSection();
    }

    private boolean isStartOfParagraph() {
        return !isStartOfSection();
    }

    private boolean isStartOfBlockTitle() {
        boolean ok = isFirstCharInLine() && !isStartOfLiteralBlock();
        return ok;
    }

    private boolean isStartOfLiteralBlock() {
        if (!isFirstCharInLine()) return false;

        int i = 1;
        int nextChar = _input.LA(i);
        while (nextChar == DOT) {
            nextChar = _input.LA(++i);
        }
        if (i < 4) return false;

        // check trailings blanks
        while (nextChar == SP || nextChar == TAB || nextChar == CR || nextChar == NL || nextChar == EOF) {
            if (nextChar == NL || nextChar == EOF) {
                return true;
            }
            nextChar = _input.LA(++i);
        }

        return false;
    }

    private boolean isStartOfListContinuation() {
        boolean ok = isFirstCharInLine() && isStartOfListContinuationAtIndex(1);
        return ok;
    }

    private boolean isStartOfTableCellSpecifier() {
        char[] buf = new char[20];
        int i = 1;
        int c = _input.LA(i);

        while (true) {
            if (c == PIPE) {
                char[] used = Arrays.copyOfRange(buf, 0, i - 1);
                String str = new String(used);
                return str.matches("((\\d+\\.\\d+|\\.\\d+|\\d+)(\\*|\\+))?((<|^|>)\\.(<|^|>)|\\.(<|^|>)|(<|^|>))?[aehlmdsv]?");
            }
            else if (c == ALOWER || c == ELOWER || c == HLOWER
                || c == LLOWER || c == MLOWER || c == DLOWER
                || c == SLOWER || c == VLOWER || c == DIGIT
                || c == LABRACK || c == RABRACK || c == CARET
                || c == DOT || c == PLUS || c == TIMES) {

                switch (c) {
                    case ALOWER:
                    case ELOWER:
                    case HLOWER:
                    case LLOWER:
                    case MLOWER:
                    case DLOWER:
                    case SLOWER:
                    case VLOWER:
                        buf[i - 1] = 'a';
                        break;
                    case DIGIT:
                        buf[i - 1] = '1';
                        break;
                    case DOT:
                        buf[i - 1] = '.';
                        break;
                    case TIMES:
                        buf[i - 1] = '*';
                        break;
                    case PLUS:
                        buf[i - 1] = '+';
                        break;
                    case LABRACK:
                    case RABRACK:
                    case CARET:
                        buf[i - 1] = '<';
                        break;
                    default:
                        buf[i - 1] = 'X';

                }
                c = _input.LA(++i);
                continue;
            }

            return false;
        }
    }

    // ---------------------------------------------------------------
    // 'isStartOfAtIndex' element methods
    // ---------------------------------------------------------------

    private boolean isStartOfBlankLineAtIndex(int index, boolean checkFirstCharInLine) {
        if (checkFirstCharInLine && !isFirstCharInLine()) return false;
        int i = index;
        int nextChar = _input.LA(i);
        while (nextChar != EOF && nextChar != NL) {
            if (nextChar != SP && nextChar != TAB) {
                return false;
            }
            nextChar = _input.LA(++i);
        }

        return true;
    }


    private boolean isStartOfListItemAtIndex(int index) {
        int i = index;
        int nextChar = _input.LA(i);
        if (nextChar == TIMES) {
            while ((nextChar = _input.LA(++i)) == TIMES) {}
            if (nextChar == SP) return true;
        }
        if (nextChar == DOT) {
            while ((nextChar = _input.LA(++i)) == DOT) {}
            if (nextChar == SP) return true;
        }
        return false;
    }

    private boolean isStartOfListContinuationAtIndex(int index) {
        int i = index;
        if (_input.LA(i) == PLUS && isStartOfBlankLineAtIndex(i + 1, false)) {
            return true;
        }
        return false;
    }

    private boolean isStartOfAttributeEntryAtIndex(int index) {
        int i = index;
        int nextChar = _input.LA(i);

        // check start with ':'
        if (nextChar != COLON) return false;
        nextChar = _input.LA(++i);

        // check attribute name starts with 'other' char
        if (nextChar == BANG) {
            nextChar = _input.LA(++i);
            if (nextChar != OTHER) return false;
        }
        if (nextChar != OTHER) return false;
        nextChar = _input.LA(++i);

        while (nextChar != EOF && nextChar != NL) {

            if (nextChar == COLON) {
                nextChar = _input.LA(++i);
                if (nextChar == SP | nextChar == TAB) {
                    return true;
                }
            }

            nextChar = _input.LA(++i);
        }

        return false;
    }

    private boolean isStartOfAttributeListAtIndex(int index) {
        int i = index;
        int nextChar = _input.LA(i);

        // check start with '['
        if (nextChar != LSBRACK) return false;
        nextChar = _input.LA(++i);

        // check attribute name starts with 'other' char
        if (nextChar != OTHER && nextChar != ALOWER && nextChar != ELOWER
            && nextChar != HLOWER && nextChar != LLOWER && nextChar != MLOWER
            && nextChar != DLOWER && nextChar != SLOWER && nextChar != VLOWER) return false;
        nextChar = _input.LA(++i);

        while (nextChar != EOF && nextChar != NL) {

            if (nextChar == RSBRACK) {
                nextChar = _input.LA(++i);
                while (nextChar == SP || nextChar == TAB || nextChar == EOF || nextChar == NL || nextChar == CR) {
                    if (nextChar == EOF || nextChar == NL) {
                        return true;
                    }
                    nextChar = _input.LA(++i);
                }
                return false;
            }

            nextChar = _input.LA(++i);
        }

        return false;
    }


}

// Parser

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

nl
    : CR? NL
    ;

bl [boolean withEOF]
    : {isFirstCharInLine()}? (SP|TAB)* (CR? NL|{$withEOF}? EOF)
    ;

spaces
    : (SP|TAB)+
    ;

title
    : ~(SP|TAB|NL|EOF) ~(NL|EOF)*
    ;

horizontalRule
    : QUOTE QUOTE QUOTE (SP|TAB)* (CR? NL|EOF)
    ;

header
    : documentTitle
      (multiComment|singleComment)*
      (authors
        (multiComment|singleComment)*
        (attributeEntry|revisionInfo)?
      )?
      attributeEntry*
    ;

documentTitle
    : EQ SP title? (SP|TAB)* (CR? NL|EOF)
    ;

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

preamble
    :       //(attributeList   // TODO
            //|anchor
            //|blockTitle
            //|blockMacro
            //)

      block[false]
      ({!isCurrentCharEOF()}? bl[false]|nl|block[false])*
    ;

section
    : sectionTitle ({!isCurrentCharEOF()}? bl[false]
                    |nl
                    |attributeEntry
                    |attributeList
                    |block[false])*
    ;

sectionTitle :
    EQ+ (SP|TAB)+ title (SP|TAB)* (CR? NL|EOF)
    ;

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

blockTitle
    : {isStartOfBlockTitle()}? DOT title (CR? NL|EOF)
    ;

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

multiCommentDelimiter
    : {isFirstCharInLine()}?
      SLASH SLASH SLASH SLASH (SP|TAB)* (CR? NL|EOF)
    ;

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

sourceBlockDelimiter
    : {isFirstCharInLine()}?
      MINUS MINUS MINUS MINUS (SP|TAB)* (CR? NL|EOF)
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

list
    : listItem
      (({!isCurrentCharEOF()}? bl[false]|attributeList)* listItem)*
    ;

listItem
    : (TIMES+|DOT+) SP listItemValue (CR? NL listContinuation*|EOF)
    ;

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

table
    : tableDelimiter (tableRow|bl[false])* tableDelimiter
    //: tableDelimiter (tableRow|{!isCurrentCharEOF()}? bl[false])* tableDelimiter
    ;// TODO retirer isCurrentCharEOF()

tableRow
    : tableCell+
    ;

tableCell
    : tableCellSpecifiers? PIPE tableBlock (bl[false]+ tableBlock)*?
    //: tableCellSpecifiers? PIPE (tableBlock|(bl[false]|tableBlock)+ tableBlock)?
    //: tableCellSpecifiers? PIPE (tableBlock|tableBlock bl[false] tableBlock)?
    //: tableCellSpecifiers? PIPE tableBlock (bl[false] tableBlock)?
    //: tableCellSpecifiers? PIPE tableCellContent
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
      //|{isPlusInParagraph(false)}? PLUS
      |{!isStartOfTableCellSpecifier()}? PLUS
      |{!isStartOfTableCellSpecifier()}? DOT
      |COLON
      |SEMICOLON
      |BANG
      |{!isStartOfTableCellSpecifier()}? TIMES
      |{isBlankInTableBlock()}? NL
      //)+?
      )+
      nl?
      //spaces? nl?
    ;

tableDelimiter
    : {isFirstCharInLine()}?
      PIPE EQ EQ EQ (SP|TAB)* (CR? NL|EOF)
    ;

// Lexer

ALOWER      : 'a'  ;
ELOWER      : 'e'  ;
HLOWER      : 'h'  ;
LLOWER      : 'l'  ;
MLOWER      : 'm'  ;
DLOWER      : 'd'  ;
SLOWER      : 's'  ;
VLOWER      : 'v'  ;
DIGIT       : [0-9];
EQ          : '='  ;
SP          : ' '  ;
TAB         : '\t' ;
CR          : '\r' ;
NL          : '\n' ;
SLASH       : '/'  ;
LSBRACK     : '['  ;
RSBRACK     : ']'  ;
LABRACK     : '<'  ;
RABRACK     : '>'  ;
CARET       : '^'  ;
COMMA       : ','  ;
MINUS       : '-'  ;
PLUS        : '+'  ;
TIMES       : '*'  ;
DOT         : '.'  ;
COLON       : ':'  ;
SEMICOLON   : ';'  ;
BANG        : '!'  ;
QUOTE       : '\'' ;
PIPE        : '|'  ;
POUND       : '#'  ;
PERCENT     : '%'  ;
OTHER       :  .   ;

/* other chars to define


LPAREN          : '(';
RPAREN          : ')';
LBRACE          : '{';
RBRACE          : '}';

SEMI            : ';';
TILDE           : '~';
QUESTION        : '?';


*/
