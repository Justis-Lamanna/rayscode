package lucbui.rayscode.token;

import lucbui.rayscode.evaluator.RayscodeEvaluator;

import java.math.BigInteger;
import java.util.Deque;
import java.util.ListIterator;

public class RayscodeVar implements RayscodeFunction {

    private String varName;

    public RayscodeVar(String varName){
        this.varName = varName;
    }

    public static RayscodeVar of(String varName){
        return new RayscodeVar(varName);
    }

    @Override
    public void execute(Deque<BigInteger> stack, ListIterator<RayscodeFunction> iterator, RayscodeEvaluator evaluator) {
        if(evaluator.hasVariableValue(varName)) {
            stack.push(evaluator.getVariableValue(varName));
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
