package htwb.ai.FaDen.bean;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

@XmlRootElement
@Entity
@Table(name = "songlist")
public class SongList {

    @Id
    private Integer id;
    private String name;
    private Boolean isPrivate;
    @ManyToOne
    @JoinColumn(name="ownerid")
    private User user;
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "songlist_song",
            joinColumns = {@JoinColumn(name = "songlist_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "songs_id", referencedColumnName = "id")})
    private Set<Song> songs;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Set<Song> getSongs() {
        return songs;
    }

    public void setSongs(Set<Song> songs) {
        this.songs = songs;
    }
}
