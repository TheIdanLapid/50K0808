package sokobanSolver;

import java.util.HashMap;

import model.data.Position;
import searchLib.BFS;
import searchLib.SearchLibAction;
import searchLib.Searchable;
import searchLib.Searcher;
import searchLib.SokPath;
import searchLib.Solution;
import searchLib.State;
import searchLib.TwoPositions;
/**
 * A searchable for pushing the box, including inner movements
 * @author שדמה
 *
 */
public class PushSearchable extends CommonSokobanSearchable<TwoPositions> {

	State<TwoPositions> lastState;
	Solution move;
	boolean first;
	//init
	public PushSearchable(char[][] sb, int initX, int initY, int goalX, int goalY, int sokX, int sokY) {
		super(sb);
		first = false;
		initState = new State<TwoPositions>(new TwoPositions(new Position(initX, initY), new Position(sokX, sokY)));
		goalState = new State<TwoPositions>(new TwoPositions(new Position(goalX, goalY)));
	}
	
	//successors
	@Override
	public HashMap<SearchLibAction, State<TwoPositions>> getAllPossibleMoves(State<TwoPositions> state) {
		//first move
		if (state.getCameFrom()==null)
		{
			state.setSB(signB);
		}
		//insert relevant moves to the map
			addState('r',state, state.getSb());
			addState('l',state, state.getSb());
			addState('u',state, state.getSb());
			addState('d',state, state.getSb());
		return map;
	}
	
	public void addState(char dir, State<TwoPositions> state, char[][] sb)
	{
		if (state.getCameFrom() == null) //initial state (first push of this box)
		{
			first = true;
			lastState = initState;
		}
		else
		{
			lastState = state.getCameFrom();
		}
		//take the current box position into x,y
		int x = state.getState().getBox().getX();
		int y = state.getState().getBox().getY();
		
		//p - the position the box move to
		//op - the position of sokoban to push the box
		Position p = new Position();
		Position op = new Position();
		String move = "";
		
		//the current position of box,sok (before moving)
		Position boxP = state.getState().getBox();
		Position sokP = state.getState().getSok();
		
		if (dir == 'r') {
			p = new Position(x,y+1);
			op = new Position(x,y-1);
			move = "Move right";
		}
		if (dir == 'l') {
			p = new Position(x,y-1);
			op = new Position(x,y+1);
			move = "Move left";
		}	
		if (dir== 'u') {
			p = new Position(x-1,y);
			op = new Position(x+1,y);
			move = "Move up";
		}
		if (dir == 'd') {
			p = new Position(x+1,y);
			op = new Position(x-1,y);
			move = "Move down";
		}
		
		//s is a state initialized with the new position of box and sok (p - new pos of box, boxP - curr pos of box, new pos of sok 
		State<TwoPositions> s = new State<TwoPositions>(new TwoPositions(p,boxP));
		s.setAction(new SearchLibAction(move));
		//set the s sb from state (previous state of s)
		s.setSB(sb);
		
		SokPath path = new SokPath();
		
		if (inSignBoard(p,s.getSb()) && inSignBoard(op,s.getSb()))
		{
			if(isClear(p,boxP,s.getSb())&&isClear(op,boxP,s.getSb()))
			{
				if (first)
				{
					//the move box+sok
					char[][] sbTemp = changeSBforBox(boxP,p,s.getSb());
					s.setSB(sbTemp);
					s.setState(new TwoPositions(p,sokP));
					if(!p.equals(sokP)){
						char[][] sbTempSok = changeSBforSok(sokP,boxP,s.getSb()); 
						s.setSB(sbTempSok);
					}
					s.setState(new TwoPositions(p,boxP));
					boxP = s.getState().getBox();
					sokP = s.getState().getSok();
					map.put(s.getAction(),s);
					first = false;
					
					
				}
				//not the first move
				else
				{
					//check if moves in a different dir
					//or if its first move (assume the sok in the right position) && !(state.getState().getSok().equals(op))
					if (state.getAction()!=null && dir!=state.getAction().getName().charAt(5))
					{
						//checks with bfs if sokoban can get the position next to the box to push it (from its curr pos, in curr sb)
						Solution actions = moveSokoban(state.getState().getSok(),op,state.getSb());
						if (actions != null && actions.getActions().size()>0)
						{
							boxP = state.getState().getBox();
							sokP = state.getState().getSok();
							for (int j = 0; j < actions.getActions().size(); j++) {
								path.addAction(actions.getActions().get(j));	
							}
							path.addAction(s.getAction());
							path.setName(s.getAction().getName());
							s.setAction(path);

								if (path.getActions().size()>0) 
								{
									char[][] sbTemp = changeSBforBox(boxP,p,s.getSb());
									s.setSB(sbTemp);
									s.setState(new TwoPositions(p,sokP));
									char[][] sbTempSok = changeSBforSok(sokP,boxP,s.getSb()); 
									s.setSB(sbTempSok);
									s.setState(new TwoPositions(p,boxP));

									//update box+sok positions to be after bfs move+push
									boxP = s.getState().getBox();
									sokP = s.getState().getSok();

									map.put(path, s);
								}
	//						}
						}
					}
					//same dir
					else
					{
						char[][] sbTemp = changeSBforBox(boxP,p,s.getSb());
						s.setSB(sbTemp);
						s.setState(new TwoPositions(p,sokP));
						char[][] sbTempSok = changeSBforSok(sokP,boxP,s.getSb()); 
						s.setSB(sbTempSok);
						s.setState(new TwoPositions(p,boxP));

						boxP = s.getState().getBox();
						sokP = s.getState().getSok();
						map.put(s.getAction(), s);
					}
				}
			}
		}
	}
	
