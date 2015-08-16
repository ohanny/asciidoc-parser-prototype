grammar Asciidoc;

@parser::members {

    private boolean isPreviousChar(int charType) {
        if (_input.LT(1).getLine() == 1 &&
            _input.LT(1).getCharPositionInLine() == 0) return false;

        return _input.LA(-1) == charType;
    }

    private boolean isNextChar(int charType) {
        return _input.LA(2) == charType;
    }

    private boolean isFirstCharInLine() {
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

    private boolean isNextCharBeginningOfAComment() {
        return _input.LT(1).getCharPositionInLine() == 0
                    && _input.LA(1) == SLASH && _input.LA(2) == SLASH;
    }
}

// Parser

document        : (nl|singleComment)* (header?|(nl|singleComment|paragraph)*) section* ;

header : documentTitle preamble? ;
documentTitle : EQ SP title? (NL|EOF) ;

preamble : (nl|singleComment|paragraph)+;

section : sectionTitle (nl|singleComment|paragraph)* ;
sectionTitle : EQ+ SP title? (NL|EOF) ;

title : ~(NL|EOF)+ ;

nl : CR? NL ;

paragraph : {!isNextLineATitle()}?
            (OTHER|SP|EQ|
            {!isNextCharBeginningOfAComment()}? SLASH|
           {!isPreviousChar(NL)}? NL)+
            ;

singleComment : {isFirstCharInLine()}? SLASH SLASH (OTHER|SP|EQ|SLASH)* (NL|EOF) ;

// Lexer

EQ          : '='  ;
SP          : ' '  ;
CR          : '\r' ;
NL          : '\n' ;
SLASH       : '/'  ;
OTHER       : .    ;

/* other chars to define
COMMA        : ',';
SEMICOLON    : ';';
COLON        : ':';

LPAREN          : '(';
RPAREN          : ')';
LBRACE          : '{';
RBRACE          : '}';
LSBRACK      : '[';
RSBRACK      : ']';
LABRACK      : '<';
RABRACK      : '>';

SEMI            : ';';
COMMA           : ',';
DOT             : '.';
BANG            : '!';
TILDE           : '~';
QUESTION        : '?';
COLON           : ':';

PLUS : '+';
MINUS : '-';
TIMES : '*';

*/
