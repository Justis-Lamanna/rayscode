package lucbui.rayscode.evaluator;

import lucbui.rayscode.token.RayscodeFunctionMetadata;

import java.math.BigInteger;
import java.util.*;

/**
 * An Evaluator that explicitly evaluates raysCode
 */
public class RayscodeEvaluator {

    private Map<String, BigInteger> variables = new HashMap<>();

    /**
     * Evaluate a line of code.
     * @param code The code to evaluate
     * @return The final value in the stack.
     */
    public Deque<BigInteger> evaluate(List<RayscodeFunctionMetadata> code){
        Deque<BigInteger> stack = new LinkedList<>();
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
