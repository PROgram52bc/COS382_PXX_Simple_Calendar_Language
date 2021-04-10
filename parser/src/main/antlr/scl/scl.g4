grammar scl;

file: event (NL NL event)*;

event: hdr (NL body)*;

hdr: 'event' ID;

body: ID ':' (ID)+;

ID: ALPHA (ALPHNUM)*;
NL: '\n';
SPACE: [ \t] -> skip;

fragment UPPER: [A-Z];
fragment LOWER: [a-z];
fragment ALPHA: (UPPER|LOWER);
fragment DIGIT: [0-9];
fragment ALPHNUM: (ALPHA|DIGIT);
