package lucbui.rayscode.token;

import lucbui.rayscode.evaluator.EvaluatorIterator;
import lucbui.rayscode.evaluator.RayscodeEvaluator;

import java.math.BigInteger;
import java.util.Deque;
import java.util.List;

/**
 * A custom function which executes a sequence of base function logic.
 */
public class RayscodeCustomFunction implements RayscodeFunction{

    private int numberOfParameters;
    private List<RayscodeFunctionMetadata> functionDefinition;

    /**
     * Creates a custom function.
     * @param numberOfParameters The number of input parameters.
     * @param functionDefinition The list of functions to execute.
     */
    public RayscodeCustomFunction(int numberOfParameters, List<RayscodeFunctionMetadata> functionDefinition) {
        this.numberOfParameters = numberOfParameters;
        this.functionDefinition = functionDefinition;
    }

    @Override
    public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator) {
        RayscodeEvaluator functionEvaluator = evaluator.createNewEvaluator(this.numberOfParameters, functionDefinition);
        Deque<BigInteger> returnStack = functionEvaluator.evaluate();
        while(!returnStack.isEmpty()){
            stack.push(returnStack.pop());
        }
    }

    @Override
    public boolean requiresId() {
        return true;
    }

    @Override
    public String getHelp() {
        return "A custom method.";
    }

    @Override
    public String toString(){
        return numberOfParameters + "=>" + functionDefinition.toString();
    }


}
