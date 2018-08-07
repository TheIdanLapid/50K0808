package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Observable;

import commons.Level;
import commons.SignBoard;
import model.data.Box;
import model.data.Character;
import model.data.LevelLoader;
import model.data.LevelSaver;
import model.data.LoaderFactory;
import model.data.MovableItem;
import model.data.Position;
import model.data.SaverFactory;
import model.db.Manager;
import model.db.Score;
import model.policy.MySokobanPolicy;

/**
 * Class that is responsible for the business logic for the game. Implements the
 * commands and computes them, checks the policy for moving.
 * 
 * @author Eon
 *
 */
public class MyModel extends Observable implements Model {
	private Level l;
	private char[][] signBoard;
	private boolean loaded, finished, zeroMoves;
	private Manager mng;
	private Socket socket;
	private BufferedReader serverInput;
	private PrintWriter outToServer;
	

	public MyModel(Level l) {
		setLoaded(false);
		zeroMoves = true;
		this.l = l;
		this.signBoard = new SignBoard(l).getSignBoard();
		mng = new Manager();
	}

	private Position moveTo(MovableItem mi, String dir) {

		Position p = new Position(mi.getPosition().getX(), mi.getPosition().getY());
		if (dir.equals("down")) {
			p.setX(mi.getPosition().getX() + 1);
		} else if (dir.equals("up")) {
			p.setX(mi.getPosition().getX() - 1);
		} else if (dir.equals("left")) {
			p.setY(mi.getPosition().getY() - 1);
		} else if (dir.equals("right")) {
			p.setY(mi.getPosition().getY() + 1);
		}

		return p;
	}

	@Override
	public void move(String dir) {
		LinkedList<String> params = new LinkedList<String>();
		if (loaded && (!finished)) {

			ListIterator<Character> iterChars = l.getChars().listIterator();
			Character currentChar = iterChars.next(); // the only character for
														// now

			ListIterator<Box> iterBoxes = l.getBoxes().listIterator();
			boolean found = false; // is there a box to the direction from the
									// character
			while (iterBoxes.hasNext() && !found) {
				Box temp = iterBoxes.next();
				if (moveTo(currentChar, dir).equals(temp.getPosition())) {
					int add = 0;
					found = true;
					MySokobanPolicy msp = new MySokobanPolicy(l);
					if (msp.canMove(temp, moveTo(temp, dir))) // check policy on
																// the box
					{
						if (signBoard[temp.getPosition().getX()][temp.getPosition().getY()] == '%') // box
																									// on
																									// star
						{

							if (signBoard[msp.getP().getX()][msp.getP().getY()] == 'o') // box
																						// to
																						// star
							{
							} else
								add += 1;
						} else {
							if (signBoard[msp.getP().getX()][msp.getP().getY()] == 'o') // box
																						// to
																						// star
								add -= 1;
						}

						temp.move(dir);
						currentChar.move(dir);
						l.setNumOfStars(l.getNumOfStars() + add);
					}
				}
			}
			if (!found) {
				if (new MySokobanPolicy(l).canMove(currentChar, moveTo(currentChar, dir))) // check
																							// policy
																							// on
																							// character
				{
					currentChar.move(dir);
				}
			}

			signBoard = new SignBoard(l).getSignBoard(); //update signboard


			// move completed
			l.setSteps(l.getSteps() + 1);
			zeroMoves=false;
			params.add("Display");
			setChanged();
			notifyObservers(params);
			
			if (l.getNumOfStars() == 0) {
				finished = true;
				LinkedList<String> win = new LinkedList<String>();
				win.add("WIN");
				setChanged();
				notifyObservers(win);
			}
		}

		else // no level currently loaded
		{
			params.add("notLoaded");
			setChanged();
			notifyObservers(params);
		}


	}

