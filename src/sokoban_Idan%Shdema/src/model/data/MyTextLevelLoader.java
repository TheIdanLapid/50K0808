package model.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import commons.Level;

/**
 * Text loader - creates a level, and fills arraylists with items.
 * @author שדמה
 *
 */
public class MyTextLevelLoader implements LevelLoader{


	private Level l = new Level();
		
	@Override
	public Level loadLevel(InputStream is) {
		
		int x; //the marker that reads characters from the txt
		int i=0,j=0; //coordinates for positions
		ArrayList<Item> line = new ArrayList<Item>(); //each arraylist is a line of text
		l.getBoard().add(line);
		try {
			while((x=is.read())!=-1)
			{
				
				if(x!=(int)'\n')
				{

						if (x==(int)'#')//wall
						{
							l.getBoard().get(i).add(new Wall(i,j));
						}
											
						else if (x==(int)'A')//char
						{
							l.getChars().add(new Character(i,j)); 
						}
						
						else if (x==(int)'@')//box
							l.getBoxes().add(new Box(i,j)); 
						
						else if (x==(int)'o')//star
						{
							l.getBoard().get(i).add(new Star(i,j));
							l.setNumOfStars(l.getNumOfStars() + 1);
						}
						
						else if (x==(int)'$')//character on star
						{
							l.getChars().add(new Character(i,j));
							l.getBoard().get(i).add(new Star(i,j));
						}
						
						else if (x==(int)'%')//box on star
						{
							l.getBoxes().add(new Box(i,j));
							l.getBoard().get(i).add(new Star(i,j));
						}
						j++;
						l.setBoundRow(Math.max(l.getBoundRow(), j)); //init the number of columns to the max
					}
				
					else
					{
						i++; //new line
						j=0; //reset the column index
						line = new ArrayList<Item>();
						l.getBoard().add(line); //add a new arraylist to board
					}
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
		l.setBoundCol(i+1); //init the number of rows to the max


		return l;
	}
	
	//get and set l
	public Level getL() {
		return l;
	}

	public void setL(Level l) {
		this.l = l;
	}

}
