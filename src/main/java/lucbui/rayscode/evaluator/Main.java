package lucbui.rayscode.evaluator;

import static lucbui.rayscode.token.RayscodeBinOp.*;

import lucbui.rayscode.token.RayscodeFunction;
import static lucbui.rayscode.token.RayscodeLiteral.*;

import java.util.Arrays;
import java.util.List;

/**
 * A testing ground for current features.
 */
public class Main {

    public static void main(String... args) {
        RayscodeEvaluator eval = new RayscodeEvaluator();
        List<RayscodeFunction> code = Arrays.asList(TWO, TWO, THREE, ADD, MULTIPLY);
        System.out.println(code.toString() + "=" + Arrays.toString(eval.evaluate(code)));  //prints: [TWO, TWO, THREE, ADD, MULTIPLY]=[10]
    }
}
