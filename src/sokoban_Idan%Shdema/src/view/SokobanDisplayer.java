package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Our 'canvas' - loads the pictures for the objects and responsible for drawing
 * the level.
 * 
 * @author Eon
 *
 */
public class SokobanDisplayer extends Canvas {

	private char[][] sokobanData;

	private StringProperty wallFileName;
	private StringProperty charFileName;
	private StringProperty boxFileName;
	private StringProperty starFileName;
	private StringProperty boxOnStarFileName;
	private StringProperty charOnStarFileName;
	// animation
	private StringProperty bobRightFileName;
	private StringProperty bobLeftFileName;

	public SokobanDisplayer() {
		wallFileName = new SimpleStringProperty();
		charFileName = new SimpleStringProperty();
		boxFileName = new SimpleStringProperty();
		starFileName = new SimpleStringProperty();
		boxOnStarFileName = new SimpleStringProperty();
		charOnStarFileName = new SimpleStringProperty();
		// animation
		bobRightFileName = new SimpleStringProperty();
		bobLeftFileName = new SimpleStringProperty();
	}

	public void setSokobanData(char[][] sokobanData) {
		this.sokobanData = sokobanData;
		redraw();
	}

	public void redraw() {
		if (sokobanData != null) {
			double W = getWidth();
			double H = getHeight();
			double w = W / sokobanData[0].length;
			double h = H / sokobanData.length;

			GraphicsContext gc = getGraphicsContext2D();
			Image wall = null;
			Image character = null;
			Image box = null;
			Image star = null;
			Image charOnStar = null;
			Image boxOnStar = null;

			try {
				wall = new Image(new FileInputStream(wallFileName.get()));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				character = new Image(new FileInputStream(charFileName.get()));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				box = new Image(new FileInputStream(boxFileName.get()));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				star = new Image(new FileInputStream(starFileName.get()));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				boxOnStar = new Image(new FileInputStream(boxOnStarFileName.get()));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				charOnStar = new Image(new FileInputStream(charOnStarFileName.get()));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			gc.clearRect(0, 0, W, H);
			for (int i = 0; i < sokobanData.length; i++) {
				for (int j = 0; j < sokobanData[i].length; j++) {
					switch (sokobanData[i][j]) {

					case '#':
						gc.drawImage(wall, j * w, i * h, w, h);
						break;
					case 'A':
						gc.drawImage(character, j * w, i * h, w, h);
						break;
					case '@':
						gc.drawImage(box, j * w, i * h, w, h);
						break;
					case 'o':
						gc.drawImage(star, j * w, i * h, w, h);
						break;
					case '$':
						gc.drawImage(charOnStar, j * w, i * h, w, h);
						break;
					case '%':
						gc.drawImage(boxOnStar, j * w, i * h, w, h);
						break;
					default:
						break;
					}
				}
			}
		}
	}

	// sets and gets
	public String getWallFileName() {
		return wallFileName.get();
	}

	public void setWallFileName(String wallFileName) {
		this.wallFileName.set(wallFileName);
	}

	public String getCharFileName() {
		return charFileName.get();
	}

	public void setCharFileName(String charFileName) {
		this.charFileName.set(charFileName);
	}

	public String getBoxFileName() {
		return boxFileName.get();
	}

	public void setBoxFileName(String boxFileName) {
		this.boxFileName.set(boxFileName);
	}

	public String getStarFileName() {
		return starFileName.get();
	}

	public void setStarFileName(String starFileName) {
		this.starFileName.set(starFileName);
	}

	public String getCharOnStarFileName() {
		return charOnStarFileName.get();
	}

	public void setCharOnStarFileName(String charOnStarFileName) {
		this.charOnStarFileName.set(charOnStarFileName);
	}

	public String getBoxOnStarFileName() {
		return boxOnStarFileName.get();
	}

	public void setBoxOnStarFileName(String boxOnStarFileName) {
		this.boxOnStarFileName.set(boxOnStarFileName);
	}

	public String getBobRightFileName() {
		return bobRightFileName.get();
	}

	public void setBobRightFileName(String bobRightFileName) {
		this.bobRightFileName.set(bobRightFileName);
	}

	public String getBobLeftFileName() {
		return bobLeftFileName.get();
	}

	public void setBobLeftFileName(String bobLeftFileName) {
		this.bobLeftFileName.set(bobLeftFileName);
	}

}
