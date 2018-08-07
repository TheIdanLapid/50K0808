package model.data;

import java.io.InputStream;

import commons.Level;

/**
 * A. The data creators are the classes that implements 'LevelLoader' - which are separated from 'Level', the data representer.
 * <br>B. The separation uses the 'Open/Closed' principle like this:
 * <br>Open - We can add more types of files to load levels from, by creating more classes that will implement 'LevelLoader'
 * <br>Closed - No need to change class 'Level'
 * <br>C. The separation uses 'Liskov Substitution Principle' like this:
 * <br>'LoadLevel' gets a general 'InputStream' as a parameter, and the overriding methods use methods from the abstract class 'InputStream', so they are defined for each specific kind of 'InputStream' (FileInputStream, ObjectInputStream...)
 * <br>D.'InputStream' is more general, and extended by classes reading not necessarily from files. and not bound only to a file, when using "String filename".
 * 
 * <br><br>
 * The interface for the loaders, has method that gets general input stream.
 */
public interface LevelLoader {
	Level loadLevel(InputStream is);
}
