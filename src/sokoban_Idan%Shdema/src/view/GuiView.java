package view;

import commons.Level;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
/**
 * 
 * @author Eon
 * Interface for the game main window
 */
public interface GuiView extends View {
	
	public void displayMessage(String msg);
	public void exit(Level l);
	public void bindTime(StringProperty time);
	public void bindSteps(IntegerProperty steps);
	public void start();
	public void setFinished(boolean bool);
	public void solutionAnimation(String sol, String hintOrSol);
	public void stopClock();
}
