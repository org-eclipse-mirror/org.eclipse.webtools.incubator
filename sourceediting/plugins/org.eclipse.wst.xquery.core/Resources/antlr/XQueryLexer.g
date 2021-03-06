lexer grammar XQueryLexer;

options {
superClass='XQDTLexer';
}

tokens {
// Imported tokens
L_QuotAttrContentChar;
L_AposAttrContentChar;
L_ElementContentChar;
L_CDataSection;
L_PredefinedEntityRef;
L_CharRef;
ESCAPE_LBRACKET;
ESCAPE_RBRACKET;
ESCAPE_APOS;
ESCAPE_QUOT;
CDATA_START;
CDATA_END;
}

@header {
/*******************************************************************************
 * Copyright (c) 2008, 2009 28msec Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Gabriel Petrovay (28msec) - initial API and implementation
 *     Sam Neth (Mark Logic)
 *******************************************************************************/
package org.eclipse.wst.xquery.internal.core.parser.antlr;
}

@lexer::members {
// dummy list for warning elimination
List<Stack<Object>> dummy = new ArrayList<Stack<Object>>();
}


// **********************************
// "keywords" present in the language
// **********************************
// XQuery 1.0
ANCESTOR                    : 'ancestor';
ANCESTOR_OR_SELF            : 'ancestor-or-self';
AND                         : 'and';
AS                          : 'as';
ASCENDING                   : 'ascending';
AT                          : 'at';
ATTRIBUTE                   : 'attribute';
BASE_URI                    : 'base-uri';
BOUNDARY_SPACE              : 'boundary-space';
BY                          : 'by';
CASE                        : 'case';
CAST                        : 'cast';
CASTABLE                    : 'castable';
CHILD                       : 'child';
COLLATION                   : 'collation';
COMMENT                     : 'comment';
CONSTRUCTION                : 'construction';
COPY_NAMESPACES             : 'copy-namespaces';
DECLARE                     : 'declare';
DEFAULT                     : 'default';
DESCENDANT                  : 'descendant';
DESCENDANT_OR_SELF          : 'descendant-or-self';
DESCENDING                  : 'descending';
DIV                         : 'div';
DOCUMENT                    : 'document';
DOCUMENT_NODE               : 'document-node';
ELEMENT                     : 'element';
ELSE                        : 'else';
EMPTY                       : 'empty';
EMPTY_SEQUENCE              : 'empty-sequence';
ENCODING                    : 'encoding';
EQ                          : 'eq';
EVERY                       : 'every';
EXCEPT                      : 'except';
EXTERNAL                    : 'external';
FOLLOWING                   : 'following';
FOLLOWING_SIBLING           : 'following-sibling';
FOR                         : 'for';
FUNCTION                    : 'function';
GE                          : 'ge';
GREATEST                    : 'greatest';
GT                          : 'gt';
IDIV                        : 'idiv';
IF                          : 'if';
IMPORT                      : 'import';
IN                          : 'in';
INHERIT                     : 'inherit';
INSTANCE                    : 'instance';
INTERSECT                   : 'intersect';
IS                          : 'is';
ITEM                        : 'item';
LAX                         : 'lax';
LE                          : 'le';
LEAST                       : 'least';
LET                         : 'let';
LT                          : 'lt';
MOD                         : 'mod';
MODULE                      : 'module';
NAMESPACE                   : 'namespace';
NE                          : 'ne';
NO_INHERIT                  : 'no-inherit';
NO_PRESERVE                 : 'no-preserve';
NODE                        : 'node';
OF                          : 'of';
OPTION                      : 'option';
OR                          : 'or';
ORDER                       : 'order';
ORDERED                     : 'ordered';
ORDERING                    : 'ordering';
PARENT                      : 'parent';
PRECEDING                   : 'preceding';
PRECEDING_SIBLING           : 'preceding-sibling';
PRESERVE                    : 'preserve';
PROCESSING_INSTRUCTION      : 'processing-instruction';
RETURN                      : 'return';
SATISFIES                   : 'satisfies';
SCHEMA                      : 'schema';
SCHEMA_ATTRIBUTE            : 'schema-attribute';
SCHEMA_ELEMENT              : 'schema-element';
SELF                        : 'self';
SOME                        : 'some';
STABLE                      : 'stable';
STRICT                      : 'strict';
STRIP                       : 'strip';
TEXT                        : 'text';
THEN                        : 'then';
TO                          : 'to';
TREAT                       : 'treat';
TYPESWITCH                  : 'typeswitch';
UNION                       : 'union';
UNORDERED                   : 'unordered';
VALIDATE                    : 'validate';
VARIABLE                    : 'variable';
VERSION                     : 'version';
WHERE                       : 'where';
XQUERY                      : 'xquery';
// XQuery 1.1 (only additional keywords only)
CATCH                       : 'catch';
CONTEXT                     : 'context';
COUNT                       : 'count';
DECIMAL_FORMAT              : 'decimal-format';
DECIMAL_SEPARATOR           : 'decimal-separator';
DIGIT                       : 'digit';
END                         : 'end';
GROUP                       : 'group';
GROUPING_SEPARATOR          : 'grouping-separator';
INFINITY                    : 'infinity';
MINUS_SIGN                  : 'minus-sign';
NAMESPACE_NODE              : 'namespace-node';
NAN                         : 'NaN';
NEXT                        : 'next';
ONLY                        : 'only';
OUTER                       : 'outer';
PATTERN_SEPARATOR           : 'pattern-separator';
PERCENT                     : 'percent';
PER_MILLE                   : 'per-mille';
PREVIOUS                    : 'previous';
SLIDING                     : 'sliding';
START                       : 'start';
TRY                         : 'try';
TUMBLING                    : 'tumbling';
WHEN                        : 'when';
WINDOW                      : 'window';
ZERO_DIGIT                  : 'zero-digit';
// XQuery Update 1.0 (only additional keywords only)
AFTER                       : 'after';
BEFORE                      : 'before';
COPY                        : 'copy';
DELETE                      : 'delete';
FIRST                       : 'first';
INSERT                      : 'insert';
INTO                        : 'into';
LAST                        : 'last';
MODIFY                      : 'modify';
NODES                       : 'nodes';
RENAME                      : 'rename';
REPLACE                     : 'replace';
REVALIDATION                : 'revalidation';
SKIP                        : 'skip';
UPDATING                    : 'updating';
VALUE                       : 'value';
WITH                        : 'with';
// XQuery Scripting Extensions 1.0 (only additional keywords only)
BLOCK                       : 'block';
CONSTANT                    : 'constant';
EXIT                        : 'exit';
RETURNING                   : 'returning';
SEQUENTIAL                  : 'sequential';
SET                         : 'set';
SIMPLE                      : 'simple';
WHILE                       : 'while';
// Zorba XQuery Extensions
EVAL                        : 'eval';
USING                       : 'using';
// Zorba DDL Extensions (only additional keywords only)
APPEND_ONLY                 : 'append-only';
AUTOMATICALLY               : 'automatically';
CHECK                       : 'check';
COLLECTION                  : 'collection';
CONSTRAINT                  : 'constraint';
CONST                       : 'const';
EQUALITY                    : 'equality';
FOREACH                     : 'foreach';
FOREIGN                     : 'foreign';
FROM                        : 'from';
INDEX                       : 'index';
INTEGRITY                   : 'integrity';
KEY                         : 'key';
MAINTAINED                  : 'maintained';
MANUALLY                    : 'manually';
MUTABLE                     : 'mutable';
NON                         : 'non';
ON                          : 'on';
QUEUE                       : 'queue';
RANGE                       : 'range';
READ_ONLY                   : 'read-only';
UNIQUE                      : 'unique';
// MarkLogic
BINARY                      : 'binary';
PRIVATE                     : 'private';

