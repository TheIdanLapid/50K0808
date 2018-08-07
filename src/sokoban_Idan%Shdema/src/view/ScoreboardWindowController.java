package view;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.db.Score;
/**
 * The window controller for the scoreboard. Has a table with the scores.
 * @author שדמה
 *
 */
public class ScoreboardWindowController extends Observable implements Initializable, WindowView{

	private Stage primaryStage;
	private LinkedList<Score> scoreList;
	private ObservableList<Score> obsList;
	private Scene mainScene;

	
	@FXML
	TextField userID;
	
	@FXML
	TextField levelName;
	
	@FXML
	TableView<Score> table;
	
	@FXML
	TableColumn<Score, Integer> user;
	
	@FXML
	TableColumn<Score, String> level;
	
	@FXML
	TableColumn<Score, Integer> steps;
	
	@FXML
	TableColumn<Score, Integer> time;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		obsList = FXCollections.observableArrayList(); //init the observable list that will hold the scores
		
		//open the user's scoreboard when you click on his name
		table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		
		table.getSelectionModel().clearSelection();
		table.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener<Integer>() {

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Integer> c) {
				if (c.getList()!=null && !c.getList().isEmpty())
				{
					Platform.runLater(new Runnable() { // move to JavaFX thread
						@Override
						public void run() {
							userID.setText(user.getCellData(c.getList().get(0)).toString());
							searchID();
						}
					});
				}
			}

		});
	}
	
	public void backToGame()
	{
		switchScene();
	}
	
	public void mainScoreboard()
	{
		LinkedList<String> params = new LinkedList<String>();
		params.add("mainScores");
		setChanged();
		notifyObservers(params);
	}
	
	public void searchID()
	{
		if (!userID.getText().equals(""))
		{	
			LinkedList<String> params = new LinkedList<String>();
			params.add("topScoresUser");
			params.add(userID.getText());
			setChanged();
			notifyObservers(params);
		}
	}
	
	public void searchLevel()
	{
		if (!levelName.getText().equals(""))
		{	
			LinkedList<String> params = new LinkedList<String>();
			params.add("topScoresLevel");
			params.add(levelName.getText());
			setChanged();
			notifyObservers(params);
		}
	}

	public void setScoreList(LinkedList<Score> arg) {
		obsList.clear();
		scoreList = new LinkedList<Score>();
		scoreList = arg;
		if (scoreList!=null)
		{
			for (int i = 0; i < scoreList.size(); i++) {
				obsList.add(scoreList.get(i));
			}
		}
		updateTable();
	}
	
	
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void updateTable() {
		user.setCellValueFactory(new PropertyValueFactory<Score, Integer>("UserID"));
		level.setCellValueFactory(new PropertyValueFactory<Score, String>("LevelID"));
		steps.setCellValueFactory(new PropertyValueFactory<Score, Integer>("Steps"));
		time.setCellValueFactory(new PropertyValueFactory<Score, Integer>("Time"));
		table.setItems(obsList);
		List<TableColumn<Score, ?>> sortOrder = new ArrayList<>(table.getSortOrder());
		table.getSortOrder().clear();
		table.getSortOrder().addAll(sortOrder);
	}

	public void setScene(Scene scene)
	{
		mainScene = scene;
	}

	@Override
	public void switchScene() {
		Platform.runLater(new Runnable() { // move to JavaFX thread

			@Override
			public void run() {
				primaryStage.setScene(mainScene);
				primaryStage.show();
			}
		});
	}
	
}
