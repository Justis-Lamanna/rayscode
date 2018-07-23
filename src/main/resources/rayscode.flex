/* Rayscode Grammar */
package lucbui.rayscode.lexer;

import lucbui.rayscode.token.Rayscode;
import lucbui.rayscode.token.RayscodeFunction;
import lucbui.rayscode.token.RayscodeFunctionMetadata;

%%

%public
%class RayscodeLexer
%unicode
%line
%column

%{
    private RayscodeFunctionMetadata code(String id, RayscodeFunction func){
        return RayscodeFunctionMetadata.make(id, func);
    }

    private RayscodeFunctionMetadata code(RayscodeFunction func){
        return RayscodeFunctionMetadata.make(func);
    }
%}

%function nextToken
%type RayscodeFunctionMetadata
%eofval{
    //Marks the End Of File
    return null;
%eofval}
//This prints a debugging line after every parse. Remove it if it's annoying.
%debug

LineTerminator  = \r|\n|\r\n
InputCharacter  = [^\r\n]
WhiteSpace      = {LineTerminator} | [ \t\f]
VarName         = "rays" [A-Z0-9] [A-Za-z0-9]*

%%

<YYINITIAL> {
    //Literals are straightforward to parse.
    "rays2"                         {return code(Rayscode.TWO);}
    "rays3"                         {return code(Rayscode.THREE);}
    "rays3c"                        {return code(Rayscode.SIZE);}
    "raysP"                         {return code(Rayscode.ADD);}
    "raysI"                         {return code(Rayscode.SUBTRACT);}
    "raysB"                         {return code(Rayscode.MULTIPLY);}
    "raysA"                         {return code(Rayscode.DIVIDE);}
    "raysLick"                      {return code(Rayscode.INPUT);}
    "raysQ"                         {return code(Rayscode.OUTPUT);}
    "raysShock"                     {return code(Rayscode.SWAP);}
    "raysD"                         {return code(Rayscode.POP);}
    "raysThump"                     {return code(Rayscode.ROLL);}
    "raysE"                         {return code(Rayscode.DUPLICATE);}
    "raysLove"                      {return code(Rayscode.ASSIGNMENT);}

    {VarName}                       {return code(yytext(), Rayscode.VARIABLE);}

    /* whitespace */
    {WhiteSpace}                    { /* ignore */ }
}

/* error fallback */
[^]                              { throw new Error("Illegal character <"+
                                                    yytext()+">"); }