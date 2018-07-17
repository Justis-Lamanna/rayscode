package lucbui.rayscode.evaluator;

import java.util.Arrays;
import java.util.Objects;

/**
 * Common helper functions
 */
public class Common {

    private Common(){
        //Nothing
    }

    /**
     * Check if any values in the supplied in the array are null (Infinity)
     * @param arguments The argument to check
     * @param <T> The type of array
     * @return True if an infinite argument is in the list.
     */
    public static <T> boolean containsInfinityArgument(T[] arguments){
       return Arrays.stream(arguments).anyMatch(Objects::isNull);
    }

    /**
     * Verify all arguments are non-infinite.
     * @param arguments The arguments to check.
     * @param <T> The type of array.
     * @throws IllegalArgumentException Infinity is present in the arguments.
     */
    public static <T> void requireNoInfiniteArgument(T[] arguments){
        if(containsInfinityArgument(arguments)){
            throw new IllegalArgumentException(Arrays.toString(arguments));
        }
    }
}
