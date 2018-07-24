package lucbui.rayscode.token;

import lucbui.rayscode.evaluator.EvaluatorIterator;
import lucbui.rayscode.evaluator.RayscodeEvaluator;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class RayscodeCustomFunction implements RayscodeFunction{

    private int numberOfParameters;
    private List<RayscodeFunctionMetadata> functionDefinition;

    public RayscodeCustomFunction(int numberOfParameters, List<RayscodeFunctionMetadata> functionDefinition) {
        this.numberOfParameters = numberOfParameters;
        this.functionDefinition = functionDefinition;
    }

    public RayscodeCustomFunction(int numberOfParameters, RayscodeFunctionMetadata... functionDefinition) {
        this.numberOfParameters = numberOfParameters;
        this.functionDefinition = Arrays.asList(functionDefinition);
    }

    @Override
    public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator) {
        RayscodeEvaluator functionEvaluator = evaluator.createNewEvaluator(this.numberOfParameters);
        Deque<BigInteger> returnStack = functionEvaluator.evaluate(functionDefinition);
        while(!returnStack.isEmpty()){
            stack.push(returnStack.pop());
        }
    }

    @Override
    public boolean requiresId() {
        return true;
    }

    @Override
    public String toString(){
        return numberOfParameters + "=>" + functionDefinition.toString();
    }
}
