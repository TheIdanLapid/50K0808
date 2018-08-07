package searchLib;

import java.util.HashMap;
/**
 * Interface for search problems
 * @author שדמה
 *
 */
public interface Searchable<T> {
	State<T> getInitialState();
	State<T> getGoalState();
	HashMap<SearchLibAction, State<T>> getAllPossibleMoves(State<T> state);
}
