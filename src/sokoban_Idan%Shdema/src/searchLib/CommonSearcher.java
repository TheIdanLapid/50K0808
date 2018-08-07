package searchLib;

import java.util.LinkedList;
/**
 * General search algorithm 
 * @author שדמה
 *
 */
public abstract class CommonSearcher<T> implements Searcher<T> {

	protected Solution backTrace(State<T> goalState){
		LinkedList<SearchLibAction> actions = new LinkedList<SearchLibAction>();
	
		State<T> currState = goalState;
		while (currState.getCameFrom()!=null && currState.getAction()!=null){
			if (currState.getAction() instanceof SokPath) //if sokoban moves before the push
			{
				SokPath sp = (SokPath) currState.getAction();
				for (int i=0; i<sp.getActions().size(); i++)
				{
					actions.addFirst(sp.getActions().get(i)); //add all actions
				}
			}
			else
			{
				actions.addFirst(currState.getAction()); //add push action
			}
			currState = currState.getCameFrom();
		}
		Solution sol = new Solution();
		sol.setActions(actions);
		return sol;
	}
	
}
