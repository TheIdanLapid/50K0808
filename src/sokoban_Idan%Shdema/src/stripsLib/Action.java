package stripsLib;

import java.util.LinkedList;

import searchLib.SearchLibAction;
/**
 * The action to do, has preconditions that has to be met, and effects to update the KB with
 * searchActions are the actions to print to the file
 * @author שדמה
 *
 */
public class Action extends Predicate{

	protected Clause preconditions,effects;
	private LinkedList<SearchLibAction> searchActions;
	
	public LinkedList<SearchLibAction> getSearchActions() {
		return searchActions;
	}

	public void setSearchActions(LinkedList<SearchLibAction> searchActions) {
		this.searchActions = searchActions;
	}

	public Action(String type, String id, String value) {
		super(type, id, value);
		// TODO Auto-generated constructor stub
	}

	public Clause getPreconditions() {
		return preconditions;
	}

	public Clause getEffects() {
		return effects;
	}

	public void setPreconditions(Clause preconditions) {
		this.preconditions = preconditions;
	}

	public void setEffects(Clause effects) {
		this.effects = effects;
	}
	
	
}
