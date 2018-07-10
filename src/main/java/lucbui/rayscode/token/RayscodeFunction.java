package lucbui.rayscode.token;

import java.math.BigInteger;

/**
 * A subinterface to mark certain functions explicitly as RayscodeFunctions
 */
public interface RayscodeFunction {
    /**
     * Get the number of arguments required by the function
     * @return the number of arguments required by the function
     */
    int getNumberOfArguments();

    /**
     * Get the number of values returned by the function
     * @return the number of values returned by the function
     */
    int getNumberOfReturns();

    /**
     * Execute the functional logic.
     * @param arguments The arguments pulled in from the stack
     * @return The return values
     */
    BigInteger[] execute(BigInteger[] arguments);
}
