package sokobanSolver;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

import model.data.Position;
import searchLib.BFS;
import searchLib.SearchLibAction;
import searchLib.Searchable;
import searchLib.Searcher;
import searchLib.Solution;
import searchLib.TwoPositions;
import stripsLib.Action;
import stripsLib.Clause;
import stripsLib.Plannable;
import stripsLib.Predicate;

/**
 *  Object adapter between plannable and solver
 */
public class SokobanPlannableAdapter implements Plannable {

	char[][] signB;
	Clause goal;
	Clause kb;
	Searcher<Position> generalSearcher;
	Searcher<TwoPositions> generalSearcher2;
	
	public SokobanPlannableAdapter(char[][] signboard){
		signB = signboard;
		goal = new Clause(null);
		kb = new Clause();
		generalSearcher = new BFS<Position>();
		generalSearcher2 = new BFS<TwoPositions>();
		int boxCount=0;
		int starCount=0;
		for(int i=0;i<signB.length;i++){
			for(int j=0;j<signB[i].length;j++){
				switch(signB[i][j]){
				case '#':kb.add(new Predicate("wallAt", "", i+","+j));break;
				case ' ':kb.add(new Predicate("clearAt", "", i+","+j));break;
				case 'A':kb.add(new Predicate("sokobanAt", "", i+","+j));break;
				case '@':boxCount++;kb.add(new Predicate("boxAt", "b"+boxCount, i+","+j));break;
				case 'o':starCount++;kb.add(new Predicate("starAt", "t"+starCount, i+","+j));break;
				}//need to add sok and box on star
			}
		}
	}
	
	@Override
	public Clause getGoal(Clause kb) {
		for(Predicate p : kb.getPredicates()){
			if(p.getType().startsWith("star")){
				goal.add(new Predicate("boxAt", "b"+p.getId().substring(1), p.getValue()));
			}
		}
		return goal;
	}
		

	@Override
	public void setKnowledgebase(Clause kb) {
		int boxCount=0;
		int starCount=0;
		for(int i=0;i<signB.length;i++){
			for(int j=0;j<signB[i].length;j++){
				switch(signB[i][j]){
				case '#':kb.add(new Predicate("wallAt", "", i+","+j));break;
				case ' ':kb.add(new Predicate("clearAt", "", i+","+j));break;
				case 'A':kb.add(new Predicate("sokobanAt", "", i+","+j));break;
				case '@':boxCount++;kb.add(new Predicate("boxAt", "b"+boxCount, i+","+j));break;
				case 'o':starCount++;kb.add(new Predicate("starAt", "t"+starCount, i+","+j));break;
				}//need to add sok and box on star
			}
		}
	}
	
	@Override
	public Clause getKnowledgebase() {
		return kb;
	}

