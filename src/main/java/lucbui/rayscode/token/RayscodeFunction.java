package lucbui.rayscode.token;

import java.math.BigInteger;
import java.util.Deque;

/**
 * A subinterface to mark certain functions explicitly as RayscodeFunctions
 */
public interface RayscodeFunction {

    /**
     * Execute the functional logic.
     * @param stack The stack to manipulate
     * @return The return values
     */
    void execute(Deque<BigInteger> stack);


}
