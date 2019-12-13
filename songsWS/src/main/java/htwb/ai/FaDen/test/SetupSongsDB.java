package htwb.ai.FaDen.test;
import htwb.ai.FaDen.bean.Song;
import htwb.ai.FaDen.dao.SongDao;
import htwb.ai.FaDen.inMemory.InMemorySongDao;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import java.io.IOException;
import java.util.Collection;

public class SetupSongsDB
{
    private static final String PERSISTENCE_UNIT_NAME = "songsWS-PU";

    public static void main(String[] args)
    {
        setup();
    }

    public static void setup()
    {
        Collection<Song> songs = readSongsFromFile();
        if (songs != null) {
            writeSongsToDatabase(songs);
        }
        System.out.println("Songs table DB setup done!");
    }

    private static Collection<Song> readSongsFromFile()
    {
        InMemorySongDao jsonDao = new InMemorySongDao();
        try {
            return jsonDao.readJSONToSongs("songs.json");
        } catch (IOException e) {
            System.out.println("Oops, something went wrong while reading file!");
            System.out.println(e.getMessage());
        }
        return null;
    }

    private static void writeSongsToDatabase(Collection<Song> songs)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        SongDao songDao = new SongDao(emf);
        try {
            for (Song song : songs) {
                songDao.addSong(song);
            }
        }
        catch (PersistenceException e) {
            System.out.println("Oops, something went wrong while writing to db!");
            System.out.println(e.getMessage());
        }
    }
}
