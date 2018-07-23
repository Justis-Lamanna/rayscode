/* Rayscode Grammar */
package lucbui.rayscode.lexer;

import lucbui.rayscode.token.Rayscode;
import lucbui.rayscode.token.RayscodeFunction;
import lucbui.rayscode.token.RayscodeFunctionMetadata;

%%

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
%debug

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

%%

//<YYINITIAL> {
//    
//}

/* error fallback */
[^]                              { throw new Error("Illegal character <"+
                                                    yytext()+">"); }