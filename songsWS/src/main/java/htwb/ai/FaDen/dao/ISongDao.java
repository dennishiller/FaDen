package htwb.ai.FaDen.dao;

import htwb.ai.FaDen.bean.Song;
import java.util.Collection;

public interface ISongDao {

    public Song getSong(Integer id);
    public Collection<Song> getSongs();
    public Integer addSong(Song song);
    public Song updateSong(Song song);
    public Song deleteSong(Integer id);
}
