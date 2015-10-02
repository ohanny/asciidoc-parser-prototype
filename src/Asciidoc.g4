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

    private boolean isNewLineInParagraph(boolean fromList) {
        boolean nextCharIsBL = isStartOfBlankLineAtIndex(2);
        boolean nextCharIsBeginningOfAComment = (_input.LA(2) == SLASH) && (_input.LA(3) == SLASH);
        boolean ok = !nextCharIsBL && !nextCharIsBeginningOfAComment;

        if (fromList) {
            boolean nextCharIsListContinuation = isStartOfListContinuationAtIndex(2);
            ok = ok && !nextCharIsListContinuation;
        }

        return ok;
    }

    private boolean isNewLineInListItemValue() {
        boolean nextCharIsBL = isStartOfBlankLineAtIndex(2);
        boolean nextCharIsListItem = isStartOfListItemAtIndex(2);
        boolean nextCharIsListContinuation = isStartOfListContinuationAtIndex(2);

        return !nextCharIsBL && !nextCharIsListItem && !nextCharIsListContinuation;
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

    // ---------------------------------------------------------------
    // 'isStartOfAtIndex' element methods
    // ---------------------------------------------------------------
    private boolean isStartOfBlankLineAtIndex(int index) {
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
        if (_input.LA(i) == PLUS && isStartOfBlankLineAtIndex(i + 1)) {
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
      |section
      |block[false]
      )*
    ;

document1
    : (bl
      |multiComment
      |singleComment
      )*
      (header (bl|nl|multiComment|singleComment)* preamble?)?
      (bl
      |attributeEntry
      |section
      |block[false]
      )*
    ;

nl
    : CR? NL
    ;

bl
    : {isFirstCharInLine()}? (SP|TAB)* {!isNextCharEOF()}?(CR? NL) // TODO
    //: {isFirstCharInLine()}? (SP|TAB)* {_input.LA(2) != EOF}?(CR? NL|EOF) // TODO
    ;

title
    : ~(SP|TAB) ~(NL|EOF)+
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
    : EQ SP title? (NL|EOF)
    ;

authors
    : authorName (LABRACK authorAddress RABRACK)?
      (SEMICOLON authorName (LABRACK authorAddress RABRACK)?)*
      (nl|EOF)
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
    : COLON BANG? attributeName BANG? COLON SP* attributeValue? (NL|EOF)
    ;

attributeValue
    : attributeValuePart (PLUS NL SP* attributeValuePart)*
    ;

attributeValuePart
    : OTHER+
    ;

attributeList
    : LSBRACK
      OTHER*
      RSBRACK ((CR? NL)|EOF) //{!isNextCharEOF()}?(CR? NL) TODO
    ;

attributeList1
    : LSBRACK
      ((positionalAttribute|namedAttribute) (SP|TAB)*
            (COMMA (positionalAttribute|namedAttribute) (SP|TAB)*)*
      |)
      RSBRACK (CR? NL)?
    ;

positionalAttribute
    : attributeName
    ;

namedAttribute
    : attributeName EQ attributeValuePart?
    ;

preamble
    : block[false] (bl|nl|block[false])*
    ;

section
    : sectionTitle (bl|nl|block[false])*
    ;

sectionTitle :
    EQ+ (SP|TAB)* title? (NL|EOF)
    ;

// A block should have only one anchor, but this is checked
// in the listener. The grammar is tolerant if multiple
// consecutive anchors are defined. Same for block title.
block1[boolean fromList]       // argument 'fromList' indicates that block is attached to a list item
    : ((anchor|attributeList)* literalBlock | // literal block must be detected before block title
          (anchor|attributeList|blockTitle)+
          (multiComment
          |singleComment
          |unorderedList
          |sourceBlock
          |literalBlock
          |paragraph[$fromList]
          )?
          |
          (anchor|attributeList|blockTitle)*
          (multiComment
          |singleComment
          |unorderedList
          |sourceBlock
          |literalBlock
          |paragraph[$fromList]
          )
      )
    ;

block[boolean fromList]       // argument 'fromList' indicates that block is attached to a list item
    : ((anchor|attributeList)* literalBlock | // literal block must be detected before block title
          (anchor|attributeList|blockTitle)*
          (multiComment
          |singleComment
          |unorderedList
          |sourceBlock
          |literalBlock
          |paragraph[$fromList]
          )
      )
    ;

blockTitle
    : DOT title? (NL|EOF)
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
      |{isNewLineInParagraph($fromList)}? NL
      )+ nl? // TODO (nl|EOF)?
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
      (NL|EOF)
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
      SLASH SLASH SLASH SLASH (NL|EOF)
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
      MINUS MINUS MINUS MINUS (NL|EOF)
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
      DOT DOT DOT DOT (NL|EOF)
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
