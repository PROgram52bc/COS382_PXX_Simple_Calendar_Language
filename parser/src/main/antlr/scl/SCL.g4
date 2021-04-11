grammar SCL;

@header {
package scl;
}

file: events NL* EOF;

events: event (NL NL+ event)*;

event: eventhead (NL eventattr)*;

eventhead: 'event' ID;

eventattr: ID ':' value;

value:	words	# wordvalue
	|	list	# listvalue
	|	STRING	# stringvalue
	|	~(NL)+	# textvalue
	;

words: (WORD)+; 
list: WORD (',' WORD)*;

STRING: '"' .*? '"';
ID: ALPHA (ALPHA|DIGIT)*;
WORD: (ALPHA|DIGIT)+;
NL: '\n';
SPACE: [ \t]+ -> skip;

fragment UPPER: [A-Z];
fragment LOWER: [a-z];
fragment ALPHA: (UPPER|LOWER);
fragment DIGIT: [0-9];
fragment ALPHNUM: (ALPHA|DIGIT);
