package lucbui.rayscode.evaluator;

import lucbui.rayscode.lexer.RayscodeLexer;
import lucbui.rayscode.token.RayscodeCustomFunction;
import lucbui.rayscode.token.RayscodeFunctionMetadata;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static lucbui.rayscode.token.Rayscode.*;
import static lucbui.rayscode.token.RayscodeFunctionMetadata.make;

/**
 * A testing ground for current features.
 */
public class Main {

    public static void main(String... args) throws IOException {
        //Example code, which adds 0 to the stack if no input was read, and 1 if input was read.
        RayscodeLexer lex = new RayscodeLexer(new StringReader("raysH raysSquare raysShy raysE raysB raysZ rays3 raysSquare"));
        RayscodeFunctionMetadata code;
        List<RayscodeFunctionMetadata> processedCode = new ArrayList<>();
        while((code = lex.nextToken()) != null){
            processedCode.add(code);
        }
        evaluate(processedCode);
        /*
        RayscodeCustomFunction customFunc = new RayscodeCustomFunction(4, make(ADD), make(ADD), make(ADD));
        List<RayscodeFunctionMetadata> code = Arrays.asList(make(THREE), make(TWO), make(TWO), make(TWO), make(TWO), make("custom", customFunc));
        evaluate(code);*/
    }

    private static void evaluate(List<RayscodeFunctionMetadata> code){
        RayscodeEvaluator eval = new RayscodeEvaluator();
        System.out.println(code.toString() + "=" + eval.evaluate(code) + ":" + eval.getVars());
    }
}
