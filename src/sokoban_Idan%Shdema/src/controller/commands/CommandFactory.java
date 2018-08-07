package controller.commands;

import java.util.HashMap;
import java.util.List;

import controller.Controller;
import model.Model;
import view.View;

/**
 * Creates commands with hashMap encoded by names of commands.
 * @author שדמה
 *
 */
public class CommandFactory {

		private List<String> params;
		private HashMap<String,Creator> commandCreator;
		private Model model;
		private View view;
		private Controller controller;

		public CommandFactory(List<String> params, Model model, View view, Controller controller) {
			this.params = params;
			this.model = model;
			this.view = view;
			this.controller = controller;
			
			commandCreator = new HashMap<String, Creator>();
			commandCreator.put("Load", new LoadCreator());
			commandCreator.put("Display", new DisplayCreator());
			commandCreator.put("Move", new MoveCreator());
			commandCreator.put("Save", new SaveCreator());
			commandCreator.put("Exit", new ExitCreator());
		}
		
		public Command createCommand()
		{
			Creator c = commandCreator.get(params.get(0)); //create a command with the first word
			
			if (c!=null)
				return c.create();

			return null; //also if the extension is not recognized
		}
		
		private interface Creator{
			public Command create();
		}
		
		private class LoadCreator implements Creator {
			
			public Command create() {
				return new LoadFileName(params.get(1),model.getLevel(),model);
			}
		}
		
		private class DisplayCreator implements Creator {
			
			public Command create() {
				return new Display(model.getLevel(),model,view);
			}
		}
	
		private class MoveCreator implements Creator {

			public Command create() {
				return new Move(params.get(1),model.getLevel(),model.getLevel().getChars().get(0),model); //our only character so far...
			}
		}
	
		private class SaveCreator implements Creator {

			public Command create() {
				return new SaveFileName(params.get(1),model.getLevel(),model);
			}
		}
		
		private class ExitCreator implements Creator {
			
			public Command create() {
				return new Exit(model.getLevel(),model,view,controller);
			}
		}

		//gets and sets
		public HashMap<String, Creator> getCommandCreator() {
			return commandCreator;
		}

		public List<String> getParams() {
			return params;
		}

		public void setParams(List<String> params) {
			this.params = params;
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

		public void setCommandCreator(HashMap<String, Creator> commandCreator) {
			this.commandCreator = commandCreator;
		}	
		
}
