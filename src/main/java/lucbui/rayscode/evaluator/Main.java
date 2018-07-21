package lucbui.rayscode.evaluator;

import lucbui.rayscode.token.RayscodeFunctionMetadata;

import java.util.Arrays;
import java.util.List;

import static lucbui.rayscode.token.Rayscode.*;
import static lucbui.rayscode.token.RayscodeFunctionMetadata.make;

/**
 * A testing ground for current features.
 */
public class Main {

    public static void main(String... args) {
        //1 at the top of the stack if the user inputs something, 0 if the user inputs nothing.
        evaluate(
                make(INPUT),
                make(SIZE),
                make("0", IF), //The IDs for this IF-ELSE-ENDIF blocks must match.
                    make(THREE),
                make("0", ELSE),
                    make(TWO),
                make("0", ENDIF),
                make(THREE),
                make(SUBTRACT));
    }

    private static void evaluate(RayscodeFunctionMetadata... funcs){
        RayscodeEvaluator eval = new RayscodeEvaluator();
        List<RayscodeFunctionMetadata> code = Arrays.asList(funcs);
        System.out.println(code.toString() + "=" + eval.evaluate(code) + ":" + eval.getVars());
    }
}
