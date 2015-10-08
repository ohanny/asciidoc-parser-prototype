grammar Asciidoc;

@parser::members {

    private boolean isFirstCharInLine() {
        //System.out.println(_input.LT(1) + " => " + (_input.LT(1).getCharPositionInLine() == 0));
        return _input.LT(1).getCharPositionInLine() == 0;
    }

    private boolean isNextCharEOF() {
        return _input.LA(2) == EOF;
    }

    // ---------------------------------------------------------------
    // 'isCharacterIn' element methods
    // ---------------------------------------------------------------

    private boolean isNewLineInRevisionInfo() {
        boolean nextCharIsNL = (_input.LA(2) == NL);
        boolean nextCharIsEOF = (_input.LA(2) == EOF);
        boolean nextCharIsBeginningOfAComment = (_input.LA(2) == SLASH) && (_input.LA(3) == SLASH);

        return !nextCharIsNL && !nextCharIsEOF && !nextCharIsBeginningOfAComment;
    }

    private boolean isNewLineInListItemValue() {
        boolean nextCharIsBL = isStartOfBlankLineAtIndex(2, false);
        boolean nextCharIsListItem = isStartOfListItemAtIndex(2);
        boolean nextCharIsListContinuation = isStartOfListContinuationAtIndex(2);

        return !nextCharIsBL && !nextCharIsListItem && !nextCharIsListContinuation;
    }

    private boolean isBlankInParagraph() {
        int curChar = _input.LA(1);
        if (curChar == SP || curChar == TAB
                || curChar == NL || curChar == CR) {
            boolean curCharIsBL = isStartOfBlankLineAtIndex(1, true);
            boolean ok = !curCharIsBL;
            return ok;
        }
        return true;
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
        return false;
    }

    private boolean isStartOfListContinuationAtIndex(int index) {
        int i = index;
        if (_input.LA(i) == PLUS && isStartOfBlankLineAtIndex(i + 1, false)) {
            return true;
        }
        return false;
    }

}

// Parser

document
    : (bl
      |multiComment
      |singleComment
      )*
      (header (bl|nl|multiComment|singleComment)* preamble?)?
      (bl
      |attributeEntry
      |attributeList   // TODO
      |anchor
      |blockTitle
      |section
      |block[false]
      )*
    ;

nl
    : CR? NL
    ;

bl
    : {isFirstCharInLine()}? (SP|TAB)* {!isNextCharEOF()}?(CR? NL) // TODO
    ;

title
    : ~(SP|TAB|NL|EOF) ~(NL|EOF)*
    ;

header
    : documentTitle
      (multiComment|singleComment)*
      authors? //TODO
      (multiComment|singleComment)*
      revisionInfo?
      attributeEntry*
    ;

documentTitle
    : EQ SP title? (SP|TAB)* (CR? NL|EOF)
    ;

authors
    : authorName (LABRACK authorAddress RABRACK)?
      (SEMICOLON authorName (LABRACK authorAddress RABRACK)?)*
      (SP|TAB)* (CR? NL|EOF)
    ;

authorName
    : (OTHER
      |SP
      |MINUS
      |DOT
      )+
    ;

authorAddress
    : (OTHER
      |MINUS
      |SLASH
      |DOT
      )+
    ;

revisionInfo
    : {isStartOfRevisionInfo()}?
      (OTHER
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
      )+
    ;

attributeName
    : OTHER+
    ;

attributeEntry
    : COLON BANG? attributeName BANG? COLON SP* attributeValue? (SP|TAB)* (CR? NL|EOF)
    ;

attributeValue
    : attributeValuePart (PLUS NL SP* attributeValuePart)*
    ;

attributeValuePart
    : OTHER+
    ;

attributeList
    : LSBRACK
      ((positionalAttribute|namedAttribute) (SP|TAB)*
            (COMMA (positionalAttribute|namedAttribute) (SP|TAB)*)*
      |)
      RSBRACK (SP|TAB)* (CR? NL|EOF)
    ;

positionalAttribute
    : attributeName
    ;

namedAttribute
    : attributeName EQ attributeValuePart?
    ;

preamble
    :       //(attributeList   // TODO
            //|anchor
            //|blockTitle
            //)

      block[false]
      (bl|nl|block[false])*
    ;

section
    : sectionTitle (bl|nl|block[false])*
    ;

sectionTitle :
    EQ+ (SP|TAB)* title? (SP|TAB)* (CR? NL|EOF)
    ;

block[boolean fromList]       // argument 'fromList' indicates that block is attached to a list item
    : (multiComment
      |singleComment
      |unorderedList
      |sourceBlock
      |literalBlock
      |paragraph[$fromList]
      )
    ;

blockTitle
    : {isStartOfBlockTitle()}? DOT title (CR? NL)?
    ;

anchor
    : {isFirstCharInLine()}?
      LSBRACK LSBRACK anchorId
      (COMMA anchorLabel)?
      RSBRACK RSBRACK NL?
    ;

anchorId
    : (OTHER)+
    ;

anchorLabel
    : (OTHER
      |SP
      |EQ
      |SLASH
      |COMMA
      )+
    ;

paragraph [boolean fromList] // argument 'fromList' indicates that paragraph is attached to a list item
    : {isStartOfParagraph()}?
      (OTHER
      |{isBlankInParagraph()}? SP
      |{isBlankInParagraph()}? TAB
      |EQ
      |{!isStartOfComment()}? SLASH
      |COMMA
      |LSBRACK
      |RSBRACK
      |LABRACK
      |RABRACK
      |MINUS
      |{isPlusInParagraph($fromList)}? PLUS
      |DOT
      |COLON
      |SEMICOLON
      |BANG
      |{isBlankInParagraph()}? NL
      )+
    ;

singleComment
    : {isFirstCharInLine()}?
      SLASH SLASH
      (OTHER
      |SP
      |EQ
      |SLASH
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
      )*
      (CR? NL|EOF)
    ;

multiComment
    : multiCommentDelimiter
      (OTHER
      |SP
      |EQ
      |SLASH
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
      |SP
      |EQ
      |SLASH
      |COMMA
      |LSBRACK
      |RSBRACK
      |LABRACK
      |RABRACK
      |MINUS
      |PLUS
      |TIMES
      |DOT
      |COLON
      |SEMICOLON
      |BANG
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
      |SP
      |EQ
      |SLASH
      |COMMA
      |LSBRACK
      |RSBRACK
      |LABRACK
      |RABRACK
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

unorderedList
    : listItem (listItem|bl listItem)*
    ;

listItem
    : TIMES+ SP listItemValue (CR? NL listContinuation*|EOF)
    ;

listItemValue
    : (OTHER
      |SP
      |EQ
      |SLASH
      |COMMA
      |LSBRACK
      |RSBRACK
      |LABRACK
      |RABRACK
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

// Lexer

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
COMMA       : ','  ;
MINUS       : '-'  ;
PLUS        : '+'  ;
TIMES       : '*'  ;
DOT         : '.'  ;
COLON       : ':'  ;
SEMICOLON   : ';'  ;
BANG        : '!'  ;
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
