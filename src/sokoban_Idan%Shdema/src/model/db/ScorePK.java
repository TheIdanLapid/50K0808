package model.db;

import java.io.Serializable;

import javax.persistence.Embeddable;
/**
 * The primary key for a score made by a user for a specific level.
 * @author שדמה
 *
 */
@Embeddable
public class ScorePK implements Serializable {

	private int UserID;
	private String LevelID;

	public ScorePK() {
	}

	public ScorePK(int userID, String levelID) {
		UserID = userID;
		LevelID = levelID;
	}
	
//	public ScorePK(ScorePK s)
//	{
//		this.UserID = s.getUserID();
//		this.LevelID = s.getLevelID();
//	}

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		UserID = userID;
	}

	public String getLevelID() {
		return LevelID;
	}

	public void setLevelID(String levelID) {
		LevelID = levelID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + LevelID.hashCode();
		result = prime * result + UserID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScorePK other = (ScorePK) obj;
		if (LevelID != other.LevelID)
			return false;
		if (UserID != other.UserID)
			return false;
		return true;
	}

}
