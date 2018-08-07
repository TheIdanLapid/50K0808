package controller.commands;

import commons.Level;
import controller.Controller;
import model.Model;
import view.GuiView;
import view.View;

/**
 * Exit command. We created an exit instance in CLI to manage opened streams.
 * @author שדמה
 *
 */
public class Exit extends SokobanCommand {

	private Level l;
	private Model model;
	private View view;
	private Controller controller;
	
	public Exit(Level l, Model model, View view, Controller controller)
	{
		super();
		this.l=l;
		this.model=model;
		this.view=view;
		this.controller = controller;
	}

	
	@Override
	public void execute() {
		if (view instanceof GuiView)
		{
			GuiView guiView = (GuiView) view;
			guiView.exit(l);
		}
		if (controller.getHandler()!=null && !controller.getHandler().getSocket().isClosed())
		{						
			controller.getHandler().closeSocket();
		}
		model.exit(l);
		controller.stop();
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
