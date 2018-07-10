package lucbui.rayscode.token;

import lucbui.rayscode.evaluator.Common;

import java.math.BigInteger;
import java.util.function.BinaryOperator;

/**
 * A binary operator in raysCode. These always have two arguments, and one return value.
 *
 * The one value is created by some form of operation between two operators.
 */
public enum RayscodeBinOp implements RayscodeFunction {
    /**
     * Addition
     */
    ADD(BigInteger::add),
    /**
     * Subtraction
     */
    SUBTRACT(BigInteger::subtract),
    /**
     * Multiplication
     */
    MULTIPLY(BigInteger::multiply),
    /**
     * Integer Division
     */
    DIVIDE(BigInteger::divide);

    /**
     * The BinaryOperator being performed for each operation
     */
    BinaryOperator<BigInteger> operator;

    RayscodeBinOp(BinaryOperator<BigInteger> operator){
        this.operator = operator;
    }

    /**
     * Being a BinOp, two arguments are always needed.
     * @return 2
     */
    @Override
    public int getNumberOfArguments() {
        return 2;
    }

    /**
     * Being a BinOp, one return value is always returned
     * @return 1
     */
    @Override
    public int getNumberOfReturns() {
        return 1;
    }

    /**
     * Executes the operation on the two values.
     * @param arguments The arguments pulled in from the stack
     * @return The BinOp result.
     */
    @Override
    public BigInteger[] execute(BigInteger[] arguments) {
        return Common.varargsToArray(operator.apply(arguments[0], arguments[1]));
    }
}
