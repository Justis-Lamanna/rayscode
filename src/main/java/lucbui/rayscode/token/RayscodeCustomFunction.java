package lucbui.rayscode.token;

import lucbui.rayscode.evaluator.EvaluatorIterator;
import lucbui.rayscode.evaluator.RayscodeEvaluator;

import java.math.BigInteger;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

/**
 * A custom function which executes a sequence of base function logic.
 */
public class RayscodeCustomFunction implements RayscodeFunction{

    private int numberOfParameters;
    private int numberOfReturns;
    private List<RayscodeFunctionMetadata> functionDefinition;

    /**
     * Creates a custom function.
     * @param numberOfParameters The number of input parameters.
     * @param functionDefinition The list of functions to execute.
     */
    public RayscodeCustomFunction(int numberOfParameters, List<RayscodeFunctionMetadata> functionDefinition) {
        this.numberOfParameters = numberOfParameters;
        this.functionDefinition = functionDefinition;
        this.numberOfReturns = calculateWork(null, new EvaluatorIterator<>(functionDefinition), null);
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
    public int getNumberOfArguments() {
        return numberOfParameters;
    }

    @Override
    public int getNumberOfReturns() {
        return numberOfReturns;
    }

    //Calculate the amount of work being done
    //NOTE: This doesn't take into account if-else loops, so it doesn't give a completely accurate number.
    //It will *definitely* tell you if nothing is happening though.
    private static int calculateWork(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunctionMetadata> iterator, RayscodeEvaluator evaluator){
        int workSum = 0;
        int start = 0;
        while(iterator.canGetRelative(start)){
            RayscodeFunctionMetadata functionToCheck = iterator.getRelative(start);
            //The total amount of "work" this function does. >0 means things are being made, <0 means things are being destroyed.
            int work = functionToCheck.getFunction().getNumberOfReturns() - functionToCheck.getFunction().getNumberOfArguments();
            workSum += work;
            start++;
        }
        return workSum;
    }

    @Override
    public String toString(){
        return numberOfParameters + "=>" + functionDefinition.toString();
    }


}
