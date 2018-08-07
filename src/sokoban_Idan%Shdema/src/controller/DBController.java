package controller;

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import model.Model;
import view.DetailsWindowController;
import view.ScoreboardWindowController;

/**
 * The controller for the DB views
 * @author שדמה
 *
 */
public class DBController implements Observer {
	private Model model; // reference facade to model
	private DetailsWindowController detailsView;
	private ScoreboardWindowController scoreView; 

	public DBController(Model model, DetailsWindowController details, ScoreboardWindowController score) {
		this.model = model;
		this.detailsView = details;
		this.scoreView = score;
	}

	
	@Override
	public void update(Observable obs, Object arg) {
		@SuppressWarnings("unchecked")
		LinkedList<String> params = (LinkedList<String>) arg; // O(1) solution
		if (obs == model) {
			
			if(params.get(0).equals("scoreList"))
			{
					
				scoreView.setScoreList(model.getScoreList());
				scoreView.updateTable();
			}
		}

		else if (obs == detailsView) { // if view updates the controller
				if ((params.get(0).equals("Details"))) // got user details from view
				{
					model.userDetails(params); // pass model the user details
					model.levelDetails(model.getLevel());
					scoreView.setScoreList(model.getScoreList());
				}
		}
		
		else if (obs == scoreView)
		{
			if(params.get(0).equals("topScoresUser"))
			{
				scoreView.setScoreList(model.getScoreListUser(params.get(1)));
			}
			
			else if(params.get(0).equals("topScoresLevel"))
			{
				scoreView.setScoreList(model.getScoreListLevel(params.get(1)));
			}
			
			else if (params.get(0).equals("mainScores"))
			{
				scoreView.setScoreList(model.getScoreList());
			}
		}

	}
}
