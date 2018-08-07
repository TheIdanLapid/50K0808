package model.data;

import java.io.IOException;

import commons.Level;

/**
 * XML saver - by serialization.
 * @author שדמה
 *
 */
public class MyXMLLevelSaver implements LevelSaver {

	@Override
	public void saveLevel(String fileName, Level l) {

			try {
				Serialization.writeSer(l, fileName); //write the level into obj file
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

}
