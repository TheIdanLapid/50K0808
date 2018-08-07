package model.data;

import java.io.IOException;
import java.io.InputStream;

import commons.Level;

/**
 * Object loader - by serialization.
 * @author שדמה
 *
 */
public class MyObjectLevelLoader implements LevelLoader {

	@Override
	public Level loadLevel(InputStream is) 
	{
	
		Level l = new Level();
		
		try 
		{
			l = (Level) Serialization.readSer(is); //read the object file into l
		}
		
		catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		return l;

	}
}
