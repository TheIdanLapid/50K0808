package view;

import java.io.File;

import commons.Level;
import controller.DBController;
import controller.SokobanController;
import controller.server.Handler;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.MyModel;

/**
 * The main class that runs the game, can run with parameters "-server" and
 * port,<br>
 * to run the game on a server,<br>
 * or if no parameters - runs with GUI.
 * 
 * @author Eon
 *
 */
public class Main extends Application {

	private Stage primaryStage;
	private File keys;
	private MainWindowController view;
	private DetailsWindowController detailsWindowController;
	private ScoreboardWindowController scoreboardWindowController;
	@FXML
	MenuItem Exit;

	@Override
	public void start(Stage primaryStage) {

		this.setPrimaryStage(primaryStage);
		this.keys = new File("keys.xml");
		try {

			FXMLLoader loader = new FXMLLoader();
			BorderPane root = loader.load(getClass().getResource("MainWindow.fxml").openStream());
			view = loader.getController();
			
			FXMLLoader detailsLoader = new FXMLLoader();
			Pane detailsPane = detailsLoader.load(getClass().getResource("DetailsWindow.fxml").openStream());
			detailsWindowController = detailsLoader.getController();

			FXMLLoader scoreLoader = new FXMLLoader();
			Pane scorePane = scoreLoader.load(getClass().getResource("ScoreboardWindow.fxml").openStream());
			scoreboardWindowController = scoreLoader.getController();

			view.setDetailsPane(detailsPane);
			detailsWindowController.setScorePane(scorePane);
			
			MyModel model = new MyModel(new Level());
			SokobanController c = new SokobanController(model, view);
			DBController dbc = new DBController(model, detailsWindowController, scoreboardWindowController);

			view.setKeyMap(c.mapKeyCodes(keys));

			model.addObserver(c);
			model.addObserver(dbc);
			view.addObserver(c);
			detailsWindowController.addObserver(dbc);
			scoreboardWindowController.addObserver(dbc);

			Scene scene = new Scene(root, 600, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			scoreboardWindowController.setScene(scene); //set the main scene to later return to it
			
			primaryStage.setScene(scene);
			view.setStage(primaryStage);
			detailsWindowController.setPrimaryStage(primaryStage);
			scoreboardWindowController.setPrimaryStage(primaryStage);
			primaryStage.show();
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		view.exitGame();
	}

	public static void main(String[] args) {
		if (args.length != 0) {
			if (args[0].equals("-server")) {
				MyModel model = new MyModel(new Level());
				Handler handler = new Handler();
				SokobanController c = new SokobanController(model, handler);
				model.addObserver(c);
				handler.addObserver(c);
				try {
					c.startServer(Integer.parseInt(args[1]));
//					c.setView((View) c.getServer().getCh());
//					c.getServer().getCh().addObserver(c);
					c.setServerOn();
					System.out.println("Server is waiting for clients...");
				} catch (Exception e) {
					// parsing exception
					e.printStackTrace();
				}
			} else
				System.out.println("Check args and run again!");
		} else
			launch(args);
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
}
