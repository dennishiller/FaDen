package htwb.ai.FaDen.services;

import htwb.ai.FaDen.bean.Song;
import htwb.ai.FaDen.di.ResourceConfigTest;
import htwb.ai.FaDen.inMemory.InMemorySongs;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

import static org.junit.Assert.*;

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


    @Test
    public void postNewSongShouldReturn201() {
        Song song = new Song();
        song.setArtist("Bad Artist");
        song.setTitle("Bad Song");
        song.setReleased(1990);
        song.setLabel("Bla");
        song.setId(30);

        Response response = target("/songs").request().post(Entity.entity(song, MediaType.APPLICATION_JSON));

        assertEquals(201, response.getStatus());
        assertTrue(response.getHeaderString("Location").contains("/songs/30"));
    }

    @Test
    public void postNewSongWithExistingIdShouldReturn201() {
        Song song = new Song();
        song.setArtist("Bad Artist");
        song.setTitle("Bad Song");
        song.setReleased(1990);
        song.setLabel("Bla");
        song.setId(1);

        Response response = target("/songs").request().post(Entity.entity(song, MediaType.APPLICATION_JSON));

        assertEquals(201, response.getStatus());
    }

    @Test
    public void tryEditExistingSongJSONShouldReturn204(){
        Response response = target("/songs/1").request(MediaType.APPLICATION_JSON).get();
        Song song = response.readEntity(Song.class);
        song.setTitle("Last Christmas");

        response = target("/songs/1").request().put(Entity.json(song));

        assertEquals(204, response.getStatus());
    }
    @Test
    public void tryEditExistingSongWrongIdShouldReturn400(){
        Response response = target("/songs/1").request(MediaType.APPLICATION_JSON).get();
        Song song = response.readEntity(Song.class);
        song.setId(1234);
        song.setTitle("Last Christmas");

        response = target("/songs/1").request().put(Entity.json(song));

        assertEquals(400, response.getStatus());
    }
    @Test
    public void tryEditExistingSongXMLShouldReturn204(){
        Response response = target("/songs/1").request(MediaType.APPLICATION_JSON).get();
        Song song = response.readEntity(Song.class);
        song.setTitle("Last Christmas");

        response = target("/songs/1").request().put(Entity.xml(song));

        assertEquals(204, response.getStatus());
    }
    @Test
    public void tryEditExistingSongWithoutTitleShouldReturn400(){
        Response response = target("/songs/1").request(MediaType.APPLICATION_JSON).get();
        Song song = response.readEntity(Song.class);
        song.setTitle(" ");

        response = target("/songs/1").request().put(Entity.json(song));

        assertEquals(400, response.getStatus());
    }

    @Test
    public void tryDeleteASongShouldReturn204(){
        Response response = target("/songs/1").request().delete();

        assertEquals(204, response.getStatus());
    }
    @Test
    public void tryDeleteNegativ1Return400(){
        Response response = target("/songs/-1").request().delete();

        assertEquals(400, response.getStatus());
    }
}
