package lucbui.rayscode.evaluator;

import java.util.List;
import java.util.Objects;

/**
 * A special iterator of my own design to help with code flow modification.
 *
 * A standard iterator only allows for next() and previous() methods, which are fine for sequential
 * traversal of a list, but not so much for traversing a potentially complex evaluation tree. Code may depend on
 * past code, or future code.
 *
 * This iterator maintains a pointer to the provided list. You can retrieve the values relative to that pointer, and
 * advance the pointer forward.
 */
public class EvaluatorIterator<T> {

    private final List<T> list;
    private int currentIndex;

    /**
     * Create an EvaluatorIterator from a list.
     * @param list The list to wrap.
     */
    public EvaluatorIterator(List<T> list){
        this.list = list;
        currentIndex = 0;
    }

    /**
     * Get the value at the pointer.
     * @return
     */
    public T get(){
        return list.get(currentIndex);
    }

    /**
     * Get the value immediately before the pointer.
     * @return
     */
    public T getPrevious(){
        return getRelative(-1);
    }

    /**
     * Get the value relative to the current pointer.
     * @param idx The number of spaces ahead to retrieve. Negative goes backwards.
     * @return
     */
    public T getRelative(int idx){
        return list.get(currentIndex + idx);
    }

    /**
     * Get the value immediately after the pointer.
     * @return
     */
    public T getNext(){
        return getRelative(1);
    }

    /**
     * Advance the pointer some amount
     * @param amt The number of indexes to jump. Negative goes backwards.
     */
    public void advance(int amt){
        currentIndex += amt;
    }

    /**
     * Advance the pointer one index.
     */
    public void advance(){
        currentIndex ++;
    }

    /**
     * Advance to a specific value in the list.
     * @param values The value(s) to advance to
     * @return True if the value was found, false if not.
     */
    public boolean advanceTo(T... values) {
        for(int idx = currentIndex; idx < list.size(); idx++){
            for(T value : values) {
                if (Objects.equals(value, list.get(idx))) {
                    currentIndex = idx;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if we are at the end of the list.
     * @return True if the list is fully traversed.
     */
    public boolean isComplete(){
        return currentIndex >= list.size();
    }
}
