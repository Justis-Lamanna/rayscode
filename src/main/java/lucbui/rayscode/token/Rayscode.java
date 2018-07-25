package lucbui.rayscode.token;

import lucbui.rayscode.evaluator.EvaluatorIterator;
import lucbui.rayscode.evaluator.RayscodeEvaluator;

import java.math.BigInteger;
import java.util.*;
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
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator) {
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
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator) {
            stack.push(three);
        }
    },
    /**
     * Get the current stack size.
     */
    SIZE() {
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator) {
            stack.push(BigInteger.valueOf(stack.size()));
        }
    },
    /**
     * A variable function.
     */
    VARIABLE(){
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator) {
            String varName = iterator.get().getId();
            if(evaluator.hasVariableValue(varName)) {
                stack.push(evaluator.getVariableValue(varName));
            } else if(evaluator.hasMethod(varName)){
                evaluator.getMethod(varName).getFunction().execute(stack, iterator, evaluator);
            } else {
                //If there isn't a definition for this yet, and the next instruction isn't assignment, we are in error.
                if(iterator.getNext().getFunction() != Rayscode.ASSIGNMENT){
                    throw new IllegalArgumentException(varName + " is not assigned!");
                }
            }
        }

        @Override
        public boolean requiresId(){
            return true;
        }
    },
    /**
     * An add operator, which pops twice and pushes their sum
     * top-most = top-most + second-top-most
     */
    ADD() {
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator) {
            stack.push(stack.pop().add(stack.pop()));
        }
    },
    /**
     * A subtraction operator, which pops twice and pushes their difference
     * top-most = top-most - second-top-most
     */
    SUBTRACT() {
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator) {
            stack.push(stack.pop().subtract(stack.pop()));
        }
    },
    /**
     * A multiplication operator, which pops twice and pushes their product
     * top-most = top-most * second-top-most
     */
    MULTIPLY() {
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator) {
            stack.push(stack.pop().multiply(stack.pop()));
        }
    },
    /**
     * A division operator, which pops twice and pushes their quotient
     * top-most = top-most / second-top-most
     */
    DIVIDE() {
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator) {
            stack.push(stack.pop().divide(stack.pop()));
        }
    },
    /**
     * An input operator, pushes all chars to STD:IN, in order of entry.
     */
    INPUT() {
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator) {
            if(evaluator.getInputString() == null){
                evaluator.setPaused(true);
            } else {
                String s = evaluator.getInputString();
                evaluator.setInputString(null);
                iterate(s.length())
                        .map(i -> BigInteger.valueOf(i < s.length() ? s.charAt(i) : 0))
                        .forEach(stack::push);
            }
        }
    },
    /**
     * An output operator, which prints out the top-most value in the stack as an ASCII character.
     * The popped character is consumed.
     */
    OUTPUT() {
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator) {
           evaluator.addOutputString((char) stack.pop().intValueExact());
        }
    },
    /**
     * An operator which swaps the top-most and the second-top-most element in the stack.
     */
    SWAP() {
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator) {
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
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator) {
            stack.pop();
        }
    },
    /**
     * Duplicates the top-most element in the stack.
     */
    DUPLICATE(){
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator) {
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
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator) {
            stack.addLast(stack.pop());
        }
    },
    /**
     * Binds a value to a variable.
     * The previous rayscode function MUST be a variable. The variable name is bound to the top-most element in the stack.
     */
    ASSIGNMENT(){
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator) {
            String varName = iterator.getPrevious().getId();
            BigInteger value = stack.peek();
            evaluator.setVariableValue(varName, value);
        }
    },
    /**
     * Begins an IF block.
     * If the top-most value in the stack is nonpositive, or the stack is empty, the block between this IF and an ELSE
     * or ENDIF clause is executed. Otherwise, execution jumps to the corresponding ELSE clause, or an ENDIF clause.
     *
     * IF does not consume any values.
     */
    IF(){
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator) {
            String id = iterator.get().getId();
            if(stack.isEmpty() || stack.peek().compareTo(BigInteger.ZERO) <= 0){
                //First Expression. We're already there so don't do anything.
            } else {
                //Jump to the else or end clause with equivalent ID.
                if(!iterator.advanceTo(
                        i -> Objects.equals(i.getId(), id) &&
                                (i.getFunction() == ELSE || i.getFunction() == ENDIF))){
                    throw new IllegalArgumentException("No END IF or ELSE clause for ID=" + id + " found.");
                }
            }
        }

        @Override
        public boolean requiresId(){
            return true;
        }
    },
    /**
     * Begins an ELSE block.
     * If encountered, it jumps to the ENDIF block.
     */
    ELSE(){
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator) {
            String id = iterator.get().getId();
            //Jump to the endif clause with the matching ID.
            if(!iterator.advanceTo(
                    i -> Objects.equals(i.getId(), id) &&
                            (i.getFunction() == ENDIF))){
                throw new IllegalArgumentException("No END IF clause for ID=" + id + " found.");
            }
        }

        @Override
        public boolean requiresId(){
            return true;
        }
    },
    /**
     * Ends the IF-ELSE block.
     * Otherwise does nothing.
     */
    ENDIF(){
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator) {
            //Do nothing. This just marks the end of the if-else block
        }

        @Override
        public boolean requiresId(){
            return true;
        }
    },
    /**
     * Mark the beginning of a loop.
     */
    STARTLOOP(){
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator) {
            //Do nothing. This just marks the start of the loop.
        }

        @Override
        public boolean requiresId(){
            return true;
        }
    },
    /**
     * Marks the end of a loop.
     */
    ENDLOOP(){
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator) {
            String id = iterator.get().getId();
            //Jump to the STARTLOOP clause with the matching ID.
            if(!iterator.advanceBackTo(
                    i -> Objects.equals(i.getId(), id) &&
                            (i.getFunction() == STARTLOOP))){
                throw new IllegalArgumentException("No STARTLOOP clause for ID=" + id + " found.");
            }
        }

        @Override
        public boolean requiresId(){
            return true;
        }
    },
    METHOD(){
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator) {

        }

        @Override
        public boolean requiresId() {
            return true;
        }
    },
    STARTFUNC(){
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator) {
            iterator.advance();
            String funcName = iterator.get().getId();
            if(funcName == null){
                throw new IllegalArgumentException("Illegal method overwritten!");
            }
            List<RayscodeFunctionMetadata> code = new ArrayList<>();
            int numParams = 0;
            while(iterator.getNext().getFunction() == PARAM){
                numParams++;
                iterator.advance();
            }
            iterator.advance();
            while(iterator.get().getFunction() != ENDFUNC){
                if(iterator.get().getFunction() == STARTFUNC){
                    throw new IllegalArgumentException("Function " + funcName + "missing end declaration");
                }
                if(iterator.get().getFunction() == PARAM){
                    throw new IllegalArgumentException("Unexpected parameter declaration");
                }
                code.add(iterator.get());
                iterator.advance();
                if(iterator.isComplete()){
                    throw new IllegalArgumentException("Function " + funcName + "missing end declaration");
                }
            }

            evaluator.setMethod(funcName, RayscodeFunctionMetadata.make(funcName, new RayscodeCustomFunction(numParams, code)));
        }
    },
    PARAM(){
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator) {
            //Marker class
        }
    },
    ENDFUNC(){
        @Override
        public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator) {
            //Marker class
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
