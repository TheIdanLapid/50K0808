
package controller.commands;

import java.util.List;

/**
 * Abstract class for commands. All commands can use setParams and have to override execute.
 * @author שדמה
 *
 */
public abstract class SokobanCommand implements Command {
	protected List<String> params;	
	boolean done;
	
	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public void setParams(List<String> params) {
		this.params = params; 
	}
	
	public SokobanCommand() {
		done = false;
	}
	
	public abstract void execute();
}
