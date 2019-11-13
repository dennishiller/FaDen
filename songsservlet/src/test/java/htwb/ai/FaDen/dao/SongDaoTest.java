package htwb.ai.FaDen.dao;

import htwb.ai.FaDen.model.InMemorySongs;
import htwb.ai.FaDen.model.Song;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import java.util.List;

public class SongDaoTest {

    @Test
    public void readJSONtoSongsTest() {
        try {
            SongDao dao = new SongDao();
            List<Song> songs = dao.readJSONToSongs("testSongs.json");
            songs.forEach(song -> System.out.println(song.toString()));
            assertEquals(4, songs.size());
            assertEquals("Mom", songs.get(2).getTitle());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void loadSongsInMemoryTest() {

        try {
            SongDao dao = new SongDao();
            List<Song> songs = dao.readJSONToSongs("testSongs.json");
            songs.add(new Song(5, "Yolo", "Dennis", "BBM ist die Gang", 1969));
            dao.saveInMemoryToJSON(songs, "editedSongs.json");

            //Nochmal einlesen zum asserten
            songs = dao.readJSONToSongs("editedSongs.json");
            assertEquals(5, songs.size());
            assertEquals("Yolo", songs.get(4).getTitle());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
