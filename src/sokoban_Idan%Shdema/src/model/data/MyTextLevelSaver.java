package model.data;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.ListIterator;

import commons.Level;

/**
 * Text saver - convert the level to a sign board (char matrix) and saves it.
 * @author שדמה
 *
 */
public class MyTextLevelSaver implements LevelSaver {

	@Override
	public void saveLevel(String fileName, Level l){		
		
		try
		{
			l.setOs(new FileOutputStream(fileName));
			
			//set bounds: longest line in board as num of columns, and num of arraylist as num of rows
			char signBoard[][] = new char[l.getBoundCol()][l.getBoundRow()];
			
			for (int i=0;i<l.getBoundCol();i++)
				for (int j=0;j<l.getBoundRow();j++)
					signBoard[i][j]=' ';
			
			
			//add to signboard only walls and stars from board
			ListIterator<ArrayList<Item>> iterCol = l.getBoard().listIterator();
	
			while (iterCol.hasNext()) //iterate arraylists
			{		
				ListIterator<Item> iterLine = iterCol.next().listIterator(); //iterate items (line)
				while (iterLine.hasNext())
				{
					try
					{
						Item temp = iterLine.next();
						if (temp.getName().equals("Wall"))
							signBoard[temp.getPosition().getX()][temp.getPosition().getY()]='#';
						else if (temp.getName().equals("Star"))
							signBoard[temp.getPosition().getX()][temp.getPosition().getY()]='o';
					}
					catch (Exception e)
					{
						System.out.println("Error creating signboard "+e);
					}
				}
			}
			
			//add to the signboard only characters and characters on stars
			ListIterator<Character> iterChars = l.getChars().listIterator();
			
			while (iterChars.hasNext())
			{
				Character temp = iterChars.next();//iterate characters
				if (signBoard[temp.getPosition().getX()][temp.getPosition().getY()]=='o')//if there is a star
					signBoard[temp.getPosition().getX()][temp.getPosition().getY()]='$';//char on star
				else
					signBoard[temp.getPosition().getX()][temp.getPosition().getY()]='A';//just char
			}
			
			ListIterator<Box> iterBoxes = l.getBoxes().listIterator();
			
			while (iterBoxes.hasNext())
			{
				Box temp = iterBoxes.next();//iterate boxes
				if (signBoard[temp.getPosition().getX()][temp.getPosition().getY()]=='o')//if there is a star
					signBoard[temp.getPosition().getX()][temp.getPosition().getY()]='%';//box on star
				else
					signBoard[temp.getPosition().getX()][temp.getPosition().getY()]='@';//just box
			}
			
			//now we'll write the signboard into the txt
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(l.getOs()));
			
			for (int i = 0; i < l.getBoundCol()-1; i++) 
			{
				bw.write(signBoard[i]);//write line i of signs
				bw.newLine();
			}
			bw.write(signBoard[l.getBoundCol()-1]);//write last line
			bw.close();
		}
		
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
