package htwb.ai.FaDen.services;

import htwb.ai.FaDen.bean.Song;
import htwb.ai.FaDen.di.ResourceConfigTest;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class SongServiceTest extends JerseyTest {

    @Override
    protected ResourceConfigTest configure() {
        return new ResourceConfigTest();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @AfterEach
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void getAllSongs() {
        Response response = target("/songs").request(MediaType.APPLICATION_JSON).get();
        List<Song> song = response.readEntity(List.class);
        System.out.println("All Songs: " + song);
        assertEquals(10, song.size());
        assertEquals(200, response.getStatus());
    }

    @Test
    public void getSongById() {
        Response response = target("/songs/1").request(MediaType.APPLICATION_JSON).get();
        Song song = response.readEntity(Song.class);
        System.out.println("Song: " + song);
        assertEquals(1, song.getId());
        assertEquals(200, response.getStatus());
    }

//    @Test
//    public void getSongByIdXML() {
//        Response response = target("/songs/1").request(MediaType.APPLICATION_XML).get();
//        Song song = response.readEntity(Song.class);
//        System.out.println("Song: " + song);
//        assertEquals(1, song.getId());
//        assertEquals(200, response.getStatus());
//    }

    @Test
    public void getSongByIdNotFound() {
        Response response = target("/songs/-1").request(MediaType.APPLICATION_JSON).get();
        assertEquals(404, response.getStatus());
        assertEquals("ID not found", response.readEntity(String.class));
    }

    @Test
    public void getSongByInvaldiId() {
        Response response = target("/songs/fdafdasfds").request(MediaType.APPLICATION_JSON).get();
        assertEquals(404, response.getStatus());
    }
}
