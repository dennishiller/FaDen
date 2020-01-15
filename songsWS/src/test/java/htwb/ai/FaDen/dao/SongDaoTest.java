package htwb.ai.FaDen.dao;

import htwb.ai.FaDen.bean.Song;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import java.util.Collection;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SongDaoTest {

    private static final String PERSISTENCE_UNIT_NAME = "songDB-TEST-PU";
    private static EntityManagerFactory emf;
    private SongDao songDao;

    @BeforeAll
    public static void initEMF() {
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    @BeforeEach
    public void setUpDao() {
        songDao = new SongDao(emf);
    }

    @Test
    void initStandardUseCase() {
        SongDao dao = new SongDao(emf);
        assertNotNull(dao);
    }

    @Test
    void getSongStandardUseCase()
    {
        Song storedSong = songDao.getSong(1);
        assertEquals(storedSong.getId(), 1);
        assertEquals(storedSong.getArtist(), "song01_artist");
        assertEquals(storedSong.getLabel(), "song01_label");
    }

    @Test
    void getSongNotExistingId_null()
    {
        Song storedSong = songDao.getSong(4);
        assertNull(storedSong);
    }

    @Test
    void getSongIdNegative_null()
    {
        Song storedSong = songDao.getSong(-4);
        assertNull(storedSong);
    }

    @Test
    void getSongIdNull_null()
    {
        assertThrows(PersistenceException.class, () -> {
            songDao.getSong(null);
        });
    }

    @Test
    void addSongStandardUseCase()
    {
        Song song01 = new Song(3, "song03_title", "song03_artist", "song03_label", 2003);
        Integer songId01 = songDao.addSong(song01);
        Song storedSong01 = songDao.getSong(songId01);
        assertEquals(song01.getId(), storedSong01.getId());
        assertEquals(song01.getTitle(), storedSong01.getTitle());
        assertEquals(song01.getArtist(), storedSong01.getArtist());
        assertEquals(song01.getLabel(), storedSong01.getLabel());
        assertEquals(song01.getReleased(), storedSong01.getReleased());
    }

    @Test
    void addSongTitleNull()
    {
        Song song01 = new Song(51, null, "song03_artist", "song03_label", 2003);
        Integer songId01 = songDao.addSong(song01);
        Song storedSong01 = songDao.getSong(songId01);
        assertEquals(song01.getId(), storedSong01.getId());
        assertEquals(song01.getTitle(), storedSong01.getTitle());
        assertEquals(song01.getArtist(), storedSong01.getArtist());
        assertEquals(song01.getLabel(), storedSong01.getLabel());
        assertEquals(song01.getReleased(), storedSong01.getReleased());
    }

    @Test
    void addSongTitleEmpty()
    {
        Song song01 = new Song(52, "", "song03_artist", "song03_label", 2003);
        Integer songId01 = songDao.addSong(song01);
        Song storedSong01 = songDao.getSong(songId01);
        assertEquals(song01.getId(), storedSong01.getId());
        assertEquals(song01.getTitle(), storedSong01.getTitle());
        assertEquals(song01.getArtist(), storedSong01.getArtist());
        assertEquals(song01.getLabel(), storedSong01.getLabel());
        assertEquals(song01.getReleased(), storedSong01.getReleased());
    }

    @Test
    void addSongArtistNull()
    {
        Song song01 = new Song(53, "mySong", null, "song03_label", 2003);
        Integer songId01 = songDao.addSong(song01);
        Song storedSong01 = songDao.getSong(songId01);
        assertEquals(song01.getId(), storedSong01.getId());
        assertEquals(song01.getTitle(), storedSong01.getTitle());
        assertEquals(song01.getArtist(), storedSong01.getArtist());
        assertEquals(song01.getLabel(), storedSong01.getLabel());
        assertEquals(song01.getReleased(), storedSong01.getReleased());
    }

    @Test
    void addSongLabelNull()
    {
        Song song01 = new Song(55, "mySong", "SpecialArtist", null, 2003);
        Integer songId01 = songDao.addSong(song01);
        Song storedSong01 = songDao.getSong(songId01);
        assertEquals(song01.getId(), storedSong01.getId());
        assertEquals(song01.getTitle(), storedSong01.getTitle());
        assertEquals(song01.getArtist(), storedSong01.getArtist());
        assertEquals(song01.getLabel(), storedSong01.getLabel());
        assertEquals(song01.getReleased(), storedSong01.getReleased());
    }


    @Test
    void addSongIdNegative()
    {
        // we filter out these exceptions in service -> no error here
        Song song01 = new Song(-1, "song03_title", "song03_artist", "song03_label", 2003);
        assertNotNull(song01);
    }

    @Test
    void getAllSongsStandard() {
        Collection<Song> songs = songDao.getSongs();
        assertTrue(songs.size() >= 1);
        Song storedSong = songs.stream().findFirst().get();
        assertEquals(storedSong.getId(), 1);
        assertEquals(storedSong.getArtist(), "song01_artist");
        assertEquals(storedSong.getLabel(), "song01_label");
    }

    @Test
    void updateSongStandardUseCase()
    {
        Song song = new Song(2, "updated_title", "updated_artist", "updated_label", 2002);
        Song updatedSong = songDao.updateSong(song);
        assertEquals(updatedSong.getId(), 2);
        assertEquals(updatedSong.getTitle(), "updated_title");
        assertEquals(updatedSong.getArtist(), "updated_artist");
    }

    @Test
    void updateSongTitleNull() {
        Song song = new Song(2, null, "updated_artist", "updated_label", 2002);
        Song updatedSong = songDao.updateSong(song);
        assertEquals(song.getArtist(), updatedSong.getArtist());
        assertNull(updatedSong.getTitle());
    }

    @Test
    void updateSongArtistNull() {
        Song song = new Song(2, "NewTitle", null, "updated_label", 2002);
        Song updatedSong = songDao.updateSong(song);
        assertEquals(song.getTitle(), updatedSong.getTitle());
        assertNull(updatedSong.getArtist());
    }

    @Test
    void updateSongLabelNull() {
        Song song = new Song(2, "NewTitle", "TheArtist", null, 2002);
        Song updatedSong = songDao.updateSong(song);
        assertEquals(song.getArtist(), updatedSong.getArtist());
        assertEquals(song.getTitle(), updatedSong.getTitle());
        assertNull(updatedSong.getLabel());
    }


    @Test
    void updateSongNull() {
        assertThrows(NullPointerException.class, () -> songDao.updateSong(null));
    }

    @Test
    void updateSongNotExistingId() {
        // we set our id in service, so no error here
        Song song = new Song(4, "updated_title", "updated_artist", "updated_label", 2002);
        assertNotNull(song);
    }

    @Test
    void updateSongNullId() {
        Song song = new Song(null, "updated_title", "updated_artist", "updated_label", 2002);
        assertThrows(PersistenceException.class, () -> songDao.updateSong(song));
    }

    @Test
    void deleteSongStandardUseCase() {
        Song song01 = new Song(60, "Title", "Artist", "Label", 2003);
        songDao.addSong(song01);
        Song deletedSong = songDao.deleteSong(song01.getId());
        assertEquals(song01.getId(), deletedSong.getId());
    }

    @Test
    void deleteSongNotExisting() {
        Song deletedSong = songDao.deleteSong(70);
        assertNull(deletedSong);
    }

    @Test
    void deleteSongNegativeId() {
        Song deletedSong = songDao.deleteSong(-1);
        assertNull(deletedSong);
    }

    @Test
    void deleteSongIdNull() {
        assertThrows(PersistenceException.class, () -> songDao.deleteSong(null));
    }
}