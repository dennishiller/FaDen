package htwb.ai.FaDen.api;

import htwb.ai.FaDen.bean.Song;
import htwb.ai.FaDen.inMemory.InMemorySongs;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

@Path("/songsWS-FaDen/rest")
public class SongService {
	private InMemorySongs songs = InMemorySongs.getInstance();

	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Collection<Song> getAllSongs(){
		return songs.getAllSongs();
	}

	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Song getSong(int songId){
		return songs.get(songId);
	}

	private boolean checkValid(int index) {
		return index >= 1 && index <= 10;
	}
}
