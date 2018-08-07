package commons;

import java.util.ArrayList;
import java.util.ListIterator;

import model.data.Box;
import model.data.Character;
import model.data.Item;
/**
 * The 2d array that holds all the symbols for objects in the level.
 * @author Eon
 *
 */
public class SignBoard {
	private char[][] signBoard;
	
	public SignBoard(Level l){
	//create a 2d array with object signs and return it
		signBoard = new char[l.getBoundCol()][l.getBoundRow()];
		for (int i=0;i<l.getBoundCol();i++)
			for (int j=0;j<l.getBoundRow();j++)
				signBoard[i][j]=' ';

		//add to the signboard only the walls and stars from board
		ListIterator<ArrayList<Item>> iterCol = l.getBoard().listIterator();

		while (iterCol.hasNext())
		{		
			ListIterator<Item> iterLine = iterCol.next().listIterator();
			while (iterLine.hasNext())
			{
				Item temp = iterLine.next();
				if (temp.getName().equals("Wall"))
					signBoard[temp.getPosition().getX()][temp.getPosition().getY()]='#';
				else if (temp.getName().equals("Star"))
					signBoard[temp.getPosition().getX()][temp.getPosition().getY()]='o';
			}
		}
		
		//add to the signboard only characters and characters on stars
		ListIterator<Character> iterChars = l.getChars().listIterator();
		
		while (iterChars.hasNext())
		{
			Character temp = iterChars.next();
			if (signBoard[temp.getPosition().getX()][temp.getPosition().getY()]=='o')//star
				signBoard[temp.getPosition().getX()][temp.getPosition().getY()]='$';//char on star
			else
				signBoard[temp.getPosition().getX()][temp.getPosition().getY()]='A';//char
		}
		
		//add to the signboard only boxes and boxes on stars
		ListIterator<Box> iterBoxes = l.getBoxes().listIterator();
		
		while (iterBoxes.hasNext())
		{
			Box temp = iterBoxes.next();
			if (signBoard[temp.getPosition().getX()][temp.getPosition().getY()]=='o')//star
				signBoard[temp.getPosition().getX()][temp.getPosition().getY()]='%';//box on star
			else
				signBoard[temp.getPosition().getX()][temp.getPosition().getY()]='@';//box
		}
	}

	public char[][] getSignBoard() {
		return signBoard;
	}

	public void setSignBoard(char[][] signBoard) {
		this.signBoard = signBoard;
	}	
}



