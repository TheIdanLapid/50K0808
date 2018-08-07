package stripsLib;

import java.util.PriorityQueue;
/**
 * An interface for plan problems
 * @author ����
 *
 */
public interface Plannable {

	Clause getGoal(Clause kb);
	Clause getKnowledgebase();
	PriorityQueue<Action> getSatisfyingActions(Predicate top);
	Action getSatisfyingAction(Predicate top);
	void setKnowledgebase(Clause kb);
	
}
