package controller.commands;

import commons.Level;
import model.Model;
import model.data.MovableItem;

/**
 * The move command, uses Policy to check for valid move, then moves the Char or Char pushing Box.
 * @author שדמה
 *
 */
public class Move extends SokobanCommand {

	private Model model;
	private String dir;
	private Level l;
	private MovableItem mi;
	
	public Move(String dir, Level l, MovableItem mi, Model model)
	{
		super();
		this.dir = dir;
		this.l = l;
		this.mi = mi;
		this.model = model;
	}

	@Override
	public void execute() {
		model.move(dir);
		done = true;
	}
	
	//sets and gets

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public Level getL() {
		return l;
	}

	public void setL(Level l) {
		this.l = l;
	}

	public MovableItem getMi() {
		return mi;
	}

	public void setMi(MovableItem mi) {
		this.mi = mi;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}
	

}
