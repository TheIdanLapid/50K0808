package searchLib;

import java.util.LinkedList;
/**
 * Holds a linked list of actions to solve the search problem - gets it from backtrace method.
 * @author שדמה
 *
 */
public class Solution {
	private LinkedList<SearchLibAction> actions = new LinkedList<SearchLibAction>();

	public LinkedList<SearchLibAction> getActions() {
		return actions;
	}

	public void setActions(LinkedList<SearchLibAction> actions) {
		this.actions = actions;
	}
	
	public void addActions(LinkedList<SearchLibAction> actions) {
		this.actions.addAll(actions);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (actions.size()>0)
		{
			for(SearchLibAction a : actions){
				if (a!=null)
					sb.append(a.getName()).append("\n");
			}
		}
		return sb.toString();
	}
}
