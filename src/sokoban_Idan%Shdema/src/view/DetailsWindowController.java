package view;

import java.net.URL;
import java.util.LinkedList;
import java.util.Observable;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
/**
 * The window controller for the 'enter details' window
 * @author שדמה
 *
 */
public class DetailsWindowController extends Observable implements Initializable, WindowView{

	private Pane scorePane;
	private Stage primaryStage;
	
	@FXML
	TextField fName;
	
	@FXML
	TextField lName;
	
	@FXML
	TextField Iduser;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
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
		switchScene();

	}

	public void setScorePane(Pane scorePane) {
		this.scorePane=scorePane;		
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	@Override
	public void switchScene() {
		Platform.runLater(new Runnable() { // move to JavaFX thread

			@Override
			public void run() {
				if(scorePane.getScene() != null)
					scorePane.getScene().setRoot(new Region());
				Scene scene = new Scene(scorePane, 600, 600);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				primaryStage.setScene(scene);
				primaryStage.show();
			}
		});
	}
	

}
