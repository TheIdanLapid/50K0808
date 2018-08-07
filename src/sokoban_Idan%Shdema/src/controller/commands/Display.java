package controller.commands;

import commons.Level;
import model.Model;
import view.View;
/**
 * Command for displaying game on screen
 * @author שדמה
 *
 */
public class Display extends SokobanCommand {
	private Model model;
	private View view;
	private Level l;
	
	public Display(Level l,Model model,View view)
	{
		super();
		this.l=l;
		this.model = model;
		this.view = view;
	}

		@Override
	public void execute() {
		view.display(l);
		done = true;
	}
	
	//sets and gets
	public Level getL() {
		return l;
	}

	public void setL(Level l) {
		this.l = l;
	}
	
	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}
	
}
