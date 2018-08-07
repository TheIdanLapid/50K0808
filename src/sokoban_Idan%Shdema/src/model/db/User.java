package model.db;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
/**
 * The user for Users table in the DB.
 * @author idanlapid
 *
 */
@Entity(name = "Users")
public class User {

	@Column(name = "FirstName")
	private String firstName;
	@Column(name = "LastName")
	private String lastName;
	@Id
	@Column(name = "ID")
	private int id;

	@OneToMany
	@JoinColumn(name = "UserID")
	private List<Score> levels;

	public User() {
	}

	public User(String firstName, String lastName, int id) {
		setFirstName(firstName);
		setLastName(lastName);
		setId(id);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", lastName=" + lastName + ", id=" + id + "]";
	}

}
