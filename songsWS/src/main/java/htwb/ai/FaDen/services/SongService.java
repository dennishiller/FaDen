package htwb.ai.FaDen.services;

import htwb.ai.FaDen.bean.Song;
import htwb.ai.FaDen.dao.ISongDao;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Collection;

@Path("/songs")
public class SongService {

	@Inject
	private ISongDao songDao;

	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getAllSongs() {
		try {
			Collection<Song> songs = songDao.getSongs();
			return Response.status(Response.Status.OK).entity(songs).build();
		} catch (PersistenceException e) {
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Database error. We fucked up, sorry").build();
		}
	}

	@GET
	@Path("{id}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getSong(@PathParam("id") int songId) {
		Song song = null;
		try {
			 song = songDao.getSong(songId);
		} catch (PersistenceException e) {
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Database error. We fucked up, sorry").build();
		}
		if (song == null) return Response.status(Response.Status.NOT_FOUND).entity("ID not found").build();
		return Response.status(Response.Status.OK).entity(song).build();
	}

	@POST
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response addSong(Song song, @Context UriInfo uriInfo) {
		try {
			Integer newId = songDao.addSong(song);
			UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
			uriBuilder.path(Integer.toString(newId));
			return Response.created(uriBuilder.build()).build(); // TODO: Location Header
		} catch (PersistenceException e) {
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Database error. We fucked up, sorry").build();
		}
	}

	@PUT
	@Path("/{id}")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response updateSong(Song song, @PathParam("id") int id) {
		if (song.getId() != id) return Response.status(Response.Status.BAD_REQUEST).entity("Payload-Id doesn't match with path-id").build();
		if (song.getTitle() == null) return Response.status(Response.Status.BAD_REQUEST).entity("Title is empty").build();

		try {
			Song updatedSong = songDao.updateSong(song);
			if (updatedSong == null) {
				return Response.status(Response.Status.NOT_FOUND).entity("Song not found").build();
			}
			return Response.status(Response.Status.NO_CONTENT).build();
		} catch (PersistenceException e) {
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Database error. We fucked up, sorry").build();
		}
	}

	@DELETE
	@Path("{id}")
	public Response deleteSong(@PathParam("id") int id) {
		try {
			Song song = songDao.deleteSong(id);
			if (song == null) return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (PersistenceException e) {
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Database error. We fucked up, sorry").build();
		}
		return Response.status(Response.Status.NO_CONTENT).build();
	}
}
