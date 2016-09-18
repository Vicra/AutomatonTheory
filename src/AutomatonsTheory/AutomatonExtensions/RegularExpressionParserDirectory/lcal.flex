package AutomatonsTheory.AutomatonExtensions.RegularExpressionParserDirectory;

import java_cup.runtime.*;

%%


%class Lexer


%line
%column


%cup


%{
    private Symbol symbol(int type) {
        return new Symbol(type, yyline, yycolumn);
    }
    private Symbol symbol(int type, Object value) {
        return new Symbol(type, yyline, yycolumn, value);
    }
%}


LineTerminator = \r|\n|\r\n


WhiteSpace     = {LineTerminator} | [ \t\f]

caracter = [0-9A-Za-z]

%%

<YYINITIAL> {


    "("                { return symbol(sym.LPAREN); }
    ")"                { return symbol(sym.RPAREN); }
    "*"                { return symbol(sym.ASTERISK); }
    "."                { return symbol(sym.AND); }
    "+"                { return symbol(sym.OR); }

    {caracter}         { return symbol(sym.CARACTER, yytext()); }

    {WhiteSpace}       { /* just skip what was found, do nothing */ }
}



[^]                    { throw new Error("Illegal character <"+yytext()+">"); }
