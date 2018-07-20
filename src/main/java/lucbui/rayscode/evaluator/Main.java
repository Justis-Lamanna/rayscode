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
        RayscodeEvaluator eval = new RayscodeEvaluator();
        List<RayscodeFunction> code = Arrays.asList(INPUT, SIZE, ROLL); //length of input + input
        System.out.println(code.toString() + "=" + eval.evaluate(code));
    }
}
