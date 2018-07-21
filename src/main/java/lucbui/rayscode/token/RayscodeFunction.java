package lucbui.rayscode.token;

import lucbui.rayscode.evaluator.EvaluatorIterator;
import lucbui.rayscode.evaluator.RayscodeEvaluator;

import java.math.BigInteger;
import java.util.Deque;

/**
 * A subinterface to mark certain functions explicitly as RayscodeFunctions
 */
public interface RayscodeFunction {

    /**
     * Execute the functional logic.
     * @param stack The stack to manipulate
     * @param iterator The code evaluation iterator, which can modify the code flow.
     * @param evaluator The evaluator calling this function
     * @return The return values
     */
    void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator);


}
