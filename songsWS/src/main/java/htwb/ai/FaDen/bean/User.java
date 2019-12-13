package htwb.ai.FaDen.bean;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

	@Id
	private String userId;
	private String key;
	private String firstName;
	private String lastName;

	public User(){};

	public User(String userId, String key, String firstName, String lastName) {
		this.userId = userId;
		this.key = key;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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

	@Override
	public String toString() {
		return "User{" +
				"id=" + userId +
				", key='" + key + '\'' +
				", firstname='" + firstName + '\'' +
				", lastname='" + lastName + '\'' +
				'}';
	}
}
