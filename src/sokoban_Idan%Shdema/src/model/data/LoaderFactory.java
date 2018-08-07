package model.data;

import java.util.HashMap;

/**
 * The Factory that creates loaders - by extensions.
 * @author שדמה
 *
 */
public class LoaderFactory {
	
	private HashMap<String,Creator> loaderCreator;
	
	public LoaderFactory() {
		loaderCreator = new HashMap<String, Creator>();
		loaderCreator.put("txt", new TextCreator());
		loaderCreator.put("obj", new ObjectCreator());
		loaderCreator.put("xml", new XMLCreator());
	}

	public LevelLoader createLoader(String fileName)
	{
		
		String ext = "";
		int i = fileName.lastIndexOf('.');
		if(i>0){
			ext = fileName.substring(i+1);
		}
		
		Creator c = loaderCreator.get(ext);
		
		if (c!=null)
			return c.create();

		return null; //also if the extension is not recognized
	}
	
	private interface Creator{
		public LevelLoader create();
	}
	
	private class TextCreator implements Creator {
		public LevelLoader create() {
			return new MyTextLevelLoader();
		}
	}
	
	private class ObjectCreator implements Creator {
		public LevelLoader create() {
			return new MyObjectLevelLoader();
		}
	}
	
	private class XMLCreator implements Creator {
		public LevelLoader create() {
			return new MyXMLLevelLoader();
		}
	}

	//get and set
	public HashMap<String, Creator> getLoaderCreator() {
		return loaderCreator;
	}

	public void setLoaderCreator(HashMap<String, Creator> loaderCreator) {
		this.loaderCreator = loaderCreator;
	}
	
	

}
