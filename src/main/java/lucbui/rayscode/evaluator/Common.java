package lucbui.rayscode.evaluator;

import java.lang.reflect.Array;
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
     * A helper function for returning values.
     * This permits the implementer to make it easier to return values from the execute
     * function, so an array does not have to be created each time.
     * @param returnValues The return value(s)
     * @return An array of return values
     */
    @SafeVarargs
    public static <T> T[] varargsToArray(T... returnValues){
        return returnValues;
    }

    /**
     * A helper function to create a dynamic typed array.
     * @param clazz The class of the array.
     * @param size The size of the array.
     * @param <T> The type of the array.
     * @return An array of the specified type.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] createArray(Class<T> clazz, int size){
        return (T[])Array.newInstance(clazz, size);
    }

    /**
     * A helper function for safe array access.
     * @param array The array to access.
     * @param idx The index to access.
     * @param <T> The type of the array.
     * @return The value at the specified index, or null if it is out of bounds.
     */
    public static <T> T getOrDefault(T[] array, int idx){
        return (idx < array.length && idx >= 0) ? array[idx] : null;
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
