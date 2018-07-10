package lucbui.rayscode.token;

import lucbui.rayscode.evaluator.Common;

import java.math.BigInteger;

/**
 * A literal in raysCode. These always have 0 arguments, and 1 returned value, which does
 * not change.
 *
 * Since rays2 and rays3 are the only number-based emotes, they are the only literal numbers that can
 * be used. Any other number required must be created using a combination of these two numbers.
 */
public enum RayscodeLiteral implements RayscodeFunction {
    /**
     * Literal 2 in raysCode
     */
    TWO(2),
    /**
     * Literal 3 in raysCode
     */
    THREE(3);

    /**
     * The literal to return.
     * We store it as a BigInteger array so conversion does not need to occur each time.
     */
    private BigInteger[] literal;

    RayscodeLiteral(int literal) {
        this.literal = Common.varargsToArray(BigInteger.valueOf(literal));
    }

    /**
     * Being a literal, no arguments are necessary
     * @return 0
     */
    @Override
    public int getNumberOfArguments() {
        return 0;
    }

    /**
     * Being a literal, only one return value is used
     * @return 1
     */
    @Override
    public int getNumberOfReturns() {
        return 1;
    }

    /**
     *
     * @param arguments The arguments pulled in from the stack
     * @return The literal value.
     */
    @Override
    public BigInteger[] execute(BigInteger[] arguments) {
        return literal;
    }
}
