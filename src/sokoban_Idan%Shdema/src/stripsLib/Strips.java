package stripsLib;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * Strips algorithm for ordering goals 
 */
public class Strips implements Planner{
	private Plannable plannable;

	@Override
	public List<Action> plan(Plannable plannable) {
		LinkedList<Action> plan=new LinkedList<>();
		this.setPlannable(plannable);
		Stack<Predicate> stack=new Stack<>();
		//1.Push goal into the stack
		stack.push(plannable.getGoal(plannable.getKnowledgebase()));
		//2.Repeat until stack is empty
		while(!stack.isEmpty()){
			Predicate top=stack.peek();
			if(! (top instanceof Action)){
				if(!plannable.getKnowledgebase().satisfies(top)){ // unsatisfied
					//3.If top is a multipart goal...
					if(top instanceof Clause){ 
						Clause c=(Clause)top; 
						//...push unsatisfied sub-goals into the stack
						stack.pop();
						for(Predicate p : c.predicates){
							stack.push(p);
						}
					//4.If top is a single unsatisfied goal - predicate
					}else{ 
						//5.Replace it with an action that satisfies the goal 
						//can be more than one action that lead to the goal - try them in desc order
						//the top priority is for the one with the most exist preconditions
						stack.pop();
						PriorityQueue<Action> actions = plannable.getSatisfyingActions(top);
						if (actions==null)
						{
							plan.clear();
							return plan;
						}
						while (!actions.isEmpty())
						{
							Action action = actions.poll();

							stack.push(action);
							//6.Push the action preconditions into the stack
							stack.push(action.preconditions);
						}
					}
				//11.If top is a satisfied goal...
				}else{
					//...pop it from the stack
					stack.pop();
				}
			//7.If top is an action	
			}else{
				//8.Pop it from the stack
				stack.pop();
				//9.Simulate its execution and update the knowledge base with its effects
				Action a=(Action)top;
				plannable.getKnowledgebase().update(a.effects);
				//10.Add the action to the plan
				plan.add(a);
			}
		}
		return plan;
	}

	public Plannable getPlannable() {
		return plannable;
	}

	public void setPlannable(Plannable plannable) {
		this.plannable = plannable;
	}

}
