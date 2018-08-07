package stripsLib;
/**
 * A class that holds:
 * 	string type - statement
 * 	string id - for box and target
 *  string value - the position in string
 *  
 *   satisfies - checks if the argument (predicate) is the same predicate
 *   contradicts - checks if the argument (predicate) has the same type and id but different value(position)
 * @author שדמה
 *
 */
public class Predicate {
	
	String type,id,value;
	
	public Predicate(String type, String id, String value) {
		super();
		this.type = type;
		this.id = id;
		this.value = value;
	}
	
	public boolean satisfies(Predicate p){
		return (type.equals(p.type) && (id.equals(p.id) || p.id.equals("?")) && value.equals(p.value));
	}

	public boolean contradicts(Predicate p) {		
		return (type.equals(p.type) && id.equals(p.id) && !value.equals(p.value));
	}
	
	@Override
	public int hashCode(){
		return (type + id + value).hashCode();
	}
	
	@Override
	public String toString(){
		return type + "_" + id + "=" + value;
	}
	
	public boolean equals(Predicate p){
		return (type.equals(p.type) && id.equals(p.id) && value.equals(p.value));
	}

	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}

	public String getId() {
		return id;
	}


}