package sokobanSolver;

import java.util.HashMap;

import model.data.Position;
import searchLib.SearchLibAction;
import searchLib.Searchable;
import searchLib.State;
/**
 * A general searchable, common for search problems
 * @author שדמה
 *
 */
public abstract class CommonSokobanSearchable<T> implements Searchable<T> {
	char[][] signB;
	State<T> initState;
	State<T> goalState;
	HashMap<SearchLibAction, State<T>> map;
	
	public CommonSokobanSearchable(char[][] sb){
		initState = null;
		goalState = null;
		map = new HashMap<>();
		signB = new char[sb.length][sb[0].length];
		for(int i=0;i<sb.length;i++){
			for(int j=0;j<sb[0].length;j++){
				signB[i][j]=sb[i][j];
			}
		}
	}
	
	
	public boolean inSignBoard(Position p, char[][] sb)
	{
		int x = p.getX();
		int y = p.getY();
		if (x<sb.length && x>=0 && y<sb[0].length && y>=0)
			return true;
		return false;
	}
	
	@Override
	public State<T> getInitialState() {
		return initState;
	}
	
	@Override
	public State<T> getGoalState() {
		return goalState;
	}
	
	public void printSB(char[][] sb) {
		for (int i = 0; i < sb.length; i++) {
			System.out.print("[");
			System.out.print(sb[i]);
			System.out.println("]");
		}
	}
	
	public char[][] changeSBforSok(Position from, Position to, char[][] sb)
	{
		if (sb[from.getX()][from.getY()]=='$') //char on star
		{
			if (sb[to.getX()][to.getY()]==' ')
			{
				sb[to.getX()][to.getY()]='A';
			}
			else if (sb[to.getX()][to.getY()]=='o')
			{
				sb[to.getX()][to.getY()]='$';
			}
			sb[from.getX()][from.getY()]='o';
		}
		
		else if (sb[from.getX()][from.getY()]=='A') //char
		{
			if (sb[to.getX()][to.getY()]==' ')
			{
				sb[to.getX()][to.getY()]='A';
			}
			else if (sb[to.getX()][to.getY()]=='o')
			{
				sb[to.getX()][to.getY()]='$';
			}
			sb[from.getX()][from.getY()]=' ';
		}
	
		return sb;
	}
	
	@Override
	public abstract HashMap<SearchLibAction, State<T>> getAllPossibleMoves(State<T> state);

}