// entity references
AMP_ER  : 'amp';
APOS_ER : 'apos';
QUOT_ER : 'quot';
//GT    :   'gt';
//LT    :   'lt';

// *******************************************
// signs and operators present in the language
// *******************************************
LPAREN                  : '(';
RPAREN                  : ')';
DOLLAR                  : '$';
LBRACKET                : '{';
RBRACKET                : '}';
LSQUARE                 : '[';
RSQUARE                 : ']';
EQUAL                   : '=';
BIND                    : ':=';
NOTEQUAL                : '!=';
AMP                     : '&' ;
COMMA                   : ',';
QUESTION                : '?';
STAR                    : '*';
PLUS                    : '+';
MINUS                   : '-';
SMALLER                 : '<';
GREATER                 : '>';
SMALLEREQ               : '<=';
GREATEREQ               : '>=';
SMALLER_SMALLER         : '<<';
GREATER_GREATER         : '>>';
SLASH                   : '/';
SLASH_SLASH             : '//';
DOT                     : '.';
DOT_DOT                 : '..';
COLON                   : ':';
COLON_COLON             : '::';
EMPTY_CLOSE_TAG         : '/>';
CLOSE_TAG               : '</';
SEMICOLON               : ';' ;
VBAR                    : '|';
PRAGMA_START            : '(#';
PRAGMA_END              : '#)';
XML_COMMENT_START       : '<!--';
XML_COMMENT_END         : '-->';
PI_START                : '<?';
PI_END                  : '?>';
ATTR_SIGN               : '@';
CHARREF_DEC             : '&#';
CHARREF_HEX             : '&#x';
APOS                    : '\'';
QUOT                    : '"';


