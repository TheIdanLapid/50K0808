package model.data;

import java.util.HashMap;

/**
 * The Factory that creates savers - by extensions.
 * @author שדמה
 *
 */
public class SaverFactory {
	
	private HashMap<String,Creator> saverCreator;
	
	public SaverFactory() {
		saverCreator = new HashMap<String, Creator>();
		saverCreator.put("txt", new TextCreator());
		saverCreator.put("obj", new ObjectCreator());
		saverCreator.put("xml", new XMLCreator());
	}

	public LevelSaver createSaver(String fileName)
	{
		//we'll extract the file name from the command "Save filename.*"
		String ext = "";
		int i = fileName.lastIndexOf('.');
		if(i>0){
			ext = fileName.substring(i+1);
		}
		
		Creator c = saverCreator.get(ext);
		
		if (c!=null)
			return c.create();

		return null; //also if the extension is not recognized
	}
	
	private interface Creator{
		public LevelSaver create();
	}
	
	private class TextCreator implements Creator {
		public LevelSaver create() {
			return new MyTextLevelSaver();
		}
	}
	
	private class ObjectCreator implements Creator {
		public LevelSaver create() {
			return new MyObjectLevelSaver();
		}
	}
	
	private class XMLCreator implements Creator {
		public LevelSaver create() {
			return new MyXMLLevelSaver();
		}
	}
	
	//gets and sets
	public HashMap<String, Creator> getSaverCreator() {
		return saverCreator;
	}

	public void setSaverCreator(HashMap<String, Creator> saverCreator) {
		this.saverCreator = saverCreator;
	}

}
