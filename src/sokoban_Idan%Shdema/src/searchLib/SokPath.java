package searchLib;

import java.util.LinkedList;
/**
 * Holds a linked list of actions. Used as a path from sokoban to box.
 * @author שדמה
 *
 */
public class SokPath extends SearchLibAction { //name - push action actions - move actions

	LinkedList<SearchLibAction> actions;

	public SokPath(String name) {
		super(name);
		actions = new LinkedList<>();
	}
	
	public SokPath() {
		actions = new LinkedList<>();
	}

	@Override
		public int hashCode() {
			int sum = 0;
			for (int i = 0; i < actions.size(); i++) {
				sum+=actions.get(i).hashCode();
			}
			return sum;
		}
	
	@Override
		public boolean equals(Object obj) {
//			if (!(obj instanceof SokPath))
//				return false;
			SokPath path = (SokPath) obj;
			if (path.getActions()==null)
				return false;
			if (path.getActions().size()==actions.size())
			{
				for (int i = 0; i < actions.size(); i++) {
					if (!path.getActions().get(i).equals(actions.get(i)))
						return false;
				}
			}
			return true; 
		}
	
	public LinkedList<SearchLibAction> getActions() {
		if (actions.size()==0)
			return null;
		return actions;
	}

	public void setActions(LinkedList<SearchLibAction> actions) {
		this.actions = actions;
	}
	
	public void addAction(SearchLibAction action) {
		if (actions==null)
			actions = new LinkedList<>();
		actions.addFirst(action);
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

	public void addEverything(LinkedList<SearchLibAction> actions) {
		if (actions!=null && actions.size()>0)
		{
			for (int i = 0; i<actions.size(); i++) {
				this.actions.add(actions.get(i));
			}
		}
	}

	public void reverse() {
		LinkedList<SearchLibAction> reversed = new LinkedList<>();
		while (!actions.isEmpty())
			reversed.add(this.actions.removeLast());
		this.actions = reversed;		
	}
	

}
