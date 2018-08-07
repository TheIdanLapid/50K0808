package controller.commands;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import commons.Level;
import model.Model;

/**
 * Command that load specific file by extension with Factory pattern
 * 
 * @author שדמה
 *
 */
public class LoadFileName extends SokobanCommand {

	private String fileName;
	private Level l;
	private Model model;

	LoadFileName(String fn, Level l, Model model)

	{
		super();
		this.l = l;
		fileName = fn;
		this.model = model;

		try {
			l.setIs(new FileInputStream(fileName)); // sets 'is' in level class
			l.setName(fileName.substring(0, fileName.indexOf('.')));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void execute() {
		model.load(fileName);
		done = true;
	}

	// gets and sets
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Level getL() {
		return l;
	}

	public void setL(Level l) {
		this.l = l;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

}
