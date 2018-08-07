package view;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import commons.Level;
import commons.SignBoard;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * The GUI controller, it's a view implementation, observed by
 * SokobanController.<br>
 * Contains methods for the menu: loading, saving and exiting.<br>
 * Exits in the right order and closes everything that's open in the GUI.
 * 
 * @author Eon
 *
 */
public class MainWindowController extends Observable implements Initializable, GuiView, WindowView {
	private Stage primaryStage;
	private Pane detailsPane;
	private char[][] sokobanData;
	private HashMap<String, String> keyMap;
	private boolean loaded;
	private MediaPlayer mp;
	private boolean finished;
	private File chosen;
	private Timer t;
	private boolean helpUsed;

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	@FXML
	SokobanDisplayer sokobanDisplayer;

	@FXML
	Label clock;

	@FXML
	Label steps;

	@FXML
	TextField fName;
	
	@FXML
	TextField lName;
	
	@FXML
	TextField Iduser;
	
	@FXML
	Button hint;
	
	@FXML
	Button solution;
	
	// is called when the class is made, and we can also call it again in other places
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		String path = "./resources/music.mp3";
		Media media = new Media(new File(path).toURI().toString());
		mp = new MediaPlayer(media);

		loaded = false;
		
		sokobanDisplayer.setSokobanData(sokobanData); // controller sends
														// signboard

