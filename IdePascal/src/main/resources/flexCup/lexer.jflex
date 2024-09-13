package com.baquiax.idepascal.backend;

import java_cup.runtime.*;
import java.util.*;
import com.baquiax.idepascal.backend.errores.*;

%%

%class Lexer
%public
%cup
%full
%line
%column
%char

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]+


    /* comments */
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}

TraditionalComment   = "(*" [^*] ~"*)" | "(*" "*"+ ")"
    // Comment can be the last line of the file, without line terminator.
 EndOfLineComment     = "{" {InputCharacter}* {LineTerminator}?"}"
DocumentationComment = "(*" {CommentContent} "*"+ ")"
CommentContent       = ( [^*] | \*+ [^/*] )*

//expresiones regulares
ENTERO = [0-9]+
DECIMAL = {ENTERO}"."{ENTERO}
ID = [a-zA-Z][a-zA-Z0-9_]*
CARACTER = \'.\'
CADENA = '([^'\n]|'')*'
TRUE = "1"
FALSE = "0"

%{
    
    public List<ErrorPascal> listErrores = new ArrayList<>();

    private Symbol symbol(int type) {
        return new Symbol(type, yyline+1, yycolumn+1);
    }

    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline+1, yycolumn+1, value);
    }

    private Symbol symbol(int type, Object value, int row, int col) {
        return new Symbol(type, row+1, col+1, value);
    }


  private void error(String message) {
    System.out.println("Error en linea line "+(yyline+1)+", columna "+(yycolumn+1)+" : "+message);
  }

%}

%ignorecase

%state STRING

%%

<YYINITIAL> "AND" { return symbol(sym.AND, yytext()); }
<YYINITIAL> "ARRAY" { return symbol(sym.ARRAY, yytext()); }
<YYINITIAL> "BEGIN" { return symbol(sym.BEGIN, yytext()); }
<YYINITIAL> "BREAK" { return symbol(sym.BREAK, yytext()); }
<YYINITIAL> "CASE" { return symbol(sym.CASE, yytext()); }
<YYINITIAL> "CONST" { return symbol(sym.CONST, yytext()); }
<YYINITIAL> "CONTINUE" { return symbol(sym.CONTINUE, yytext()); }
<YYINITIAL> "DIV" { return symbol(sym.DIV, yytext()); } // DIVISION
<YYINITIAL> "DO" { return symbol(sym.DO, yytext()); }
<YYINITIAL> "DOWNTO" { return symbol(sym.DOWNTO, yytext()); }
<YYINITIAL> "ELSE" { return symbol(sym.ELSE, yytext()); }
<YYINITIAL> "END" { return symbol(sym.END, yytext()); }
<YYINITIAL> "FILE" { return symbol(sym.FILE, yytext()); }
<YYINITIAL> "FUNCTION" { return symbol(sym.FUNCTION, yytext()); }
<YYINITIAL> "GOTO" { return symbol(sym.GOTO, yytext()); }
<YYINITIAL> "IF" { return symbol(sym.IF, yytext()); }
<YYINITIAL> "IN" { return symbol(sym.IN, yytext()); }
<YYINITIAL> "LABEL" { return symbol(sym.LABEL, yytext()); }
<YYINITIAL> "MOD" { return symbol(sym.MOD, yytext()); } // MODULO
<YYINITIAL> "NOT" { return symbol(sym.NOT, yytext()); }
<YYINITIAL> "OF" { return symbol(sym.OF, yytext()); }
<YYINITIAL> "OR" { return symbol(sym.OR, yytext()); }
<YYINITIAL> "PACKED" { return symbol(sym.PACKED, yytext()); }
<YYINITIAL> "PROCEDURE" { return symbol(sym.PROCEDURE, yytext()); }
<YYINITIAL> "PROGRAM" { return symbol(sym.PROGRAM, yytext()); }
<YYINITIAL> "RECORD" { return symbol(sym.RECORD, yytext()); }
<YYINITIAL> "REPEAT" { return symbol(sym.REPEAT, yytext()); }
<YYINITIAL> "readln" { return symbol(sym.REPEAT, yytext()); }
<YYINITIAL> "SET" { return symbol(sym.SET, yytext()); }
<YYINITIAL> "THEN" { return symbol(sym.THEN, yytext()); }
<YYINITIAL> "TO" { return symbol(sym.TO, yytext()); }
<YYINITIAL> "TYPE" { return symbol(sym.TYPE, yytext()); }
<YYINITIAL> "UNTIL" { return symbol(sym.UNTIL, yytext()); }
<YYINITIAL> "VAR" { return symbol(sym.VAR, yytext()); }
<YYINITIAL> "WHILE" { return symbol(sym.WHILE, yytext()); }
<YYINITIAL> "WITH" { return symbol(sym.WITH, yytext()); }
<YYINITIAL> "WRITELN" { return symbol(sym.WRITELN, yytext()); }

<YYINITIAL> "INTEGER" { return symbol(sym.INTEGER, yytext()); }
<YYINITIAL> "REAL" { return symbol(sym.REAL, yytext()); }
<YYINITIAL> "BOOLEAN" { return symbol(sym.BOOLEAN, yytext()); }
<YYINITIAL> "CHAR" { return symbol(sym.TKCHAR, yytext()); }
<YYINITIAL> "STRING" { return symbol(sym.STRING, yytext()); }
<YYINITIAL> "LONGINT" { return symbol(sym.LONGINT, yytext()); }

<YYINITIAL> "FOR" { return symbol(sym.FOR, yytext()); }

<YYINITIAL> "-" { return symbol(sym.MENOS, yytext()); }
<YYINITIAL> "+" { return symbol(sym.SUMA, yytext()); }
<YYINITIAL> "*" { return symbol(sym.MULTI, yytext()); }
<YYINITIAL> "/" { return symbol(sym.DIVISION, yytext()); }
<YYINITIAL> ":" { return symbol(sym.DOS_PUNTOS, yytext()); }
<YYINITIAL> ":=" { return symbol(sym.IGUAL, yytext()); }
<YYINITIAL> "," { return symbol(sym.COMA, yytext()); }
<YYINITIAL> ";" { return symbol(sym.PUNTO_COMA, yytext()); }
<YYINITIAL> "." { return symbol(sym.PUNTO, yytext()); }
<YYINITIAL> "..." { return symbol(sym.TRES_PUNTO, yytext()); }
<YYINITIAL> "\(" { return symbol(sym.PAR_A, yytext()); }
<YYINITIAL> "\)" { return symbol(sym.PAR_C, yytext()); }
<YYINITIAL> "\[" { return symbol(sym.COR_A, yytext()); }
<YYINITIAL> "\]" { return symbol(sym.COR_C, yytext()); }
<YYINITIAL> "=" { return symbol(sym.IGUALACION, yytext()); }
<YYINITIAL> "{" { return symbol(sym.LLAVE_A, yytext()); }
<YYINITIAL> "}" { return symbol(sym.LLAVE_C, yytext()); }
<YYINITIAL> "'" { return symbol(sym.COMILLA, yytext()); }
//RELACIONALES
<YYINITIAL> "<>" { return symbol(sym.DIFERENTE, yytext()); }
<YYINITIAL> ">" { return symbol(sym.MAYOR, yytext()); }
<YYINITIAL> "<"  { return symbol(sym.MENOR, yytext()); }
<YYINITIAL> "<=" { return symbol(sym.MENOR_IGUAL, yytext()); }
<YYINITIAL> ">=" { return symbol(sym.MAYOR_IGUAL, yytext()); }

<YYINITIAL> {ID} { return symbol(sym.IDENTIFICADOR, yytext()); }

<YYINITIAL> {TRUE}  { return symbol(sym.TRUE, yytext()); } //retorna 1
<YYINITIAL> {FALSE} { return symbol(sym.FALSE, yytext()); } //retorno 0

<YYINITIAL> {ENTERO} { return symbol(sym.ENTERO, yytext()); }
<YYINITIAL> {DECIMAL} { return symbol(sym.DECIMAL, yytext()); }
/*
<YYINITIAL> {CARACTER} { 
    String auxi = yytext().substring(1, yytext().length() - 1);
    return symbol(sym.CARACTER, auxi);
}
*/
<YYINITIAL> {CADENA} {
    return symbol(sym.CADENA, yytext().substring(1, yytext().length() - 1));
}

<YYINITIAL>    {Comment}                       {/* ignoramos */}

<YYINITIAL>    {WhiteSpace} 	                {/* ignoramos */}

/* error fallback */
[^]                              { listErrores.add(new ErrorPascal(TipoError.LEXICO.name(),
                                                    "El carácter " + yytext() + " no es aceptado en el lenguaje. ",
                                                    yyline + 1, yycolumn + 1)); }

<<EOF>>                 { return symbol(sym.EOF); }
