package lucbui.rayscode.evaluator;

import java.util.List;
import java.util.function.Predicate;

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
     * Checks if the user can get the relative value.
     * @param idx The relative distance away from the current index to search.
     * @return True if that relative distance is legal.
     */
    public boolean canGetRelative(int idx){
        return currentIndex + idx >= 0 && currentIndex + idx < list.size();
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
     * @param predicate A predicate which identifies the value to find.
     * @return True if the value was found, false if not.
     */
    public boolean advanceTo(Predicate<T> predicate) {
        for(int idx = currentIndex + 1; idx < list.size(); idx++){
             if (predicate.test(list.get(idx))) {
                currentIndex = idx;
                return true;
            }
        }
        return false;
    }

    /**
     * Advance backwards to a specific value in the list.
     * @param predicate A predicate which identifies the value to find.
     * @return True if the value was found, false if not.
     */
    public boolean advanceBackTo(Predicate<T> predicate){
        for(int idx = currentIndex - 1; idx >= 0; idx--){
            if (predicate.test(list.get(idx))) {
                currentIndex = idx;
                return true;
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

    public int getCurrentIndex(){
        return currentIndex;
    }
}
