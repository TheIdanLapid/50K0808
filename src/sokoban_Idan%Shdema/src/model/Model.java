package model;

import java.util.LinkedList;

import commons.Level;
import model.db.Score;
/**
 * A facade for the model layer.
 * @author Eon
 *
 */
public interface Model {
	//observable - only can defined in the implementations 
	//the state of the game
	public Level getLevel();
	public char[][] getSignBoard(); 

	public boolean isLoaded();
	
	public void move(String dir);
	public void load(String fileName);
	public void save(String fileName);
	public void exit(Level l);
	public void userDetails(LinkedList<String> params);
	public void levelDetails(Level level);
	public LinkedList<Score> getScoreListUser(String string);
	public LinkedList<Score> getScoreListLevel(String string);
	LinkedList<Score> getScoreList();
	void topScoresUser(String filter);
	void topScoresLevel(String filter);
	public void openSocket();
	public void helpFromServer(String hintOrSol);
//	public void solutionAnimation(String sol, String hintOrSol);
	public void sayBye();
}
