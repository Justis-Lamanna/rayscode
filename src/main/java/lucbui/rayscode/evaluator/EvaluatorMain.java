package lucbui.rayscode.evaluator;

import lucbui.rayscode.lexer.RayscodeLexer;
import lucbui.rayscode.token.RayscodeFunctionMetadata;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A testing ground for current features.
 */
public class EvaluatorMain {

    public static void main(String... args) throws IOException {
        //Example code, which defines method raysSquare, and applies it to the number 3.
        RayscodeLexer lex = new RayscodeLexer(new StringReader("raysH raysSquare raysShy raysE raysB raysZ rays3 raysSquare"));
        RayscodeFunctionMetadata code;
        List<RayscodeFunctionMetadata> processedCode = new ArrayList<>();
        while((code = lex.nextToken()) != null){
            processedCode.add(code);
        }
        evaluate(processedCode);
    }

    private static void evaluate(List<RayscodeFunctionMetadata> code){
        RayscodeEvaluator eval = new RayscodeEvaluator();
        System.out.println(code.toString() + "=" + eval.evaluate(code) + ":" + eval.getVars());
    }
}
