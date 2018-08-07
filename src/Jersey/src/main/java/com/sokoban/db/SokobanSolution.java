/**
 * 
 */
package com.sokoban.db;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author שדמה
 * The entity for the solutions table in the DB
 */
@Entity(name = "SokobanSolutions")
public class SokobanSolution {
	
	@Id
	private String name;
	private String solution;
	
	public SokobanSolution() {}
	
	public SokobanSolution(String name, String solution) {
		super();
		this.name = name;
		this.solution = solution;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSolution() {
		return solution;
	}
	public void setSolution(String solution) {
		this.solution = solution;
	}
	
}
