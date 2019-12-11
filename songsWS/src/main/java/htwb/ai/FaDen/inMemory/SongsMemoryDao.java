package htwb.ai.FaDen.inMemory;

import htwb.ai.FaDen.bean.Song;
import htwb.ai.FaDen.dao.ISongDao;

import javax.xml.bind.annotation.XmlAnyElement;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SongsMemoryDao {

    private static SongsMemoryDao instance = null;
    private Map<Integer, Song> storage;

    public SongsMemoryDao(List<Song> songs) {
        storage = new ConcurrentHashMap<>();
        songs.forEach(song -> storage.put(song.getId(), song));
    }

    private SongsMemoryDao() {
        storage = new ConcurrentHashMap<>();
    }

    public static SongsMemoryDao getInstance() {
        if(instance == null) instance = new SongsMemoryDao();
        return instance;
    }

    public Collection<Song> getAllSongs(){
        return storage.values();
    }

    public Song update(int key, Song value) {
        if(!storage.containsKey(key)) throw new IllegalArgumentException("No such element!");
        return storage.replace(key, value);
    }

    public Song get(int key) {
        return storage.get(key);
    }

    @XmlAnyElement(lax = true) //TODO what does this do?
    public List<Song> getSongs() {
        return new LinkedList<Song>(storage.values());
    }
}