L_NCName
        :   NCNameStartChar NCNameChar*
        ;

fragment Letter             : 'a'..'z' | 'A'..'Z' ;
fragment HexLetter          : 'a'..'f' | 'A'..'F' ;
fragment Digit              : '0'..'9' ;
fragment Digits             : Digit+ ;

fragment NCNameStartChar    : Letter | '_' ;
fragment NCNameChar         : Letter | Digit | '.' | '-' | '_' ; // | CombiningChar | Extender ;

S
        : ('\t' | ' ' | '\n' | '\r')+  { $channel = HIDDEN; }
        ;
fragment SU
        : ('\t' | ' ' | '\n' | '\r')+
        ;



// ****************
// EBNF Productions
// ****************

//[66]  /* ws: explicit */
L_Pragma
        : PRAGMA_START SU? L_NCName COLON L_NCName (SU (options {greedy=false;} : .)*)? PRAGMA_END
        ;
        
//[80]  /* ws: explicit */
//L_Wildcard
//      :
//      ;

//[103] /* ws: explicit */
L_DirCommentConstructor 
        : XML_COMMENT_START (options {greedy=false;} : .* ) XML_COMMENT_END
        ;

// TODO: Unallowed '--' in the comment content
//[104] /* ws: explicit */
//DirCommentContents

//[105] /* ws: explicit */
L_DirPIConstructor  
        : PI_START SU? L_NCName (SU(options {greedy=false;} : .*))? PI_END
        ;

//[106] /* ws: explicit */ - resolved in the previous production
//DirPIContents

//[141] /* ws: explicit */
L_IntegerLiteral
        :   Digits
        ;

//[142] /* ws: explicit */
L_DecimalLiteral
        : ('.' Digits) | (Digits '.' Digit*)
        ;

//[143] /* ws: explicit */
L_DoubleLiteral
        : (('.' Digits) | (Digits ('.' Digit*)?)) ('e' | 'E') ('+'|'-')? Digits
        ;

//[151]
L_Comment
        : '(:' (options {greedy=false;}: L_Comment | . )* ':)' { $channel = HIDDEN; }
        ;

L_AnyChar : . ;
