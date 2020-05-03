lexer grammar CopyexLexer;

// General
PRINT               : 'print' ;
EQUALS              : '=' ;
LPAREN              : '(' ;
RPAREN              : ')' ;
LCPAREN             : '{' ;
RCPAREN             : '}' ;
fragment POINT               : '.' ;
fragment DIGIT               : ('0' .. '9') ;
fragment LETTER              : ('a' .. 'z') | ('A' .. 'Z') ;

// Comments
COMMENT             : '#' .*? '#' -> skip;

// Operators
PLUS                : '+' ;
MINUS               : '-' ;
MULT                : '*' ;
DIV                 : '/' ;
MOD                 : '%' ;

// Conditionals
IF                  : 'if' ;
ELSE                : 'else' ;
GT                  : '>' ;
LT                  : '<' ;
AND                 : 'and' ;
OR                  : 'or' ;
NOT                 : 'not' ;

// Loops
WHILE               : 'while' ;

// Functions
FUNC                : 'function' ;
VOID                : 'void';
COMMA               : ',';
RETURN               : 'return';

// Numbers
NUMBER              : DIGIT+ (POINT DIGIT+)? ;

// Variables
NUM                 : 'num' ;
BOOL                : 'bool' ;
ID                  : LETTER (LETTER | DIGIT)* ;

//Command Terminator
TERM                : ';' ;

//White Space
WS                  : [ \t\r\n]+ -> skip ;
