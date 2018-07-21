package lucbui.rayscode.token;

import lucbui.rayscode.evaluator.EvaluatorIterator;
import lucbui.rayscode.evaluator.RayscodeEvaluator;

import java.math.BigInteger;
import java.util.Deque;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * An enum holding the RayscodeFunction operators.
 *
 * Implemented as an enum so there's only ever one instance of each.
 */
public enum Rayscode implements RayscodeFunction {
    /**
     * A literal two, which places a 2 on the stack.
     */
    TWO() {
        //Cache result so we don't invoke valueOf each time.
        BigInteger two = BigInteger.valueOf(2);

        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
            stack.push(two);
        }
    },
    /**
     * A literal three, which places a 3 on the stack.
     */
    THREE() {
        //Cache result so we don't invoke valueOf each time.
        BigInteger three = BigInteger.valueOf(3);

        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
            stack.push(three);
        }
    },
    /**
     * Get the current stack size.
     */
    SIZE() {
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
            stack.push(BigInteger.valueOf(stack.size()));
        }
    },
    /**
     * An add operator, which pops twice and pushes their sum
     */
    ADD() {
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
            stack.push(stack.pop().add(stack.pop()));
        }
    },
    /**
     * A subtraction operator, which pops twice and pushes their difference
     */
    SUBTRACT() {
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
            stack.push(stack.pop().subtract(stack.pop()));
        }
    },
    /**
     * A multiplication operator, which pops twice and pushes their product
     */
    MULTIPLY() {
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
            stack.push(stack.pop().multiply(stack.pop()));
        }
    },
    /**
     * A division operator, which pops twice and pushes their quotient
     */
    DIVIDE() {
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
            stack.push(stack.pop().divide(stack.pop()));
        }
    },
    /**
     * An input operator, which pops once (the number of chars to read), and pushes that number of chars from STD IN.
     */
    INPUT() {
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
            System.out.print(">> ");

            Scanner inputScanner = new Scanner(System.in);
            String s = inputScanner.nextLine();
            inputScanner.close();

            iterate(s.length())
                    .map(i -> BigInteger.valueOf(i < s.length() ? s.charAt(i) : 0))
                    .forEach(stack::push);
        }
    },
    /**
     * An output operator, which pops once (the number of chars to read), and prints that number of chars to STD OUT.
     * The popped characters are consumed.
     */
    OUTPUT() {
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
            iterate(stack.size())
                    .forEach(i -> System.out.print((char) stack.pop().intValueExact()));
            System.out.println();

        }
    },
    /**
     * An operator which swaps a number of elements on the stack.
     */
    SWAP() {
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
            BigInteger first = stack.pop();
            BigInteger second = stack.pop();
            stack.push(first);
            stack.push(second);
        }
    },
    /**
     * A pop operator, which removes the first element from the stack.
     */
    POP(){
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
            stack.pop();
        }
    },
    DUPLICATE(){
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
            BigInteger toDuplicate = stack.pop();
            stack.push(toDuplicate);
            stack.push(toDuplicate);
        }
    },
    ROLL(){
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
            stack.addLast(stack.pop());
        }
    },
    ASSIGNMENT(){
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
            RayscodeVar var = (RayscodeVar) iterator.getPrevious();
            BigInteger value = stack.peek();
            evaluator.setVariableValue(var.getVariableName(), value);
        }
    };

    private static Stream<Integer> iterate(int stackSize){
        return Stream.iterate(0, i -> i + 1)
                .limit(stackSize);
    }
}
