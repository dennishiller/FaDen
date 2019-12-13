package htwb.ai.FaDen.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import htwb.ai.FaDen.bean.Song;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemorySongDao implements ISongDao {

    Map<Integer, Song> storage = new HashMap<>();

    public InMemorySongDao() {
        List<Song> songs = null;
        try {
            songs = readJSONToSongs("songs.json");
        } catch (Exception e) {
            System.exit(0);
        }
        for (Song song : songs) {
            storage.put(song.getId(), song);
        }
    }

    private List<Song> readJSONToSongs(String filename) throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename);
        ObjectMapper objectMapper = new ObjectMapper();
        return (List<Song>) objectMapper.readValue(in, new TypeReference<List<Song>>() {});
    }

    @Override
    public Song getSong(Integer id) {
        return storage.get(id);
    }

    @Override
    public Collection<Song> getSongs() {
        return storage.values();
    }

    @Override
    public Integer addSong(Song song) {
        storage.put(song.getId(), song);
        return song.getId();
    }

    @Override
    public Song updateSong(Song song) {
        storage.put(song.getId(), song);
        return song;
    }

    @Override
    public Song deleteSong(Integer id) {
        Song song = storage.get(id);
        storage.remove(id);
        return song;
    }
}
