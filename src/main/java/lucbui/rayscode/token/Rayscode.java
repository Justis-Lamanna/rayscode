package lucbui.rayscode.token;

import java.math.BigInteger;
import java.util.Deque;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

public class Rayscode {

    public static final BigInteger INFINITY = null;

    private Rayscode(){
        //Static methods only please.
    }

    public static RayscodeFunction two(){
        return RayscodeLiteral.TWO;
    }

    public static RayscodeFunction three(){
        return RayscodeLiteral.THREE;
    }

    public static RayscodeFunction infinity(){
        return RayscodeLiteral.INFINITY;
    }

    public static RayscodeFunction add(){
        return RayscodeBinOp.ADD;
    }

    public static RayscodeFunction subtract(){
        return RayscodeBinOp.SUBTRACT;
    }

    public static RayscodeFunction multiply(){
        return RayscodeBinOp.MULTIPLY;
    }

    public static RayscodeFunction divide(){
        return RayscodeBinOp.DIVIDE;
    }

    public static RayscodeFunction input(){
        return RayscodeUnaryOp.INPUT;
    }

    public static RayscodeFunction output(){
        return RayscodeUnaryOp.OUTPUT;
    }


    /**
     * A literal in raysCode. These always have 0 arguments, and 1 returned value, which does
     * not change.
     *
     * Since rays2 and rays3 are the only number-based emotes, they are the only literal numbers that can
     * be used. Any other number required must be created using a combination of these two numbers.
     */
    private enum RayscodeLiteral implements RayscodeFunction {
        /**
         * Literal 2 in raysCode
         */
        TWO(2),
        /**
         * Literal 3 in raysCode
         */
        THREE(3),
        /**
         * "Literal" infinity in raysCode
         */
        INFINITY(null);

        /**
         * The literal to return.
         * We store it as a BigInteger array so conversion does not need to occur each time.
         */
        private BigInteger literal;

        RayscodeLiteral(Integer literal) {
            this.literal = Objects.isNull(literal) ? Rayscode.INFINITY : BigInteger.valueOf(literal);
        }

        /**
         *
         * @param stack The stack
         * @return The literal value.
         */
        @Override
        public void execute(Deque<BigInteger> stack) {
            stack.push(literal);
        }
    }

    /**
     * A binary operator in raysCode. These always have two arguments, and one return value.
     *
     * The one value is created by some form of operation between two operators.
     */
    private enum RayscodeBinOp implements RayscodeFunction {
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
         * Executes the operation on the two values.
         * @param stack The stack
         * @return The BinOp result.
         */
        @Override
        public void execute(Deque<BigInteger> stack) {
            operator.apply(stack.pop(), stack.pop());
        }
    }

    private enum RayscodeUnaryOp implements RayscodeFunction{

        INPUT(){
            @Override
            public void execute(Deque<BigInteger> stack) {
                BigInteger numberOfCharactersToRead = stack.pop();

                Scanner inputScanner = new Scanner(System.in);
                String s = inputScanner.nextLine();
                inputScanner.close();

                Stream.iterate(0, i -> i + 1)
                        .limit(numberOfCharactersToRead == INFINITY ? s.length() : numberOfCharactersToRead.longValueExact())
                        .map(i -> BigInteger.valueOf(i < s.length() ? s.charAt(i) : 0))
                        .forEach(stack::push);
            }
        },
        OUTPUT(){
            @Override
            public void execute(Deque<BigInteger> stack) {
                BigInteger numberOfCharactersToRead = stack.pop();

                Stream.iterate(0, i -> i + 1)
                        .limit(numberOfCharactersToRead == null ? stack.size() : numberOfCharactersToRead.longValueExact())
                        .forEach(i -> System.out.print((char)stack.pop().intValueExact()));
                System.out.println();
            }
        };

    }
}
