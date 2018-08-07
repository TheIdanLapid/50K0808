package searchLib;
/**
 * Represents a node in the graph, has a general state (for example: position)
 * Holds the state it came from, the cost and the action done to get to the state
 * SB - the signboard in the state
 * @author שדמה
 *
 */
public class State<T> {
	private T state;
	private State<T> cameFrom;
	private SearchLibAction action;
	private double cost;
	private char[][] sb;

	public char[][] getSb() {
		return sb;
	}
	public void setSB(char[][] sb)
	{
		this.sb = new char[sb.length][sb[0].length];
		for(int i=0;i<sb.length;i++){
			for(int j=0;j<sb[0].length;j++){
				this.sb[i][j]=sb[i][j];
			}
		}
	}

	public State(T state) {
		this.state=state;
		cost=Double.MAX_VALUE; //infinity (very large)
	}
	public T getState() {	
		return state;
	}
	public void setState(T state) {
		this.state = state;
	}
	public State<T> getCameFrom() {
		return cameFrom;
	}
	public void setCameFrom(State<T> cameFrom) {
		this.cameFrom = cameFrom;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	public SearchLibAction getAction() {
		return action;
	}
	public void setAction(SearchLibAction action) {
		this.action = action;
	}
	
	@Override
	public String toString() {
		return state.toString();
	}
	
	@Override
	public int hashCode() {
		return state.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		State<T> s = (State<T>)obj;
		return state.equals(s.state);
	}
	
}

