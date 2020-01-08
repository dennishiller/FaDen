package htwb.ai.FaDen.services;

import htwb.ai.FaDen.bean.Song;
import htwb.ai.FaDen.dao.ISongDao;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Path("/songs")
public class SongService {

	@Inject
	private ISongDao songDao;

	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getAllSongs() {
		try {
			Collection<Song> songs = songDao.getSongs();
			GenericEntity<Collection<Song>> entity = new GenericEntity<Collection<Song>>(songs) {}; //otherwise we get problems with xml lists
			return Response.status(Response.Status.OK).entity(entity).build();
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
		if (song == null) return Response.status(Response.Status.BAD_REQUEST).entity("No Payload available").build();
		try {
			if (!song.valid()) return Response.status(Response.Status.BAD_REQUEST).entity("Payload is not complete").build();
			if (song.getId() <= 0)  return Response.status(Response.Status.BAD_REQUEST).entity("Id is not valid").build();
			if (!song.idIsSet()) {
				Collection<Song> songs = songDao.getSongs();
				Set<Integer> intSet = IntStream.rangeClosed(1, songs.size()+1).boxed().collect(Collectors.toSet());
				Set<Integer> alreadyUsedIds = songs.stream().map(Song::getId).collect(Collectors.toSet());
				intSet.removeAll(alreadyUsedIds);
				int newId = intSet.stream().findFirst().orElse(-1);

				song.setId(newId);
			}
			Integer newId = songDao.addSong(song);
			if (newId == null) return Response.status(Response.Status.BAD_REQUEST).build();
			UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
			uriBuilder.path(Integer.toString(newId));
			return Response.created(uriBuilder.build()).build();
		} catch (PersistenceException e) {
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Database error. We fucked up, sorry").build();
		}
	}

	@PUT
	@Path("{id}")
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response updateSong(Song song, @PathParam("id") int id) {
		if (song == null) return Response.status(Response.Status.BAD_REQUEST).entity("No Payload available").build();
		if (song.getId() != id) return Response.status(Response.Status.BAD_REQUEST).entity("Payload-Id doesn't match with path-id").build();
		if (song.getTitle() == null) return Response.status(Response.Status.BAD_REQUEST).entity("Title is empty").build();
		if (!song.valid()) return Response.status(Response.Status.BAD_REQUEST).entity("Payload is not complete").build();

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
