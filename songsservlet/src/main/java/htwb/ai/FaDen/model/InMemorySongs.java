package htwb.ai.FaDen.model;

import javax.xml.bind.annotation.XmlAnyElement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemorySongs {

    private static InMemorySongs instance = null;
    private Map<Integer, Song> storage;

    public InMemorySongs(List<Song> songs) {
        songs.forEach(song -> getInstance().storage.put(song.getId(), song));
    }

    private InMemorySongs() {
        storage = new ConcurrentHashMap<>();
    }

    public static InMemorySongs getInstance() {
        if(instance == null) instance = new InMemorySongs();
        return instance;
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
