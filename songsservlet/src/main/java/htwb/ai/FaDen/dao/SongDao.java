package htwb.ai.FaDen.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import htwb.ai.FaDen.model.InMemorySongs;
import htwb.ai.FaDen.model.Song;

import java.io.*;
import java.util.List;

public class SongDao {

    // Reads a list of songs from a JSON-file into List<Song>
    @SuppressWarnings("unchecked")
    public List<Song> readJSONToSongs(String filename) throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(filename);
        ObjectMapper objectMapper = new ObjectMapper();
        return (List<Song>) objectMapper.readValue(in, new TypeReference<List<Song>>() {});
    }

    public InMemorySongs loadSongsInMemory(String filename) throws IOException{
        List<Song> songs = readJSONToSongs(filename);
        return new InMemorySongs(songs);
    }

    public void saveInMemoryToJSON(List<Song> songs, String filename) throws IOException { //TODO was ist wenn File noch nicht existiert?
        PrintWriter writer = new PrintWriter(new File(this.getClass().getClassLoader().getResource(filename).getPath()));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(writer, songs);
    }

    public void writeStoragetoJSON(String filename) throws IOException {
        List<Song> songs = InMemorySongs.getInstance().getSongs();
        saveInMemoryToJSON(songs, filename);
    }
}
