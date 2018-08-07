import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

import commons.Level;
import commons.SignBoard;
import model.data.LevelLoader;
import model.data.LoaderFactory;
import sokobanSolver.SokobanPlannableAdapter;
import stripsLib.Action;
import stripsLib.Plannable;
import stripsLib.Planner;
import stripsLib.Strips;

public class Main {

	public static void main(String[] args) {
		System.out.println("Loading level from: "+args[0]);
		Level l = new Level();
		try {
			l.setIs(new FileInputStream(args[0]));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LoaderFactory lf = new LoaderFactory();
		LevelLoader ll = lf.createLoader(args[0]);
		Level temp = new Level(ll.loadLevel(l.getIs()));
		l.setBoard(temp.getBoard());
		l.setBoundCol(temp.getBoundCol());
		l.setBoundRow(temp.getBoundRow());
		l.setBoxes(temp.getBoxes());
		l.setChars(temp.getChars());
		l.setNumOfStars(temp.getNumOfStars());
		l.setSteps(temp.getSteps());
		l.setMyTimer(temp.getMyTimer());
		
		SignBoard sb = new SignBoard(l);
		char[][] signBoard = sb.getSignBoard();
		
		Planner strips = new Strips();
		Plannable adapter = new SokobanPlannableAdapter(signBoard);
		List<Action> list = new LinkedList<>();
		list = strips.plan(adapter);
		List<String> plan = new LinkedList<>();
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.get(i).getSearchActions().size(); j++) {
				plan.add(list.get(i).getSearchActions().get(j).getName());	
			}
		}
		if (plan.isEmpty())
			System.out.println("Couldn't solve "+args[0]);
		else
		{
			
			OutputStream o = null;
			
			try 
			{	
				o = new FileOutputStream(args[1]);
			
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(o));
				
				for (int i = 0; i < plan.size()-1; i++) 
				{
					bw.write(plan.get(i));
					bw.newLine();
				}
				
				bw.write(plan.get(plan.size()-1));
				bw.close();
				
				System.out.println("Finished!");
			}
		
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
}
