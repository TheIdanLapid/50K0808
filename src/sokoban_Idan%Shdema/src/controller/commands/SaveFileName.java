package controller.commands;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import commons.Level;
import model.Model;

/**
 * Command that saves specific file by extension with Factory pattern
 * @author שדמה
 *
 */
public class SaveFileName extends SokobanCommand {

	private String fileName;
	private Level l;
	private Model model;
	
	
	SaveFileName(String fn, Level l, Model model) {
		
		super();
		fileName=fn;
		this.l = l;
		this.model = model;
		
		try {
			l.setOs(new FileOutputStream(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void execute() {
		model.save(fileName);
		done = true;
	}
	
	
	//gets and sets
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
