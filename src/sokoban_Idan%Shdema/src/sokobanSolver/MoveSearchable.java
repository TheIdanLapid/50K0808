package sokobanSolver;

import java.util.HashMap;

import model.data.Position;
import searchLib.SearchLibAction;
import searchLib.State;
/**
 * A searchable for moving sokoban
 * @author שדמה
 *
 */
public class MoveSearchable extends CommonSokobanSearchable<Position> {
	
	public MoveSearchable(char[][] sb, Position init, Position goal) {
		super(sb);
		initState = new State<Position>(new Position(init.getX(), init.getY()));
		goalState = new State<Position>(new Position(goal.getX(), goal.getY()));
	}
	
	@Override
	public HashMap<SearchLibAction, State<Position>> getAllPossibleMoves(State<Position> state) {
		if (state.getCameFrom()==null)
		{
			state.setSB(signB);
		}
		else
		{
			state.setSB(state.getCameFrom().getSb());
		}
			addState('r',state, state.getSb());
			addState('l',state, state.getSb());
			addState('u',state, state.getSb());
			addState('d',state, state.getSb());
		return map;
	}
		
	public void addState(char dir, State<Position> state, char[][] sb)
	{
		int x = state.getState().getX();
		int y = state.getState().getY();
		
		Position p = new Position();
		String move = "";

		if (dir == 'r') {
			p = new Position(x,y+1);
			move = "Move right";
		}
		if (dir == 'l') {
			p = new Position(x,y-1);
			move = "Move left";
		}	
		if (dir== 'u') {
			p = new Position(x-1,y);
			move = "Move up";
		}
		if (dir == 'd') {
			p = new Position(x+1,y);
			move = "Move down";
		}
		
		State<Position> s = new State<Position>(p);
		s.setAction(new SearchLibAction(move));
		s.setCameFrom(state);
		s.setSB(sb);
		
		if(inSignBoard(p,s.getSb()) && isClear(p,s.getSb()))
		{
			if (state.getCameFrom()!=null && state.getCameFrom().getState().equals(s.getState()))
			{
			}
			else
			{
				s.setSB(changeSBforSok(state.getState(),p,sb));
				s.setState(p);
				map.put(new SearchLibAction(move), s);
			}
		}
	}
	
	public boolean isClear(Position p,char[][] sb) {
		if (sb[p.getX()][p.getY()]!='#' && sb[p.getX()][p.getY()]!='@' && sb[p.getX()][p.getY()]!='%')
		{
				return true;
		}
		return false;
	}
	
	public char[][] getSB()
	{
		return super.signB;
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
	
}
