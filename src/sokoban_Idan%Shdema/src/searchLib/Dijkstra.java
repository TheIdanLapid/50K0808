package searchLib;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
/**
 * Dijkstra search algorithm - searches the least cost path between two positions 
 * @author שדמה
 *
 */
public class Dijkstra<T> extends CommonSearcher<T> {

	LinkedList<State<T>> visited;
	Queue<State<T>> candidates;
	@Override
	public Solution search(Searchable<T> s) {
		candidates = new PriorityQueue<>(100, new Comparator<State<T>>() {
			
			@Override
			public int compare(State<T> s1, State<T> s2) {
				return (int) (s1.getCost()-s2.getCost());
			}
		});
		visited = new LinkedList<>();
		
		s.getInitialState().setCost(0);
		State<T> current = s.getInitialState();
		visited.add(current);
		setNeighbors(s, current, visited, candidates);
		while(!candidates.isEmpty())
		{
			if (current.equals(s.getGoalState()))
				return backTrace(current);
			else
			{
				current=candidates.poll();
				visited.add(current);
				setNeighbors(s, current, visited, candidates);
			}
		}
		return null;
	}

	void setNeighbors(Searchable<T> s,State<T> curr, LinkedList<State<T>> visited, Queue<State<T>> candidates){
		HashMap<SearchLibAction, State<T>> neighbors = s.getAllPossibleMoves(curr);
		if(neighbors!=null) {
			for (SearchLibAction a : neighbors.keySet()) {
				State<T> neighbor = neighbors.get(a);
				if (neighbor.getCost() > curr.getCost()+1) {
					neighbor.setCost(curr.getCost()+1);
					neighbor.setCameFrom(curr);
				}
				boolean flag = false;
				for(State<T> st: visited)
				{
					if(st.equals(neighbor) && !flag)
					{
						flag=true;
					}
				}
				if(!flag)
					candidates.add(neighbor);
			}
		}
		this.candidates=candidates;
	}
}