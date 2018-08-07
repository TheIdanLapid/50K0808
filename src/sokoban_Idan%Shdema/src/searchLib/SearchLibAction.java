package searchLib;
/**
 * A simple class to hold the string "Move ..." (... = direction)
 * @author שדמה
 *
 */
public class SearchLibAction {
	private String name;

	public SearchLibAction() {
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public SearchLibAction(String name){
		this.name = name;
	}

	 @Override
	public boolean equals(Object obj) {
		 SearchLibAction a = (SearchLibAction)obj;
		 return a.name.equals(name);
	}
	 
	 @Override
	public int hashCode() {
		return name.hashCode();
	}
}
