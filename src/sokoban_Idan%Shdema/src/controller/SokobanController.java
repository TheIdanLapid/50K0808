package controller;

/**
 * Class that contains Controller, facades to model and view, and server.
 * It binds the timer and step counter from the loaded level to the GUI labels,
 * operates the timer,
 * sets keys from "keys.xml",
 * has methods that start and stop the server, start is called in Main,
 * stop is not activated because we want the server to stay open for another client.
 * it observes model, view and the server's client handler and operates according to notifications.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import controller.commands.Command;
import controller.commands.CommandFactory;
import controller.server.Handler;
import controller.server.SokobanServer;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.Model;
import view.GuiView;
import view.View;

public class SokobanController implements Observer {
	private Model model; // reference facade to model
	private View view; // reference facade to view
	private Controller controller; // reference to general Controller class
	private SokobanServer server;
	private Handler handler;
	
	private CommandFactory cf;
	private Command c;
	
	private StringProperty time;
	private IntegerProperty steps;
	
	Timer t = new Timer();
	
	boolean timerStop;

	private boolean serverOn = false; // a flag to see if we run on server or locally

	public SokobanController(Model model, View view) {
		timerStop = false;
		handler = null;
		this.model = model;
		this.view = view;
		
		if (view != null) // GUI
		{
			time = new SimpleStringProperty();
			steps = new SimpleIntegerProperty();

			Timer t = new Timer();

			t.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					if (timerStop)
					{
						t.cancel();
					}
					else
					{
						int sec = model.getLevel().getMyTimer().getSec();
						int min = model.getLevel().getMyTimer().getMin();
						int hour = model.getLevel().getMyTimer().getHour();
	
						if (sec == 59) {
							min++;
							sec = 0;
						} else
							sec++;
						if (min == 60) {
							hour++;
							min = 0;
						}
	
						model.getLevel().getMyTimer().setHour(hour);
						model.getLevel().getMyTimer().setMin(min);
						model.getLevel().getMyTimer().setSec(sec);
						Platform.runLater(new Runnable() { // move to JavaFX thread
	
							@Override
							public void run() {
								time.set(model.getLevel().getMyTimer().toString());
							}
						});
					}
				}

			}, 0, 1000);

		}

		controller = new Controller();
		controller.start();
	}

	public void update(Observable obs, Object arg) {
		if (arg instanceof Handler)
		{
			handler = (Handler) arg;
		}
		else
		{
			@SuppressWarnings("unchecked")
			LinkedList<String> params = (LinkedList<String>) arg; // O(1) solution
			if (obs == model) {
				
				if (params.get(0).equals("notLoaded")) {
					if (serverOn)
						server.getCh().feedback(params.get(0));
					else
					{
						GuiView guiView = (GuiView) view;
						guiView.displayMessage(params.get(0));
					}
				}
	
				else // a level is currently loaded
				{
	
					if (serverOn) { //model updated the controller (on server)
						if (params.get(0).equals("Load")) {
							server.getCh().display(model.getLevel());
	
							t.scheduleAtFixedRate(new TimerTask() {
	
								@Override
								public void run() {
									int sec = model.getLevel().getMyTimer().getSec();
									int min = model.getLevel().getMyTimer().getMin();
									int hour = model.getLevel().getMyTimer().getHour();
	
									if (sec == 59) {
										//min++;
										sec = 0;
									} else
										//sec++;
									if (min == 60) {
										//hour++;
										min = 0;
									}
	
									model.getLevel().getMyTimer().setHour(hour);
									model.getLevel().getMyTimer().setMin(min);
									model.getLevel().getMyTimer().setSec(sec);
								}
							}, 0, 1000);
						} else if (params.get(0).equals("Move"))
							server.getCh().display(model.getLevel());
						else if (params.get(0).equals("Display"))
							server.getCh().display(model.getLevel());
						else if (params.get(0).equals("WIN"))
						{
							server.getCh().displayWin(model.getLevel());
						}
						else
							server.getCh().feedback(params.get(0));
					}
	
					else //model updated the controller (on GUI)
					{
						GuiView guiView = (GuiView) view;
						Platform.runLater(new Runnable() { // move to JavaFX thread
	
							@Override
							public void run() {
								time.set(model.getLevel().getMyTimer().toString());
								steps.set(model.getLevel().getSteps());
							}
	
						});
	
						
						
						if (params.get(0).equals("Load")) {
							LinkedList<String> display = new LinkedList<String>();
							display.add("Display");
							CommandFactory cf = new CommandFactory(display, model, guiView, controller);
							Command c = cf.createCommand();
							controller.insertCommand(c);
	
						} 
						else if (params.get(0).equals("cannotSolve")) {
							guiView.displayMessage("cannotSolve");
						} 
						else if (params.get(0).equals("stopAnim")) {
							guiView.stopClock();
						} 
						else if (params.get(0).equals("Move")) {
							LinkedList<String> display = new LinkedList<String>();
							display.add("Display");
							CommandFactory cf = new CommandFactory(display, model, guiView, controller);
							Command c = cf.createCommand();
							controller.insertCommand(c);
						} 
						else if (params.get(0).equals("Display")) {
							LinkedList<String> display = new LinkedList<String>();
							display.add("Display");
							CommandFactory cf = new CommandFactory(display, model, guiView, controller);
							Command c = cf.createCommand();
							controller.insertCommand(c);
						} else if (params.get(0).equals("WIN")) {
							timerStop = true;
							guiView.setFinished(true);
							LinkedList<String> display = new LinkedList<String>();
							display.add("Display");
							CommandFactory cf = new CommandFactory(display, model, guiView, controller);
							Command c = cf.createCommand();
							controller.insertCommand(c);
							guiView.displayMessage(params.get(0));
						}
						else if (params.get(0).equals("Saved")) {
							guiView.displayMessage(params.get(0));
						}
						else if (params.get(0).equals("Animation")) {
							guiView.solutionAnimation(params.get(1),params.get(2));
						}
						else if (params.get(0).equals("sayBYE")) {
							model.sayBye();
						}
						
					}
				}
			}

			if (obs == view) { // if view updates the controller
	
				if (!serverOn) //on GUI
				{
					GuiView guiView = (GuiView) view;
					if (params.get(0).equals("openSocket"))
						model.openSocket();
					else if (params.get(0).equals("hint"))
						model.helpFromServer("hint");
					else if (params.get(0).equals("solution"))
						model.helpFromServer("solution");
					else if (!(model.isLoaded()) && (params.get(0).equals("Move")))
					{
						guiView.displayMessage("notLoaded");
					}
					else {
						CommandFactory cf2 = new CommandFactory(params, model, view, controller);
						Command c2 = cf2.createCommand();
						if (c2 != null) // c exists
						{
							controller.insertCommand(c2);
							guiView.bindTime(time);
							guiView.bindSteps(steps);
						} else
							guiView.displayMessage("noCommand");
					}
				} 
				else //server updates the controller with input from the client
				{
					boolean loaded = false;
					
					if (!(model.isLoaded()))
					{
						if (params.get(0).equals("Load"))
						{
							cf = new CommandFactory(params, model, view, controller);
							c = cf.createCommand();
							loaded = true;
						}
						else
						{
							if (!params.get(0).equals("Exit"))
								server.getCh().feedback("notLoaded");
						}
					}
					else //a level is loaded
					{
						cf = new CommandFactory(params, model, view, controller);
						c = cf.createCommand();
					}
					if (c != null) //command exists
					{
						if (cf.getParams().get(0).equals("Exit"))
						{
							controller.setHandler(handler);
						}
						controller.insertCommand(c);
					}

					else {
						if (loaded)
							server.getCh().feedback("noCommand");
					}
				}
			}
		}
	}
	public HashMap<String, String> mapKeyCodes(File f) {

		Scanner s;

		try {
			HashMap<String, String> keyMap = new HashMap<String, String>();

			s = new Scanner(f);

			while (s.hasNextLine()) {
				String str = s.nextLine();
				String[] keyVal = str.split(":");
				keyMap.put(keyVal[0], keyVal[1]);
			}
			s.close();
			return keyMap;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return null;

	}

	public void startServer(int port) throws Exception {
		server = new SokobanServer(port, (Handler) view);
		server.start();
	}

	public void stopServer() {
		server.stopServer();
	}

	// gets and sets
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

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public void setServerOn() {
		serverOn = true;
	}

	public SokobanServer getServer() {
		return server;
	}
}
