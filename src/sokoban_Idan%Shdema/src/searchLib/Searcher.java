package searchLib;
/**
 * Interface for searchers
 * @author שדמה
 *
 */
public interface Searcher<T> {
	Solution search(Searchable<T> s);	
}
