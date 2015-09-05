grammar Asciidoc;

@parser::members {

/*
    private boolean isPreviousChar(int charType) {
        if (_input.LT(1).getLine() == 1 &&
            _input.LT(1).getCharPositionInLine() == 0) return false;

        return _input.LA(-1) == charType;
    }
*/
/*
    private boolean isNextChar(int charType) {
        return _input.LA(2) == charType;
    }
*/
/*
    private boolean isFirstCharInLine1() {
    System.out.println("HH => " +_input.LT(1).getText()+_input.LT(2).getText()+_input.LT(3).getText()+_input.LT(4).getText()+_input.LT(5).getText()+_input.LT(6).getText()+_input.LT(7).getText()+_input.LT(8).getText() + " ----> "+(_input.LT(1).getCharPositionInLine() == 0));
        return _input.LT(1).getCharPositionInLine() == 0;
    }
*/
    private boolean isCurrentCharFirstCharInLine() {
        return _input.LT(1).getCharPositionInLine() == 0;
    }

    private boolean isNextLineATitle() {
        int i = 1;
        int nextChar = _input.LA(i);

        while (nextChar != EOF && nextChar != NL) {
            if (nextChar == EQ) {
                if (_input.LA(++i) == SP) {
                    return true;
                }
                nextChar = _input.LA(i);
                continue;
            }
            break;
        }
        return false;
    }


    private boolean isCurrentCharBeginningOfAComment() {
        return _input.LT(1).getCharPositionInLine() == 0
                    && _input.LA(1) == SLASH && _input.LA(2) == SLASH;
    }


    private boolean isNewLinePartOfParagraph() {
        boolean nextCharIsNL = (_input.LA(2) == NL);
        boolean nextCharIsEOF = (_input.LA(2) == EOF);
        boolean nextCharIsBeginningOfAComment = (_input.LA(2) == SLASH) && (_input.LA(3) == SLASH);

        return !nextCharIsNL && !nextCharIsEOF && !nextCharIsBeginningOfAComment;
    }
}

// Parser

document
    : (nl
      |multiComment
      |singleComment
      )*
      (header?|(nl|block)*)
      section*
    ;

header : documentTitle preamble? ;

documentTitle : EQ SP title? (NL|EOF) ;

preamble : (nl|block)+;

section : sectionTitle (nl|block)* ;

sectionTitle : EQ+ SP title? (NL|EOF) ;

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
    : {isCurrentCharFirstCharInLine()}?
      LSBRACK LSBRACK anchorId
      (COMMA anchorLabel)?
      RSBRACK RSBRACK NL?
    ;

anchorId : (OTHER)+ ;

anchorLabel
    : (OTHER
      |SP
      |EQ
      |SLASH
      |COMMA
      )+
    ;

title : ~(NL|EOF)+ ;

nl : CR? NL ;

paragraph
    : {!isNextLineATitle()}?
      (OTHER
      |SP
      |EQ
      |{!isCurrentCharBeginningOfAComment()}? SLASH
      |COMMA
      |LSBRACK
      |RSBRACK
      |MINUS
      |PLUS
      |DOT
      |{isNewLinePartOfParagraph()}? NL
      )+
    ;

singleComment
    : {isCurrentCharFirstCharInLine()}?
      SLASH SLASH
      (OTHER
      |SP
      |EQ
      |SLASH
      |COMMA
      |LSBRACK
      |RSBRACK
      |MINUS
      |PLUS
      |DOT
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
      |MINUS
      |PLUS
      |DOT
      |NL
      )*?
      multiCommentDelimiter
    ;

multiCommentDelimiter
    : {isCurrentCharFirstCharInLine()}?
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
      |MINUS
      |PLUS
      |DOT
      |NL
      )*?
      sourceBlockDelimiter
    ;

sourceBlockDelimiter
    : {isCurrentCharFirstCharInLine()}?
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
      |MINUS
      |PLUS
      |DOT
      |NL
      )*?
      literalBlockDelimiter
    ;

literalBlockDelimiter
    : {isCurrentCharFirstCharInLine()}?
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
COMMA       : ','  ;
MINUS       : '-'  ;
PLUS        : '+'  ;
TIMES       : '*'  ;
DOT         : '.'  ;
OTHER       : .    ;

/* other chars to define

SEMICOLON    : ';';
COLON        : ':';

LPAREN          : '(';
RPAREN          : ')';
LBRACE          : '{';
RBRACE          : '}';
LABRACK      : '<';
RABRACK      : '>';

SEMI            : ';';
COMMA           : ',';

BANG            : '!';
TILDE           : '~';
QUESTION        : '?';
COLON           : ':';


*/
