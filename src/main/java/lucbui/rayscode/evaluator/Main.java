package lucbui.rayscode.evaluator;

import lucbui.rayscode.token.RayscodeFunction;

import java.util.Arrays;
import java.util.List;

import static lucbui.rayscode.token.Rayscode.*;

/**
 * A testing ground for current features.
 */
public class Main {

    public static void main(String... args) {
        //Reverses the input string.
        //evaluate(INPUT, OUTPUT);
        //1 at the top of the stack if the user inputs something, 0 if the user inputs nothing.
        evaluate(INPUT, SIZE, IF, THREE, ELSE, TWO, ENDIF, THREE, SUBTRACT);
    }

    private static void evaluate(RayscodeFunction... funcs){
        RayscodeEvaluator eval = new RayscodeEvaluator();
        List<RayscodeFunction> code = Arrays.asList(funcs);
        System.out.println(code.toString() + "=" + eval.evaluate(code) + ":" + eval.getVars());
    }
}
