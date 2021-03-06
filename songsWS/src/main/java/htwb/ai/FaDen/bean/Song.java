package htwb.ai.FaDen.bean;


import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "song")
@Entity
@Table(name = "song")
public class Song {

	@Id
	private Integer id;
	private String title;
	private String artist;
	private String label;
	private Integer released;

	public Song() {
	} //WICHTIG!

	public Song(Integer id, String title, String artist, String label, Integer released) {
		this.id = id;
		this.title = title;
		this.artist = artist;
		this.label = label;
		this.released = released;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getReleased() {
		return released;
	}

	public void setReleased(Integer released) {
		this.released = released;
	}

	@Override
	public String toString() {
		return "Song [id=" + id + ", title=" + title + ", artist=" + artist + ", label=" + label + ", released=" + released + "]";
	}

	public boolean valid() {
		if (title == null || title.isEmpty() || title.isBlank()) return false;
		if (artist == null || artist.isEmpty()) return false;
		if (label == null || label.isEmpty()) return false;
		if (released == null || released < 0) return false;

		return true;
	}

	public boolean validWithId() {
		if(id == null || id < 1) return false;
		return valid();
	}

	public boolean idIsSet() {
		return id != null;
	}

	public boolean hasSameContentAs(Song otherSong) {
		if (otherSong == null) return false;
		if (!this.id.equals(otherSong.id)) return false;
		if (!this.title.equals(otherSong.title)) return false;
		if (!this.artist.equals(otherSong.artist)) return false;
		if (!this.label.equals(otherSong.label)) return false;
		if (!this.released.equals(otherSong.released)) return false;
		return true;
	}
}
