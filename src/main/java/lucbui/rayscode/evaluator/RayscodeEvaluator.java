package lucbui.rayscode.evaluator;

import lucbui.rayscode.token.RayscodeFunction;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;

/**
 * An Evaluator that explicitly evaluates raysCode
 */
public class RayscodeEvaluator {
    /**
     * Evaluate a line of code.
     * @param code The code to evaluate
     * @return The final value in the stack.
     */
    public Deque<BigInteger> evaluate(Collection<RayscodeFunction> code){
        Deque<BigInteger> stack = new LinkedList<>();
        code.forEach(func -> func.execute(stack));
        return stack;
    }
}
