package htwb.ai.FaDen.bean;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

@XmlRootElement
@Entity
@Table(name = "users")
public class User {

	@Id
	private String userId;
	private String key;
	private String firstName;
	private String lastName;
	@OneToMany(mappedBy = "songs", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<SongList> songLists;

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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (!this.getUserId().equals(other.getUserId()))
			return false;
		if (!this.getKey().equals(other.getKey()))
			return false;
		if (!this.getFirstName().equals(other.getFirstName()))
			return false;
		if (!this.getLastName().equals(other.getLastName()))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		return userId.hashCode();
	}
}