		sokobanDisplayer.setFocusTraversable(true);
		sokobanDisplayer.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (!finished)
				{
					String direction = "";
					if (event.getCode().getName().equals(keyMap.get("up"))) {
						direction = "up";
					} else if (event.getCode().getName().equals(keyMap.get("down"))) {
						direction = "down";
					} else if (event.getCode().getName().equals(keyMap.get("right"))) {
						sokobanDisplayer.setCharFileName(sokobanDisplayer.getBobRightFileName());
						direction = "right";
					} else if (event.getCode().getName().equals(keyMap.get("left"))) {
						sokobanDisplayer.setCharFileName(sokobanDisplayer.getBobLeftFileName());
						direction = "left";
					}
	
					if (!direction.equals("")) {
						LinkedList<String> params = new LinkedList<String>();
						params.add("Move");
						params.add(direction);
	
						setChanged();
						notifyObservers(params);
					}
				}
			}
		});
	}
	@FXML
	public void openFile() {
		helpUsed = false;
		FileChooser fc = new FileChooser();
		fc.setTitle("Open Sokoban file"); // title for the file dialog
		fc.setInitialDirectory(new File("./")); // the directory to open
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml")); // file
																								// types
																								// to
																								// choose
																								// from
		finished = false;
		chosen = fc.showOpenDialog(primaryStage); // return main window

		LinkedList<String> params = new LinkedList<String>();
		params.add("Load");
		params.add(chosen.getName());

		setChanged();
		notifyObservers(params);

		// load music
		if (mp != null)
			mp.stop();

		mp.setAutoPlay(true);
		mp.play();

		// if loaded correctly - display
		LinkedList<String> params2 = new LinkedList<String>();
		params2.add("Display");

		setChanged();
		notifyObservers(params2);
		
		loaded = true;
		sokobanDisplayer.setCharFileName(sokobanDisplayer.getCharFileName());
	}
	@FXML
	public void muteMusic() {
		mp.stop();
	}
	@FXML
	public void saveFile() {

		if (!loaded) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("WARNING!!!");
			alert.setHeaderText("Load a level first!");
			alert.setContentText("Cannot save.");

			alert.showAndWait();
		} else {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save current level as"); // title for the file
															// dialog
			fileChooser.setInitialDirectory(new File("./")); // the directory to
																// open
			// Set extension filter
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML file", "*.xml");
			fileChooser.getExtensionFilters().add(extFilter);

			// Show save file dialog
			File file = fileChooser.showSaveDialog(primaryStage);
			LinkedList<String> params = new LinkedList<String>();
			if (file!=null)
			{
				params.add("Save");
				params.add(file.getName());
				setChanged();
				notifyObservers(params);
			}
		}

	}
	@FXML
	public void exitGame() {
		LinkedList<String> params = new LinkedList<String>();
		params.add("Exit");

		setChanged();
		notifyObservers(params);
		if (loaded) {
			mp.dispose();
		}
		exitApplication();
	}

	public void setStage(Stage primaryStage) 
	{
		
		this.primaryStage = primaryStage;
		
		//ask user "run on server?"

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Run on server?");
		String s = "Do you want to run on the server?";
		alert.setContentText(s);
		Optional<ButtonType> result = alert.showAndWait();
		if ((result.isPresent()) && (result.get() == ButtonType.OK)) 
		{	
			if (hint.isVisible())
			{
	    		LinkedList<String> params = new LinkedList<String>();
	    		params.add("openSocket");
	        	setChanged();
	        	notifyObservers(params);
			}
		}
    	else 
    	{
    		hint.setVisible(false);
    		solution.setVisible(false);
    	}
	}

	@Override
	public void displayMessage(String msg) {
		if (loaded == false)
		{
			if (msg.equals("notLoaded")) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("WARNING!!!");
				alert.setHeaderText("Load a level first!");
				alert.setContentText("Please load a level.");
	
				alert.showAndWait();
			}
		}

		else if (msg.equals("Saved")) {
			Platform.runLater(new Runnable() { // move to JavaFX thread

				@Override
				public void run() {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Level saved");
					alert.setHeaderText("Level saved successfully!");

					alert.showAndWait();
				}
			});
		}

		else if (msg.equals("noCommand")) {
			Platform.runLater(new Runnable() { // move to JavaFX thread

			@Override
			public void run() {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("WARNING!!!");
				alert.setHeaderText("Command not recognized.");
	
				alert.showAndWait();
				}
			});
		}
		else if (msg.equals("WIN")) {
			mp.stop();
			switchScene();
		}
		else if (msg.equals("cannotSolve")) {
			Platform.runLater(new Runnable() { // move to JavaFX thread

			@Override
			public void run() {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("You're stuck!");
				alert.setHeaderText("Sorry, there's no solution. Please reload the level and try again :)");
	
				alert.showAndWait();
				}
			});
		}
	}


	
	@Override
	public void display(Level l) {
		sokobanData = new SignBoard(l).getSignBoard(); // init sokobanData with
														// signboard from l
		sokobanDisplayer.setSokobanData(sokobanData);
		sokobanDisplayer.redraw();
	}

	@Override
	public void exit(Level l) {
		LinkedList<String> params = new LinkedList<String>();
		params.add("sayBYE");
		setChanged();
		notifyObservers(params);
		Platform.exit();
	}

	@FXML
	public void exitApplication() {
		LinkedList<String> params = new LinkedList<String>();
		params.add("sayBYE");
		setChanged();
		notifyObservers(params);
		System.exit(0);
	}

	public HashMap<String, String> getKeyMap() {
		return keyMap;
	}

	public void setKeyMap(HashMap<String, String> keyMap) {
		this.keyMap = keyMap;
	}

	@Override
	public void bindTime(StringProperty time) {
		this.clock.textProperty().bind(time);
	}

	@Override
	public void bindSteps(IntegerProperty steps) {
		this.steps.textProperty().bind(steps.asString());
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	public void setDetails(){
		LinkedList<String> params = new LinkedList<String>();
		params.add("Details"); // so the controller knows it's the user details 
		params.add(fName.getText());
		params.add(lName.getText());
		params.add(Iduser.getText()); // casting to int when receive
		setChanged();
		notifyObservers(params);
	}

	public void setDetailsPane(Pane details) {
		detailsPane = details;
	}
	
	public void hint()
	{
		if (helpUsed)
			return;
		LinkedList<String> params = new LinkedList<String>();
		params.add("hint");
		setChanged();
		notifyObservers(params);
		helpUsed = true;
	}
	
	public void solution() 
	{
		if (helpUsed)
			return;
		LinkedList<String> params = new LinkedList<String>();
		params.add("solution");
		setChanged();
		notifyObservers(params);
		helpUsed = true;
	}
	
	public void solutionAnimation(String sol, String hintOrSol)
	{
		if (hintOrSol.equals("hint"))
		{
			Platform.runLater(() -> {
				LinkedList<String> params = new LinkedList<String>();
				switch (sol.charAt(0)) {
				case 'r':
					params.add("Move");
					params.add("right");
					sokobanDisplayer.setCharFileName(sokobanDisplayer.getBobRightFileName());
					break;
				case 'l':
					params.add("Move");
					params.add("left");
					sokobanDisplayer.setCharFileName(sokobanDisplayer.getBobLeftFileName());
					break;
				case 'u':
					params.add("Move");
					params.add("up");
					break;
				case 'd':
					params.add("Move");
					params.add("down");
					break;
				}
				setChanged();
				notifyObservers(params);
			});
		}
		else if (hintOrSol.equals("solution"))
		{
			Platform.runLater(() -> {
				LinkedList<String> params = new LinkedList<String>();
				t = new Timer();
				t.scheduleAtFixedRate(new TimerTask() {
					int i=0;
					
					@Override
					public void run() {
						params.clear();
						if (i<sol.length())
						{
							switch (sol.charAt(i)) {
							case 'r':
								params.add("Move");
								params.add("right");
								sokobanDisplayer.setCharFileName(sokobanDisplayer.getBobRightFileName());
								break;
							case 'l':
								params.add("Move");
								params.add("left");
								sokobanDisplayer.setCharFileName(sokobanDisplayer.getBobLeftFileName());
								break;
							case 'u':
								params.add("Move");
								params.add("up");
								break;
							case 'd':
								params.add("Move");
								params.add("down");
								break;
							}
							i++;
							setChanged();
							notifyObservers(params);
						}
						if (i==sol.length()-1)
						{
							t.cancel();
							Platform.runLater(() -> {
								Alert alert = new Alert(AlertType.CONFIRMATION);
								alert.setTitle("Restart level?");
								String s = "Do you want to restart the level (OK) or choose a new level (Cancel)?";
								alert.setContentText(s);
								Optional<ButtonType> result = alert.showAndWait();
								if ((result.isPresent()) && (result.get() == ButtonType.OK)) 
								{	
									LinkedList<String> params = new LinkedList<String>();
									params.add("Load");
									params.add(chosen.getName());

									setChanged();
									notifyObservers(params);

									// load music
									if (mp != null)
										mp.stop();

									mp.setAutoPlay(true);
									mp.play();

									// if loaded correctly - display
									LinkedList<String> params2 = new LinkedList<String>();
									params2.add("Display");

									setChanged();
									notifyObservers(params2);
									
									helpUsed = false;
									finished = false;
										
									loaded = true;
								}
								else
								{
									openFile();
								}
							});
							
						}
					}
				}, 0, 500);
			});
		}
	}

	public void stopClock() {
		if (t!=null)
			t.cancel();
	}
	
	@Override
	public void switchScene() {
		Platform.runLater(new Runnable() { // move to JavaFX thread

			@Override
			public void run() {
				if(detailsPane.getScene() != null)
					detailsPane.getScene().setRoot(new Region());
				Scene scene = new Scene(detailsPane, 600, 600);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				primaryStage.setScene(scene);
				primaryStage.show();
			}
		});
	}
}
