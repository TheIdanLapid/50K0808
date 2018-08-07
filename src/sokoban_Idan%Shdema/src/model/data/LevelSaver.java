package model.data;

import commons.Level;

/**
 * The interface for the savers, has method that gets a filename, a level, and a general output stream to write the level to.
 * @author שדמה
 *
 */
public interface LevelSaver {
	public void saveLevel(String fileName, Level l);
}
