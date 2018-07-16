package lucbui.rayscode.token;

import lucbui.rayscode.evaluator.Common;

import java.math.BigInteger;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

public class Rayscode {
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
        private BigInteger[] literal;

        RayscodeLiteral(Integer literal) {
            this.literal = Common.varargsToArray( Objects.isNull(literal) ? null : BigInteger.valueOf(literal));
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
        public BigInteger[] execute(Deque<BigInteger> stack, BigInteger[] arguments) {
            return literal;
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
        public BigInteger[] execute(Deque<BigInteger> stack, BigInteger[] arguments) {
            Common.requireNoInfiniteArgument(arguments);
            return Common.varargsToArray(operator.apply(arguments[0], arguments[1]));
        }
    }

    private enum RayscodeUnaryOp implements RayscodeFunction{

        INPUT(){
            @Override
            public BigInteger[] execute(Deque<BigInteger> stack, BigInteger[] arguments) {
                BigInteger numberOfCharactersToRead = arguments[0];

                Scanner inputScanner = new Scanner(System.in);
                String s = inputScanner.nextLine();
                inputScanner.close();

                return Stream.iterate(0, i -> i + 1)
                        .limit(numberOfCharactersToRead == null ? s.length() : numberOfCharactersToRead.longValueExact())
                        .map(i -> BigInteger.valueOf(i < s.length() ? s.charAt(i) : 0))
                        .toArray(BigInteger[]::new);
            }
        },
        OUTPUT(){
            @Override
            public BigInteger[] execute(Deque<BigInteger> stack, BigInteger[] arguments) {
                BigInteger numberOfCharactersToRead = arguments[0];
                List<BigInteger> stackList = new ArrayList<>(stack);

                BigInteger[] returns = Stream.iterate(0, i -> i + 1)
                        .limit(numberOfCharactersToRead == null ? stack.size() : numberOfCharactersToRead.longValueExact())
                        .map(i -> {
                            System.out.print((char)stackList.get(i).intValueExact());
                            return stackList.get(i);
                        })
                        .toArray(BigInteger[]::new);
                System.out.println();
                return returns;
            }
        };

        /**
         * Being a BinOp, two arguments are always needed.
         * @return 2
         */
        @Override
        public int getNumberOfArguments() {
            return 1;
        }

        /**
         * Being a BinOp, one return value is always returned
         * @return 1
         */
        @Override
        public int getNumberOfReturns() {
            return -1;
        }
    }
}
