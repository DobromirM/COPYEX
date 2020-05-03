parser grammar CopyexParser;

options { tokenVocab = CopyexLexer; }

file                : blocks = fileBlock* EOF;

fileBlock           : codeBlock | functionBlock;

functionBlock       : function;
codeBlock           : block+ ;

block               : lines = line+                                                      # lines
                    | conditional                                                        # conditionalBlock
                    | loop                                                               # loopBlock ;

line                : statement TERM ;

statement           : initialisation                                                     # initialisationStatement
                    | evaluation                                                         # evaluationStatement
                    | arithmetic                                                         # arithmeticStatement
                    | logical                                                            # logicalStatement
                    | value = ID LPAREN parameters? RPAREN                               # functionCall ;

arithmetic          : left = arithmetic operator = (DIV | MULT | MOD) right = arithmetic # binaryOperation
                    | left = arithmetic operator = (PLUS | MINUS) right = arithmetic     # binaryOperation
                    | negation                                                           # negationOperation
                    | grouping                                                           # groupingOperation
                    | ID                                                                 # variable
                    | NUMBER                                                             # number ;

logical             : left = arithmetic operator = logicalOp right = arithmetic          # comparisonOperation
                    | operator = NOT value = logical                                     # notOperation
                    | left = logical operator = AND right = logical                      # binaryLogicOperation
                    | left = logical operator = OR right = logical                       # binaryLogicOperation
                    | arithmetic                                                         # arithmeticExpression ;

grouping            : LPAREN arithmetic RPAREN ;

negation            : operator = MINUS value = grouping                                  # groupingNegation
                    | operator = MINUS value = (ID | NUMBER)                             # valueNegation
                    | operator = MINUS value = negation                                  # nestedNegation ;


initialisation      : def = NUM name = ID assign = EQUALS value = arithmetic             # initNum
                    | def = BOOL name = ID assign = EQUALS value = logical               # initBool
                    | def = NUM name = ID                                                # declarationNum
                    | def = BOOL name = ID                                               # declarationBool
                    | name = ID assign = EQUALS value = arithmetic                       # assignmentNum
                    | name = ID assign = EQUALS value = logical                          # assignmentBool
                    | name = ID assign = EQUALS value = ID LPAREN parameters? RPAREN     # assignmentFunction
                    | name = ID operator = operators assign = EQUALS value = arithmetic  # augmented ;

evaluation          : op = PRINT LPAREN arithmetic RPAREN ;

conditional         : clause = IF LPAREN condition = logical RPAREN LCPAREN ifBlock = codeBlock RCPAREN (otherwise = ELSE LCPAREN elseBlock = codeBlock RCPAREN)? ;


loop                : clause = WHILE LPAREN condition = logical RPAREN LCPAREN whileBlock = codeBlock RCPAREN ;

function            : clause = FUNC type = VOID name = ID LPAREN  arguments?  RPAREN LCPAREN funcBlock = codeBlock RCPAREN
                    | clause = FUNC type = NUM name = ID LPAREN  arguments?  RPAREN LCPAREN funcBlock = codeBlock returnNum RCPAREN
                    | clause = FUNC type = BOOL name = ID LPAREN  arguments?  RPAREN LCPAREN funcBlock = codeBlock returnBool RCPAREN ;

arguments           : ((varType ID) (COMMA varType ID)*) ;
parameters          : (operation (COMMA operation)*) ;
operation           : (logical | arithmetic) ;
returnNum           : RETURN arithmetic TERM ;
returnBool          : RETURN logical TERM ;

operators           : (PLUS | MINUS | MULT | DIV | MOD) ;
logicalOp           : (GT | LT | EQUALS EQUALS | GT EQUALS | LT EQUALS) ;
varType             : (NUM | BOOL) ;
