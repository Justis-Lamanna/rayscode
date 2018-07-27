package lucbui.rayscode.evaluator;

import lucbui.rayscode.token.RayscodeFunction;
import lucbui.rayscode.token.RayscodeFunctionMetadata;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * An Evaluator that explicitly evaluates raysCode
 */
public class RayscodeEvaluator {

    public static int MAX_STACK_SIZE = 2000;
    public static long MAX_TIME_OF_EVALUATION = 15 * 1000; //15 seconds, in milliseconds.

    private Map<String, BigInteger> variables;
    private Map<String, RayscodeFunctionMetadata> methods;
    private Deque<BigInteger> stack;

    private boolean paused;
    private String inputString;
    private StringBuilder outputString;

    private EvaluatorIterator<RayscodeFunctionMetadata> iterator;
    private boolean debug;
    private Consumer<String> outputMethod;

    public RayscodeEvaluator(List<RayscodeFunctionMetadata> code) {
        variables = new HashMap<>();
        stack = new LinkedList<>();
        methods = new HashMap<>();
        this.iterator = new EvaluatorIterator<>(code);
        this.outputString = new StringBuilder();
    }

    public void setDebug(boolean debug){
        this.debug = debug;
    }

    public void setOutputMethod(Consumer<String> method){
        this.outputMethod = method;
    }

    /**
     * Create a new evaluator based on this one.
     * Used in method running, this "splits" off the main evaluator, preserving the variables and shortening
     * the stack.
     * @param numberOfParameters The number of parameters to preserve.
     * @return An evaluator based off this one.
     */
    public RayscodeEvaluator createNewEvaluator(int numberOfParameters, List<RayscodeFunctionMetadata> code){
        RayscodeEvaluator newEvaluator = new RayscodeEvaluator(code);
        Stream.iterate(0, i -> i + 1).limit(numberOfParameters).forEach(i -> newEvaluator.stack.push(stack.pop()));
        newEvaluator.variables = new HashMap<>(variables);
        newEvaluator.methods = new HashMap<>(methods);
        return newEvaluator;
    }

    /**
     * Evaluate a line of code.
     * @return The final value in the stack.
     */
    public Deque<BigInteger> evaluate(){
        long currentTime = System.currentTimeMillis();
        StringBuilder debugString = new StringBuilder();
        while(!iterator.isComplete() && !isPaused()){
            RayscodeFunctionMetadata metadata = iterator.get();
            RayscodeFunction funcToExecute;
            if(methods.containsKey(metadata.getId())){
                funcToExecute = methods.get(metadata.getId()).getFunction();
            } else {
                funcToExecute = metadata.getFunction();
            }
            funcToExecute.execute(stack, iterator, this);
            if(!isPaused()) {
                iterator.advance();
            }
            //Limit the stack size so people can't effectively DOS my machine.
            if(stack.size() > MAX_STACK_SIZE){
                throw new IllegalStateException("Stack is too large, must be less than " + MAX_STACK_SIZE + " elements");
            }
            if(System.currentTimeMillis() > currentTime + MAX_TIME_OF_EVALUATION){
                throw new IllegalStateException("Code has been running for " + (MAX_TIME_OF_EVALUATION / 1000) + " seconds. Code has been terminated");
            }
            if(debug && outputMethod != null){
                debugString.append("Command: ")
                        .append(funcToExecute.toString())
                        .append(" Stack: ")
                        .append(stack.toString())
                        .append(" Output: ")
                        .append(outputString.toString())
                        .append("\n");
                if(debugString.length() > 1000){
                    outputMethod.accept(debugString.toString().trim());
                    debugString.setLength(0);
                }
            }
        }
        if(debugString.length() > 0){
            outputMethod.accept(debugString.toString().trim());
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

    public boolean hasMethod(String name){return methods.containsKey(name);}

    public RayscodeFunctionMetadata getMethod(String name){
        if(methods.containsKey(name)){
            return methods.get(name);
        } else {
            throw new IllegalArgumentException("No method called " + name);
        }
    }

    public void setVariableValue(String variableName, BigInteger value) {
        variables.put(variableName, value);
    }

    public void setMethod(String method, RayscodeFunctionMetadata function){
        methods.put(method, function);
    }

    public String getVars(){
        return variables.toString();
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public String getOutputString() {
        return outputString.toString();
    }

    public void addOutputString(char outputString) {
        this.outputString.append(outputString);
    }

    public String getInputString() {
        return inputString;
    }

    public void setInputString(String inputString) {
        this.inputString = inputString;
    }

    public int getLength() {
        return this.iterator.size();
    }
}
