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
     * top-most = top-most + second-top-most
     */
    ADD() {
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
            stack.push(stack.pop().add(stack.pop()));
        }
    },
    /**
     * A subtraction operator, which pops twice and pushes their difference
     * top-most = top-most - second-top-most
     */
    SUBTRACT() {
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
            stack.push(stack.pop().subtract(stack.pop()));
        }
    },
    /**
     * A multiplication operator, which pops twice and pushes their product
     * top-most = top-most * second-top-most
     */
    MULTIPLY() {
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
            stack.push(stack.pop().multiply(stack.pop()));
        }
    },
    /**
     * A division operator, which pops twice and pushes their quotient
     * top-most = top-most / second-top-most
     */
    DIVIDE() {
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
            stack.push(stack.pop().divide(stack.pop()));
        }
    },
    /**
     * An input operator, pushes all chars to STD:IN, in order of entry.
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
     * An output operator, which prints out the top-most value in the stack as an ASCII character.
     * The popped character is consumed.
     */
    OUTPUT() {
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
            System.out.print((char) stack.pop().intValueExact());
            System.out.println();

        }
    },
    /**
     * An operator which swaps the top-most and the second-top-most element in the stack.
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
     * A pop operator, which removes the top-most element from the stack.
     */
    POP(){
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
            stack.pop();
        }
    },
    /**
     * Duplicates the top-most element in the stack.
     */
    DUPLICATE(){
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
            BigInteger toDuplicate = stack.pop();
            stack.push(toDuplicate);
            stack.push(toDuplicate);
        }
    },
    /**
     * Moves the top-most element to the bottom of the stack.
     */
    ROLL(){
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
            stack.addLast(stack.pop());
        }
    },
    /**
     * Binds a value to a variable.
     * The previous rayscode function MUST be a RayscodeVar. The variable name is bound to the top-most element in the stack.
     */
    ASSIGNMENT(){
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
            RayscodeVar var = (RayscodeVar) iterator.getPrevious();
            BigInteger value = stack.peek();
            evaluator.setVariableValue(var.getVariableName(), value);
        }
    },
    /**
     * Begins an IF block.
     * If the top-most value in the stack is nonpositive, or the stack is empty, the block between this IF and an ELSE
     * or ENDIF clause is executed. Otherwise, execution jumps to the corresponding ELSE clause, or an ENDIF clause.
     *
     * IF consumes the top-most value in the stack.
     */
    IF(){
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
            if(stack.isEmpty() || stack.pop().compareTo(BigInteger.ZERO) <= 0){
                //First Expression. We're already there so don't do anything.
            } else {
                //Jump to the ELSE operator and begin execution from there.
                if(!iterator.advanceTo(ELSE, ENDIF)){
                    throw new IllegalArgumentException("IF not bound by an ELSE or ENDIF");
                }
            }
        }
    },
    /**
     * Begins an ELSE block.
     * If encountered, it jumps to the ENDIF block.
     */
    ELSE(){
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
            //If we hit ELSE, we were in the "IF" portion of the block, and we jump to the ENDIF.
            if(!iterator.advanceTo(ENDIF)){
                throw new IllegalArgumentException("ELSE not bound by ENDIF");
            }
        }
    },
    /**
     * Ends the IF-ELSE block.
     * Otherwise does nothing.
     */
    ENDIF(){
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
            //Do nothing. This just marks the end of the if-else block
        }
    };

    /**
     * Helper limit to create a stream for-loop.
     * @param stackSize The number of iterations to perform.
     * @return A stream which performs that number of iterations.
     */
    private static Stream<Integer> iterate(int stackSize){
        return Stream.iterate(0, i -> i + 1)
                .limit(stackSize);
    }
}
