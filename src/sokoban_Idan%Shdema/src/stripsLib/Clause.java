package stripsLib;

import java.util.HashSet;
import java.util.Set;
/**
 * Extend Predicate and holds a set of predicates
 * @author שדמה
 *
 */
public class Clause extends Predicate{

	HashSet<Predicate> predicates;
	
	private void updateDescription(){
		value="{";
		for(Predicate p : predicates){
			value+=p.toString()+" & ";
		}
		value+="}";
	}
	
	public Clause(Predicate...predicates) {
		super("And", "", "");
		if(predicates!=null){
			this.predicates=new HashSet<>();
			for(Predicate p : predicates){
				this.predicates.add(p);
			}
			updateDescription();
		}
	}

	public void update(Clause effect) {
		effect.predicates.forEach((Predicate pEffect)->predicates.removeIf((Predicate pThis)->pEffect.contradicts(pThis)));
		predicates.addAll(effect.predicates);
		updateDescription();
	}
	
	public void add(Predicate p){
		if(predicates==null)
			predicates=new HashSet<>();		
		this.predicates.add(p);
		updateDescription();
	}
	
	public int numOfSatisfied(Clause c){
		int counter=0;
		for(Predicate p: c.predicates)
			if(satisfies(p))
				counter++;
		return counter;
	}
	
	//checks if the given predicate is satisfied by this Clause Predicate set
	@Override
	public boolean satisfies(Predicate p){
		for(Predicate pr : predicates)
			if(pr.satisfies(p))
				return true;
		return false;
	}
	//checks if the given clause is satisfied by the clause Predicate Set - 
	//checks for each predicate from the given clause ifSatified by this Clause Predicate Set
	public boolean satisfies(Clause clause){
		for(Predicate p : clause.predicates){
			if(!satisfies(p))
				return false;
		}
		return true;
	}

	public Set<Predicate> getPredicates() {
		return predicates;
	}

	public String getSokobanPos() {
		for (Predicate p : predicates) {
			if (p.getType().equals("sokobanAt"))
					return p.getValue();
		}
		return "";
	}

	public String getBoxPos(String id) {	
		for (Predicate p : predicates) {
			if (p.getId().equals(id))
					return p.getValue();
		}
		return "";
	}

}