	boolean isClear(Position p, Position boxP, char[][] sb){
		if (sb[p.getX()][p.getY()]!='#')
		{
			if (sb[p.getX()][p.getY()]!='@' && sb[p.getX()][p.getY()]!='%') //can be clear, sok or sok on star
				return true;
			else
				return false;
		}
		return false;
	}

	private Solution moveSokoban(Position start, Position end,char[][] sb) {
		Searcher<Position> searcher = new BFS<Position>();
		Searchable<Position> fromWhere = new MoveSearchable(sb, start, end);
		Solution actions = searcher.search(fromWhere);
		return actions;
	}

	public State<TwoPositions> getLastState() {
		return lastState;
	}


	public void setLastState(State<TwoPositions> lastState) {
		this.lastState = lastState;
	}
	
	public void setSB(char[][] sb)
	{
		super.signB = new char[sb.length][sb[0].length];
		for(int i=0;i<sb.length;i++){
			for(int j=0;j<sb[0].length;j++){
				signB[i][j]=sb[i][j];
			}
		}
	}
	
	public char[][] changeSBforBox(Position from, Position to, char[][] sb)
	{
		
		if (sb[from.getX()][from.getY()]=='@') //box
		{
			if (sb[to.getX()][to.getY()]==' ')
			{
				sb[to.getX()][to.getY()]='@';
				sb[from.getX()][from.getY()]=' ';
			}
			else if (sb[to.getX()][to.getY()]=='A' && first)
			{
				sb[to.getX()][to.getY()]='@';
				sb[from.getX()][from.getY()]='A';
			}
			else if (sb[to.getX()][to.getY()]=='$' && first) //char on star
			{
				sb[to.getX()][to.getY()]='%';
				sb[from.getX()][from.getY()]='A';
			}
			else if (sb[to.getX()][to.getY()]=='o')
			{
				sb[to.getX()][to.getY()]='%';
				sb[from.getX()][from.getY()]=' ';
			}
	
		}
		
		else if (sb[from.getX()][from.getY()]=='%') //box on star
		{
			if (sb[to.getX()][to.getY()]==' ')
			{
				sb[to.getX()][to.getY()]='@';
				sb[from.getX()][from.getY()]='o';
			}
			else if (sb[to.getX()][to.getY()]=='o')
			{
				sb[to.getX()][to.getY()]='%';
				sb[from.getX()][from.getY()]='o';
			}
			else if (sb[to.getX()][to.getY()]=='A' && first)
			{
				sb[to.getX()][to.getY()]='@';
				sb[from.getX()][from.getY()]='$';
			}
			else if (sb[to.getX()][to.getY()]=='$' && first)
			{
				sb[to.getX()][to.getY()]='%';
				sb[from.getX()][from.getY()]='$';
			}
		}
		
		return sb;
	}

}
