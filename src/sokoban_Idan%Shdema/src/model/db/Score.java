package model.db;

import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
/**
 * Holds steps and time, and a primary key of class ScorePK
 * @author שדמה
 *
 */
@Entity(name = "Scoreboard")
public class Score {

	@EmbeddedId
	private ScorePK key;
	@Column(name = "Steps")
	private int steps;
	@Column(name = "Time")
	private Time time;

	public Score() {
		this.key= new ScorePK();
	}

	public Score(int UserID, String LevelID, int steps, Time time) {
		this.key = new ScorePK(UserID, LevelID);
		this.steps = steps;
		this.time = time;
	}
	
	public Score(Score score) {
		this.key = score.getKey();
		this.steps = score.getSteps();
		this.time = score.getTime();
	}

	public ScorePK getKey() {
		return key;
	}

	public void setKey(ScorePK key) {
		this.key = key;
	}

	public int getUserID() {
		return key.getUserID();
	}
	
	public String getLevelID() {
		return key.getLevelID();
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

}
