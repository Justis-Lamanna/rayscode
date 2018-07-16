package lucbui.rayscode.token;

import java.math.BigInteger;
import java.util.Deque;

/**
 * A subinterface to mark certain functions explicitly as RayscodeFunctions
 */
public interface RayscodeFunction {

    public static final BigInteger NULL = null;

    /**
     * Get the number of arguments required by the function.
     * If this value returns a negative number, the entire stack is provided.
     * @return the number of arguments required by the function
     */
    int getNumberOfArguments();

    /**
     * Get the number of values returned by the function
     * If this value returns a negative number, the entire stack is returned.
     * @return the number of values returned by the function
     */
    int getNumberOfReturns();

    /**
     * Execute the functional logic.
     * @param arguments The arguments pulled in from the stack
     * @return The return values
     */
    BigInteger[] execute(Deque<BigInteger> stack, BigInteger[] arguments);


}
