package lucbui.rayscode.evaluator;

import lucbui.rayscode.token.RayscodeFunctionMetadata;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Stream;

/**
 * An Evaluator that explicitly evaluates raysCode
 */
public class RayscodeEvaluator {

    private Map<String, BigInteger> variables;
    private Deque<BigInteger> stack;

    public RayscodeEvaluator() {
        variables = new HashMap<>();
        stack = new LinkedList<>();
    }

    /**
     * Create a new evaluator based on this one.
     * Used in method running, this "splits" off the main evaluator, preserving the variables and shortening
     * the stack.
     * @param numberOfParameters The number of parameters to preserve.
     * @return An evaluator based off this one.
     */
    public RayscodeEvaluator createNewEvaluator(int numberOfParameters){
        RayscodeEvaluator newEvaluator = new RayscodeEvaluator();
        Stream.iterate(0, i -> i + 1).limit(numberOfParameters).forEach(i -> newEvaluator.stack.push(stack.pop()));
        newEvaluator.variables = new HashMap<>(variables);
        return newEvaluator;
    }

    /**
     * Evaluate a line of code.
     * @param code The code to evaluate
     * @return The final value in the stack.
     */
    public Deque<BigInteger> evaluate(List<RayscodeFunctionMetadata> code){
        EvaluatorIterator<RayscodeFunctionMetadata> iterator = new EvaluatorIterator<>(code);
        while(!iterator.isComplete()){
            RayscodeFunctionMetadata metadata = iterator.get();
            metadata.getFunction().execute(stack, iterator, this);
            iterator.advance();
        }
        return stack;
    }

    public boolean hasVariableValue(String name){
        return variables.containsKey(name);
    }

    public BigInteger getVariableValue(String name){
        if(variables.containsKey(name)){
            return variables.get(name);
        } else {
            throw new IllegalArgumentException("No variable called " + name);
        }
    }

    public void setVariableValue(String variableName, BigInteger value) {
        variables.put(variableName, value);
    }

    public String getVars(){
        return variables.toString();
    }
}
