grammar SCL;

@header {
package scl;
}

file: events NL* EOF;

events: event (NL NL+ event)*;

event: eventhead (NL eventattr)*;

eventhead: eventtype 'event' ID;

eventtype: 	'recurring' 
	|		'partial' 
	|		'general' 
	|;

eventattr: ID ':' value;

value:	STRING	# stringvalue
	|	list	# listvalue
	;

list: STRING (',' STRING)*;

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
