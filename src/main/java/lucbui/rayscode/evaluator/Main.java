package lucbui.rayscode.evaluator;

import lucbui.rayscode.token.RayscodeFunctionMetadata;

import java.util.Arrays;
import java.util.List;

import static lucbui.rayscode.token.Rayscode.*;
import static lucbui.rayscode.token.RayscodeFunctionMetadata.make;

/**
 * A testing ground for current features.
 */
public class Main {

    public static void main(String... args) {
        //1 at the top of the stack if the user inputs something, 0 if the user inputs nothing.
        /*
        evaluate(
                make(INPUT),
                make(SIZE),
                make("0", IF), //The IDs for this IF-ELSE-ENDIF blocks must match.
                    make(THREE),
                make("0", ELSE),
                    make(TWO),
                make("0", ENDIF),
                make(THREE),
                make(SUBTRACT));*/
        //Print out the first character of an inputted string.
        evaluate(
                make(INPUT),
                make("A", STARTLOOP),
                make(SIZE),
                make(TWO),
                make(THREE),
                make(SUBTRACT),
                make(SWAP),
                make(SUBTRACT),
                make("0", IF), // size - (3 - 2) <= 0 -> size <= 1
                make(POP),
                make("0", ELSE),
                make(POP),
                make(POP),
                make("A", ENDLOOP),
                make("0", ENDIF),
                make(OUTPUT)
        );
    }

    private static void evaluate(RayscodeFunctionMetadata... funcs){
        RayscodeEvaluator eval = new RayscodeEvaluator();
        List<RayscodeFunctionMetadata> code = Arrays.asList(funcs);
        System.out.println(code.toString() + "=" + eval.evaluate(code) + ":" + eval.getVars());
    }
}
