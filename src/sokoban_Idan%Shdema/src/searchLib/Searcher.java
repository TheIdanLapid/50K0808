package searchLib;
/**
 * Interface for searchers
 * @author ����
 *
 */
public interface Searcher<T> {
	Solution search(Searchable<T> s);	
}
