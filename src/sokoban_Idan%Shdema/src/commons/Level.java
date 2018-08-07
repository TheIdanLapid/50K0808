package commons;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import model.data.Box;
import model.data.Character;
import model.data.Item;
import model.data.MyTimer;
import model.db.Score;

/**
 * Level class, has a board that is array list of array lists, array list of
 * chars and array list of boxes. Has a timer, steps and number of stars left to
 * cover. Has I/O streams.
 *
 */

@Entity(name = "Levels")
public class Level implements Serializable {

	@Id
	private String name;

	@OneToMany
	@JoinColumn(name = "Name")
	private List<Score> users;

	// data members
	@Transient
	private ArrayList<ArrayList<Item>> board;
	@Transient
	private ArrayList<Character> chars;
	@Transient
	private ArrayList<Box> boxes;

	@Transient
	private int boundRow;// number of columns
	@Transient
	private int boundCol;// number of rows

	@Transient
	private MyTimer timer;
	@Transient
	private int steps;
	@Transient
	private int numOfStars;// counts the number of uncovered stars

	@Transient
	private transient InputStream is;
	@Transient
	private transient OutputStream os;

	// c'tors
	public Level() {
		board = new ArrayList<ArrayList<Item>>();
		chars = new ArrayList<Character>();
		boxes = new ArrayList<Box>();
		timer = new MyTimer();
		steps = 0;
		boundRow = 0;
		boundCol = 0;
		numOfStars = 0;
		is = null;
		os = null;
	}

	public Level(Level l) // copy c'tor
	{
		this.board = l.board;
		this.chars = l.chars;
		this.boxes = l.boxes;
		this.timer = l.timer;
		this.steps = l.steps;
		this.boundRow = l.boundRow;
		this.boundCol = l.boundCol;
		this.numOfStars = l.numOfStars;
		this.is = l.is;
		this.os = l.os;
	}

	// gets and sets
	public ArrayList<ArrayList<Item>> getBoard() {
		return board;
	}

	// not for use - just for serial
	public void setBoard(ArrayList<ArrayList<Item>> board) {
		this.board = board;
	}

	public ArrayList<Character> getChars() {
		return chars;
	}

	public void setChars(ArrayList<Character> chars) {
		this.chars = chars;
	}

	public ArrayList<Box> getBoxes() {
		return boxes;
	}

	public void setBoxes(ArrayList<Box> boxes) {
		this.boxes = boxes;
	}

	public MyTimer getMyTimer() {
		return timer;
	}

	public void setMyTimer(MyTimer timer) {
		this.timer = timer;
	}

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public int getBoundRow() {
		return boundRow;
	}

	public void setBoundRow(int boundRow) {
		this.boundRow = boundRow;
	}

	public int getBoundCol() {
		return boundCol;
	}

	public void setBoundCol(int boundCol) {
		this.boundCol = boundCol;
	}

	public int getNumOfStars() {
		return numOfStars;
	}

	public void setNumOfStars(int numOfStars) {
		this.numOfStars = numOfStars;
	}

	public InputStream getIs() {
		return is;
	}

	public void setIs(InputStream is) {
		this.is = is;
	}

	public OutputStream getOs() {
		return os;
	}

	public void setOs(OutputStream os) {
		this.os = os;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public List<Score> getUsers() {
		return users;
	}

	public void setUsers(List<Score> users) {
		this.users = users;
	}

	public MyTimer getTimer() {
		return timer;
	}

	public void setTimer(MyTimer timer) {
		this.timer = timer;
	}

}
