grammar Asciidoc;

@parser::members {

/*
    private boolean isPreviousChar(int charType) {
        if (_input.LT(1).getLine() == 1 &&
            _input.LT(1).getCharPositionInLine() == 0) return false;

        return _input.LA(-1) == charType;
    }
*/

    private boolean isFirstCharInLine() {
        return _input.LT(1).getCharPositionInLine() == 0;
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

    private boolean isNewLineInParagraph() {
        boolean nextCharIsNL = (_input.LA(2) == NL);
        boolean nextCharIsEOF = (_input.LA(2) == EOF);
        boolean nextCharIsBeginningOfAComment = (_input.LA(2) == SLASH) && (_input.LA(3) == SLASH);

        return !nextCharIsNL && !nextCharIsEOF && !nextCharIsBeginningOfAComment;
    }

    // ---------------------------------------------------------------
    // 'isStartOf' element methods
    // ---------------------------------------------------------------

    private boolean isStartOfTitle() {
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

        return !currentCharIsNL && !isStartOfTitle();
    }

    private boolean isStartOfParagraph() {
        return !isStartOfTitle();
    }

}

// Parser

document
    : (nl
      |multiComment
      |singleComment
      )*
      (header (nl|multiComment|singleComment)* preamble?)?
      (nl
      |attributeEntry
      |block
      |section
      )*
    ;

header
    : documentTitle
      (multiComment|singleComment)*
      authors?
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

attributeEntry
    : COLON attributeName COLON SP* attributeValue (NL|EOF)
    ;

attributeName
    : OTHER+
    ;

attributeValue
    : OTHER+
    ;

preamble
    : (nl|block)+
    ;

section
    : sectionTitle (nl|block)*
    ;

sectionTitle :
    EQ+ SP title? (NL|EOF)
    ;

// A block should have only one anchor, but this is checked
// in the listener. The grammar is tolerant if multiple
// consecutive anchors are defined
block
    : anchor*
      (multiComment
      |singleComment
      |sourceBlock
      |literalBlock
      |paragraph
      )
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

title
    : ~(NL|EOF)+
    ;

nl
    : CR? NL
    ;

paragraph
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
      |{isNewLineInParagraph()}? NL
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


// Lexer

EQ          : '='  ;
SP          : ' '  ;
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
COMMA           : ',';

TILDE           : '~';
QUESTION        : '?';


*/
