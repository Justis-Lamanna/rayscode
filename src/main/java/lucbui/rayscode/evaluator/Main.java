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
public class Main {

    public static void main(String... args) throws IOException {
        //Example code, which runs 2*2.
        RayscodeLexer lex = new RayscodeLexer(new StringReader("rays2 rays2 raysB"));
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
