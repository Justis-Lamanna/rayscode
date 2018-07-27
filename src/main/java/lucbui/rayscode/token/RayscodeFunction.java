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
    void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator);

    /**
     * Returns if an ID is required for this particular function. Default to false.
     * @return True if a function needs an ID.
     */
    default boolean requiresId(){
        return false;
    }

    /**
     * Get the help text for this function.
     * @return Help text.
     */
    String getHelp();

    /**
     * Checks if there's enough arguments for a function.
     * @param stack The stack to check.
     * @param numberOfArgs The number of arguments needed.
     */
    static void requiresArguments(Deque<?> stack, int numberOfArgs, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator eval){
        if(stack.size() < numberOfArgs){
            throw new IllegalArgumentException(
                    iterator.get().getFunction() + " at location " + (iterator.getCurrentIndex() + 1) +
                            " requires " + numberOfArgs + " arguments, but stack is of size " + stack.size());
        }
    }
}