	@Override
	public PriorityQueue<Action> getSatisfyingActions(Predicate top) {
		PriorityQueue<Action> queue = new PriorityQueue<>(20,new Comparator<Action>(){

			@Override
			public int compare(Action a1, Action a2) {
				return (int)(kb.numOfSatisfied(a1.getPreconditions()) - kb.numOfSatisfied(a2.getPreconditions()));
			}});
		//the position we want to go to
		int topX=getPosition(top.getValue()).getX();
		int topY=getPosition(top.getValue()).getY();
		//the position of the box we want to push
		int boxX=0;
		int boxY=0;
		if (top.getType().startsWith("boxAt"))
		{
			boxX=getPosition(kb.getBoxPos(top.getId())).getX();
			boxY=getPosition(kb.getBoxPos(top.getId())).getY();
		}
		//the position of sokoban character at the moment
		int sokX=getPosition(kb.getSokobanPos()).getX();
		int sokY=getPosition(kb.getSokobanPos()).getY();
		
		
		switch (top.getType()) {
		case "sokobanAt":
			sokX=getPosition(kb.getSokobanPos()).getX();
			sokY=getPosition(kb.getSokobanPos()).getY();
			Action stepAction = null;
			if (sokX-1==topX && sokY==topY)
				stepAction = getStepAction("Move up", top, topX, topY, sokX, sokY);
			else if (sokX+1==topX && sokY==topY)
				stepAction = getStepAction("Move down", top, topX, topY, sokX, sokY);
			else if (sokY-1==topY && sokX==topX)
				stepAction = getStepAction("Move left", top, topX, topY, sokX, sokY);
			else if (sokY+1==topY && sokX==topX)
				stepAction = getStepAction("Move right", top, topX, topY, sokX, sokY);
			if (stepAction!=null)
			{	
				queue.add(stepAction);
				break;
			}
			Solution sol = generalSearcher.search(new MoveSearchable(signB, new Position(sokX, sokY),new Position(topX, topY)));
			if (sol!=null)
			{
				
				sokX=getPosition(kb.getSokobanPos()).getX();
				sokY=getPosition(kb.getSokobanPos()).getY();
				
				Action moveAction = new Action("Move", "", kb.getSokobanPos() + ',' + top.getValue());
				moveAction.setPreconditions(new Clause(new Predicate("clearAt", "", topX + "," + topY)));
				moveAction.setEffects(new Clause(new Predicate("sokobanAt", "", topX + "," + topY), new Predicate("clearAt", "", sokX + "," + sokY)));
				
				moveAction.setSearchActions(sol.getActions());
				
				queue.add(moveAction);					
			}
			else 
			{
				return null;
			}
			break;			
		case "boxAt":
			Solution pushSol = generalSearcher2.search(new PushSearchable(signB, boxX, boxY, topX, topY, sokX, sokY));
			if (pushSol!=null)
			{
				String dir = pushSol.getActions().peek().getName();
				int x=boxX;
				int y=boxY;
				
				Position p = getOpPosition(dir,x,y);
				String lastBoxDir = pushSol.getActions().peekLast().getName();
				x=topX;
				y=topY;
				Position sokAfterGoal = getOpPosition(lastBoxDir,x,y);
				
				Searchable<Position> searchable = new MoveSearchable(signB,new Position(sokX, sokY),p);
				Solution moveSol = generalSearcher.search(searchable);
				
				if (moveSol != null)
				{
					Action pushAction = new Action("Push", top.getId() , kb.getBoxPos(top.getId()) + ',' + top.getValue());
					pushAction.setPreconditions(new Clause(new Predicate("sokobanAt", "", p.getX()+","+p.getY()), new Predicate("starAt", top.getId().substring(1), topX + "," + topY)));
					pushAction.setEffects(new Clause(new Predicate("boxAt", top.getId().substring(1), topX + "," + topY), new Predicate("sokobanAt", "", sokAfterGoal.getX() + "," + sokAfterGoal.getY()), new Predicate("clearAt", "", sokX + "," + sokY)));
					
					pushAction.setSearchActions(pushSol.getActions());
					
					queue.add(pushAction); //add push to the stack first so it will be popped after move 
					
					Action moveAction = new Action("Move", "", kb.getSokobanPos() + ',' + p);
					moveAction.setPreconditions(new Clause(new Predicate("sokobanAt", "",sokX + "," + sokY), new Predicate("clearAt", "", p.getX() + "," + p.getY())));
					moveAction.setEffects(new Clause(new Predicate("sokobanAt", "", p.getX() + "," + p.getY()), new Predicate("clearAt", "", sokX + "," + sokY)));
					
					moveAction.setSearchActions(moveSol.getActions());
					
					queue.add(moveAction);
					
				}
				else { //moveSol is null - sokoban can't reach the box! :(
					return null;
				}
			}
			else { //pushSol is null - there is no path from the box to the star
				return null;
			}
			break;			

		}		
		return queue;
	}
	
	private Position getOpPosition(String dir, int x, int y)
	{
		Position p = new Position();
		if (dir.equals("Move right"))
		{
			p = new Position(x,y-1);
		}
		else if (dir.equals("Move left")) {
			p = new Position(x,y+1);
		}	
		else if (dir.equals("Move up")) {
			p = new Position(x+1,y);
		}
		else if (dir.equals("Move down")) {
			p = new Position(x-1,y);
		}
		return p;
	}

	private Action getStepAction(String direction, Predicate top, int topX, int topY, int sokX, int sokY)
	{
		Action stepAction = new Action("Step", "", kb.getSokobanPos() + ',' + top.getValue());
		stepAction.setPreconditions(new Clause(new Predicate("clearAt", "", topX + "," + topY)));			
		stepAction.setEffects(new Clause(new Predicate("sokobanAt", "", topX + "," + topY),new Predicate("clearAt", "", sokX + "," + sokY)));
		LinkedList<SearchLibAction> stepList = new LinkedList<SearchLibAction>();
		stepList.add(new SearchLibAction(direction));
		stepAction.setSearchActions(stepList);
		
		return stepAction;
	}
	
	@Override
	public Action getSatisfyingAction(Predicate top) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Position getPosition(String pos){
		int index = pos.indexOf(",");
		int x = Integer.parseInt(pos.substring(0,index));
		int y = Integer.parseInt(pos.substring(index+1,pos.length()));
		return new Position(x,y);
	}

}
