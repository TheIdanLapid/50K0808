package searchLib;

import model.data.Position;
/**
 * Holds positions of box and sokoban
 * @author שדמה
 *
 */
public class TwoPositions {
	Position box, sok;

	public TwoPositions(Position box, Position sok) {
		this.box = box;
		this.sok = sok;
	}
	
	public TwoPositions(Position box)
	{
		this.box = box;
		this.sok = null;
	}
	
	public Position getBox() {
		return box;
	}

	public void setBox(Position box) {
		this.box = box;
	}

	public Position getSok() {
		return sok;
	}

	public void setSok(Position sok) {
		this.sok = sok;
	}

	
	@Override
	public String toString() {
		return "Box:"+box.getX()+","+box.getY()+" Sok:"+sok.getX()+","+sok.getY();
	}
	
	@Override
	public boolean equals(Object obj) {
		TwoPositions p = (TwoPositions)obj;
		return (box.equals(p.getBox()));
	}
	
}
