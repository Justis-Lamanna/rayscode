package lucbui.rayscode.token;

import lucbui.rayscode.evaluator.EvaluatorIterator;
import lucbui.rayscode.evaluator.RayscodeEvaluator;

import java.math.BigInteger;
import java.util.Deque;

public class RayscodeVar implements RayscodeFunction {

    private String varName;

    public RayscodeVar(String varName){
        this.varName = varName;
    }

    public static RayscodeVar of(String varName){
        return new RayscodeVar(varName);
    }

    @Override
    public void execute(Deque<BigInteger> stack, EvaluatorIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
        if(evaluator.hasVariableValue(varName)) {
            stack.push(evaluator.getVariableValue(varName));
        } else {
            //If there isn't a definition for this yet, and the next instruction isn't assignment, we are in error.
            if(iterator.getNext() != Rayscode.ASSIGNMENT){
                throw new IllegalArgumentException(varName + " is not assigned!");
            }
        }
    }

    public String getVariableName(){
        return varName;
    }

    @Override
    public String toString(){
        return "var=" + varName;
    }
}
