/* Rayscode Grammar */
package lucbui.rayscode.lexer;

import lucbui.rayscode.token.*;

import java.util.Deque;
import java.util.LinkedList;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

%%

%public
%class RayscodeLexer
%unicode
%line
%column

%{
    private Deque<String> loopId = new LinkedList<>(); 
    private Deque<String> ifElseId = new LinkedList<>(); 

    private BigInteger loopHighestId = BigInteger.ZERO;
    private BigInteger ifElseHighestId = BigInteger.ZERO;
    
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
//%debug

LineTerminator  = \r|\n|\r\n
InputCharacter  = [^\r\n]
WhiteSpace      = {LineTerminator} | [ \t\f]
VarName         = "rays" [A-Z0-9] [A-Za-z0-9]*

%state FUNC

%%

<YYINITIAL> {
    //Literals are straightforward to parse.
    "rays2"                         {return code(yytext(), Rayscode.TWO);}
    "rays3"                         {return code(yytext(), Rayscode.THREE);}
    "rays3c"                        {return code(yytext(), Rayscode.SIZE);}
    "raysP"                         {return code(yytext(), Rayscode.ADD);}
    "raysI"                         {return code(yytext(), Rayscode.SUBTRACT);}
    "raysB"                         {return code(yytext(), Rayscode.MULTIPLY);}
    "raysA"                         {return code(yytext(), Rayscode.DIVIDE);}
    "raysLick"                      {return code(yytext(), Rayscode.INPUT);}
    "raysQ"                         {return code(yytext(), Rayscode.OUTPUT);}
    "raysShock"                     {return code(yytext(), Rayscode.SWAP);}
    "raysD"                         {return code(yytext(), Rayscode.POP);}
    "raysThump"                     {return code(yytext(), Rayscode.ROLL);}
    "raysE"                         {return code(yytext(), Rayscode.DUPLICATE);}
    "raysLove"                      {return code(Rayscode.ASSIGNMENT);}
    //Control structures
    "raysC"                         {String id = loopHighestId.toString(); loopHighestId = loopHighestId.add(BigInteger.ONE); loopId.push(id); return code(id, Rayscode.STARTLOOP);}
    "raysLurk"                      {return code(loopId.pop(), Rayscode.ENDLOOP);}
    "raysShrug"                     {String id = ifElseHighestId.toString(); ifElseHighestId = ifElseHighestId.add(BigInteger.ONE); ifElseId.push(id); return code(id, Rayscode.IF);}
    "raysT"                         {return code(ifElseId.peek(), Rayscode.ELSE);}
    "raysFox"                       {return code(ifElseId.pop(), Rayscode.ENDIF);}
    //Method Declaration
    "raysH"                         {yybegin(FUNC); return code(Rayscode.STARTFUNC);}
    "raysShy"                       {return code(Rayscode.PARAM);}
    "raysZ"                         {return code(Rayscode.ENDFUNC);}
}

<FUNC>{
    {VarName}                       {yybegin(YYINITIAL); return code(yytext(), Rayscode.METHOD);}

    /* whitespace */
    {WhiteSpace}                    { /* ignore */ }
}

//Only in the last case should VarName be checked. 
<YYINITIAL>{

    {VarName}                       {return code(yytext(), Rayscode.VARIABLE);}

    /* whitespace */
    {WhiteSpace}                    { /* ignore */ }
}

/* error fallback */
[^]                              { throw new Error("Illegal character <"+
                                                    yytext()+">"); }