package lucbui.rayscode.evaluator;

import lucbui.rayscode.evaluator.Common;
import lucbui.rayscode.token.RayscodeFunction;

import java.math.BigInteger;
import java.util.Deque;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * An Evaluator that explicitly evaluates raysCode
 */
public class RayscodeEvaluator {
    /**
     * Evaluate a line of code.
     * @param code The code to evaluate
     * @return The final value in the stack.
     */
    public BigInteger[] evaluate(Iterable<RayscodeFunction> code){
        Deque<BigInteger> stack = new LinkedList<>();
        for(RayscodeFunction function : code){
            BigInteger[] parameters = getArgumentsFromStack(stack, function.getNumberOfArguments());
            BigInteger[] returns = function.execute(parameters);
            addReturnsToStack(stack, function.getNumberOfReturns(), returns);
        }
        return getArgumentsFromStack(stack, stack.size());
    }

    /**
     * Pop some number of values from the stack
     * @param stack The stack to pop from.
     * @param numberOfMembers The number of members to pop
     * @return The result of the numberOfMembers pop
     */
    private BigInteger[] getArgumentsFromStack(Deque<BigInteger> stack, int numberOfMembers){
        return Stream.iterate(0, i -> i + 1)
                .limit(numberOfMembers)
                .map(i -> stack.removeFirst())
                .toArray(size -> Common.createArray(BigInteger.class, size));
    }

    /**
     * Push some number of values onto the stack
     * @param stack The stack to push onto
     * @param numberOfMembers The number of members to push
     * @param members The members to push
     */
    private void addReturnsToStack(Deque<BigInteger> stack, int numberOfMembers, BigInteger[] members){
        Stream.iterate(0, i -> i + 1)
                .limit(numberOfMembers)
                .forEach(i ->  stack.addFirst(Common.getOrDefault(members, i)));
    }
}
