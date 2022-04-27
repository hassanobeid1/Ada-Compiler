lexer grammar DecaLexer;

options {
	language = Java;
	// Tell ANTLR to make the generated lexer class extend the the named class, which is where any
	// supporting code and variables will be placed.
	superClass = AbstractDecaLexer;
}

@members {
}

// Synboles speciaux

LT: '<';
GT: '>';
EQUALS: '=';
PLUS: '+';
MINUS: '-';
TIMES: '*';
SLASH: '/';
PERCENT: '%';
DOT: '.';
COMMA: ',';
OPARENT: '(';
CPARENT: ')';
OBRACE: '{';
CBRACE: '}';
EXCLAM: '!';
SEMI: ';';
EQEQ: '==';
NEQ: '!=';
GEQ: '>=';
LEQ: '<=';
AND: '&&';
OR: '||';

PRINT: 'print';
PRINTLN: 'println';
PRINTX: 'printx';
PRINTLNX: 'printlnx';
WHILE: 'while';
RETURN: 'return';
IF: 'if';
ELSE: 'else';
INSTANCEOF: 'instanceof';
TRUE: 'true';
FALSE: 'false';
NULL: 'null';
CLASS: 'class';
EXTENDS: 'extends';
PROTECTED: 'protected';
ASM: 'asm';
THIS: 'this';
NEW: 'new';
READFLOAT: 'readFloat';
READINT: 'readInt';

// Litteraux entier
fragment POSITIVE_DIGIT: '1' .. '9';
INT: '0' | POSITIVE_DIGIT DIGIT*;

// Litteraux flotants
fragment NUM: DIGIT+;
fragment SIGN: ('+' | '-')?;
fragment EXP: ('E' | 'e') SIGN NUM;
fragment DEC: NUM '.' NUM;
fragment FLOATDEC: (DEC | DEC EXP) ('F' | 'f')?;
fragment DIGITHEX: '0' .. '9' | 'A' .. 'F' | 'a' .. 'f';
fragment NUMHEX: DIGITHEX+;
fragment FLOATHEX: ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f')?;
FLOAT: FLOATDEC | FLOATHEX;

// Chaine de characteres
fragment STRING_CAR: ~ ('"' | '\\' | '\n');
STRING: '"' (STRING_CAR | '\\"' | '\\\\')* '"';
MULTI_LINE_STRING:
	'"' (STRING_CAR | '\n' | '\\"' | '\\\\')* '"';

// Commentaires
fragment COMMENT: '//' (~'\n')* | '/*' .*? '*/';

// Separateurs
SEP: (' ' | '\t' | '\n' | '\r' | COMMENT) {skip();};

// Inclusion de fichier
fragment FILENAME: (LETTER | DIGIT | '.' | '-' | '_')+;
INCLUDE:
	'#include' (' ')* '"' FILENAME '"' { doInclude(getText());};


// Identificateurs
fragment DIGIT: '0' .. '9';
fragment LETTER: ( 'a' .. 'z' | 'A' .. 'Z');
IDENT: (LETTER | '$' | '_') (LETTER | DIGIT | '$' | '_')*;
