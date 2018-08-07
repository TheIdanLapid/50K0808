package model.policy;


import java.util.ArrayList;
import java.util.ListIterator;

import commons.Level;
import model.data.Box;
import model.data.Item;
import model.data.MovableItem;
import model.data.Position;

/**
 * The policy for our game.
 * @author שדמה
 *
 */
public class MySokobanPolicy implements Policy{
	
	private Level l;
	private Position p;

	public MySokobanPolicy(Level l) 
	{
		this.l=l;
		this.p = new Position();
	}
	
	//checks if move is valid
	@Override
	public boolean canMove(MovableItem mi, Position p)
	{
		
		this.p.setX(p.getX());
		this.p.setY(p.getY());
		
		ListIterator<Box> iterBoxes = l.getBoxes().listIterator();
		
		while (iterBoxes.hasNext())
		{
			Box temp = iterBoxes.next();
			if (temp.getPosition().equals(p))
				return false;
		}
		
		ListIterator<ArrayList<Item>> iterCol = l.getBoard().listIterator();

		while (iterCol.hasNext())
		{		
			ListIterator<Item> iterLine = iterCol.next().listIterator();
			while (iterLine.hasNext())
			{
				Item temp = iterLine.next();
				if (temp.getName().equals("Wall"))
					if (temp.getPosition().equals(p))
						return false;
			}
		}
			
		return true;
	}
	
	//gets and sets
	public Position getP() {
		return p;
	}

	public void setP(Position p) {
		this.p = p;
	}	

	public Level getL() {
		return l;
	}

	public void setL(Level l) {
		this.l = l;
	}

	
	
}