	@Override
	public void load(String fileName) {
		if (socket!=null && loaded)
		{
			try {
				outToServer.println("BYE");
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			openSocket();
		}
		
		LinkedList<String> stopAnimation = new LinkedList<String>();
		stopAnimation.add("stopAnim");

		setChanged();
		notifyObservers(stopAnimation);
		
		finished = false;
		LoaderFactory lf = new LoaderFactory();
		LevelLoader ll = lf.createLoader(fileName);

		LinkedList<String> params = new LinkedList<String>();

		if (ll != null) {

			Level temp = new Level(ll.loadLevel(l.getIs())); // we set every
																// member of
																// level
			l.setBoard(temp.getBoard());
			l.setBoundCol(temp.getBoundCol());
			l.setBoundRow(temp.getBoundRow());
			l.setBoxes(temp.getBoxes());
			l.setChars(temp.getChars());
			l.setNumOfStars(temp.getNumOfStars());
			l.setSteps(temp.getSteps());
			l.setMyTimer(temp.getMyTimer());

			// loading is complete
			setLoaded(true);
			params.add("Load");
			signBoard = new SignBoard(l).getSignBoard();
		}

		else // no level currently loaded
			params.add("notLoaded");

		setChanged();
		notifyObservers(params);

	}

	@Override
	public void save(String fileName) {
		LinkedList<String> params = new LinkedList<String>();

		if (loaded) {
			SaverFactory lf = new SaverFactory();
			LevelSaver ll = lf.createSaver(fileName);
			ll.saveLevel(fileName, l);
			params.add("Saved");
		}

		else {

			params.add("notLoaded");
		}

		setChanged();
		notifyObservers(params);

	}

	@Override
	public void exit(Level l) {
		LinkedList<String> params = new LinkedList<String>();
		if (loaded) {

			if (l.getIs() != null) {
				try {
					l.getIs().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (l.getOs() != null) {
				try {
					l.getOs().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (socket!=null)
			try {
				outToServer.println("BYE");
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		params.add("Exit");
		setChanged();
		notifyObservers(params);

	}

	@Override
	public void userDetails(LinkedList<String> params) {
		mng.addUser(params.get(1), params.get(2), Integer.parseInt(params.get(3)));
	}
	
	@Override
	public void levelDetails(Level level) {
		mng.addLevel(level);
		mng.addScore(mng.getUser(), level);
	}
	
	@Override
	public Level getLevel() {
		return l;
	}

	public Level getL() {
		return l;
	}

	public void setL(Level l) {
		this.l = l;
	}

	public char[][] getSignBoard() {
		return signBoard;
	}

	public void setSignBoard(char[][] signBoard) {
		this.signBoard = signBoard;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	@Override
	public void topScoresUser(String filter) {
		LinkedList<String> params = new LinkedList<String>();
		params.add("scoreListUser");
		params.add(filter);
		setChanged();
		notifyObservers(params);
	}
	
	@Override
	public void topScoresLevel(String filter) {
		LinkedList<String> params = new LinkedList<String>();
		params.add("scoreListLevel");
		params.add(filter);
		setChanged();
		notifyObservers(params);
	}

	@Override
	public LinkedList<Score> getScoreList() {
		return mng.pullData();
	}

	@Override
	public LinkedList<Score> getScoreListUser(String string) {
		return mng.pullDataUser(string);
	}

	@Override
	public LinkedList<Score> getScoreListLevel(String string) {
		return mng.pullDataLevel(string);
	}
	
	public void openSocket() {
		try {
			socket = new Socket("localhost", 12345);
			serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outToServer = new PrintWriter(socket.getOutputStream());
    	} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void requestHintFromServer()
	{
		if (loaded && !finished && zeroMoves)
		{
			outToServer.println("Hint");
			outToServer.flush();
			outToServer.println(l.getName());
			outToServer.flush();
			for (int i = 0; i < signBoard.length; i++) {
				outToServer.println(signBoard[i]);
				outToServer.flush();
			}
			outToServer.println("Solve");
			outToServer.flush();
		}
	}
	
	public void helpFromServer(String hintOrSol)
	{
		if (loaded && !finished)
		{
			if (zeroMoves)
			{
				outToServer.println("zeroMoves");
				outToServer.flush();
			}
			else
			{
				outToServer.println("notZeroMoves");
				outToServer.flush();
			}
			if (hintOrSol.equals("hint"))
			{
				outToServer.println("Hint");
				outToServer.flush();
			}
			else if (hintOrSol.equals("solution"))
			{
				outToServer.println("Sol");
				outToServer.flush();
			}
			outToServer.println(l.getName());
			outToServer.flush();
			outToServer.println(signBoard.length);
			outToServer.flush();
			outToServer.println(signBoard[0].length);
			outToServer.flush();
			for (int i = 0; i < signBoard.length; i++)
			{
				StringBuilder str = new StringBuilder();
				for(int j = 0; j < signBoard[i].length;j++)
				{
					str.append(signBoard[i][j]);
				}
				outToServer.println(str.toString());
				outToServer.flush();
			}
			
			if (hintOrSol.equals("hint"))
			{
				String solution = "";
				try {
					solution = serverInput.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (solution.equals(""))
				{
					List<String> params = new LinkedList<String>();
					params.add("cannotSolve");
					setChanged();
					notifyObservers(params);
				}
				else
				{
					List<String> params = new LinkedList<String>();
					params.add("Animation");
					params.add(solution);
					params.add(hintOrSol);
					setChanged();
					notifyObservers(params);
				}
			}
			else if (hintOrSol.equals("solution"))
			{
				String solution = "";
				try {
					solution = serverInput.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (solution.equals(""))
				{
					List<String> params = new LinkedList<String>();
					params.add("cannotSolve");
					setChanged();
					notifyObservers(params);
				}
				else
				{
					finished = false;
					List<String> params = new LinkedList<String>();
					params.add("Animation");
					params.add(solution);
					params.add(hintOrSol);
					setChanged();
					notifyObservers(params);
				}
			}			
		}
	}

	@Override
	public void sayBye() {
		outToServer.println("BYE");
	}


}
