package model.data;

import java.io.Serializable;

/**
 * Item in the game.
 * @author שדמה
 *
 */
public class Item implements Serializable{
	private String name;
	private Position position;
	
	Item()
	{
		name = "";
		position=new Position();
	}
	
	Item(int x,int y,String name1){
		position = new Position(x,y);
		name=name1;
	}
	
	//sets and gets
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Position getPosition() {
		return new Position(position);
	}
	public void setPosition(Position p) {
		this.position = p;
	}
	

}
