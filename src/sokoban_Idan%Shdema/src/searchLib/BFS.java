package searchLib;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;


public class BFS<T> extends CommonSearcher<T> {
/**
 * Algorithm for searching the (best first) path between two positions
 */
	
	boolean found;
	
	@Override
	public Solution search(Searchable<T> s) {
		found = false;
		Queue<State<T>> openList = new PriorityQueue<>(100, new Comparator<State<T>>() {

			@Override
			public int compare(State<T> s1, State<T> s2) {
				return (int) (s1.getCost()-s2.getCost());
			}
		});
		HashSet<State<T>> closeSet = new HashSet<>();
		s.getInitialState().setCost(0);
		openList.add(s.getInitialState());
		while (openList.size() > 0) {
			State<T> n = openList.poll();
			closeSet.add(n);
			if (n.equals(s.getGoalState())) {
				return backTrace(n);
			}

			HashMap<SearchLibAction, State<T>> possibleStatesMap = s.getAllPossibleMoves(n);
			
			if(possibleStatesMap!=null) {
				for (SearchLibAction a : possibleStatesMap.keySet()) {
					State<T> state = possibleStatesMap.get(a);
					if (!openList.contains(state) && !closeSet.contains(state)) {
						state.setCameFrom(n);
						state.setCost(n.getCost()+1);

						for (State<T> temp : openList)
						{
							if (temp.equals(state))
							{
								found = true;
							}
						}
						
						for (State<T> temp : closeSet)
						{
							if (temp.equals(state))
							{
								found = true;
							}
						}
						
						if (found==false)
							openList.add(state);
						found = false;
						
					} else {
						if (openList.contains(state)) {
							if (state.getCost() > n.getCost()+1) {
								state.setCost(n.getCost()+1);
								state.setCameFrom(n);
								openList.remove(state);
								openList.add(state);
							
							}
							for (State<T> temp : openList)
							{
								if (temp.equals(state) && state.getCost()<temp.getCost())
								{
									openList.remove(temp);
								}
							}	
						}
					}
				} //close for
			}
		}
		return null; //didn't find a path
	}
}