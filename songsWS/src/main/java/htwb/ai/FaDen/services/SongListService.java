package htwb.ai.FaDen.services;

import htwb.ai.FaDen.authentication.Authenticator;
import htwb.ai.FaDen.authentication.IAuthenticator;
import htwb.ai.FaDen.bean.SongList;
import htwb.ai.FaDen.bean.User;
import htwb.ai.FaDen.dao.ISongListDao;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Set;

@Path("/songLists")
public class SongListService {

	@Inject
	private ISongListDao songListDao;

	@Inject
	private IAuthenticator authenticator;

	@GET
	@Path("{songListId}")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getSongList(@PathParam("songListId") int songListId, @HeaderParam("Authorization") String authorizationToken) {
		SongList songList = null;
		try {
			songList = songListDao.getSongList(songListId);
		} catch (PersistenceException e) {
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Database error. We fucked up, sorry").build();
		}

		if (songList == null) return Response.status(Response.Status.NOT_FOUND).entity("ID not found").build();

		String userIdByToken = getUserIdByAuthorizationToken(authorizationToken);
		if (songList.getUser().getUserId().equals(userIdByToken)) {
			return Response.status(Response.Status.OK).entity(songList).build();
		}
		else {
			if (songList.getPrivate()) {
				return Response.status(Response.Status.FORBIDDEN).build();
			}
			else {
				return Response.status(Response.Status.OK).entity(songList).build();
			}
		}
	}

	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getAllSongs(@QueryParam("userId") String userId, @HeaderParam("Authorization") String authorizationToken) {
		Collection<SongList> songLists= null;

		if (userId.equals(getUserIdByAuthorizationToken(authorizationToken))) {
			try {
				songLists = songListDao.getSongLists(userId);
			} catch (PersistenceException e) {
				return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Database error. We fucked up, sorry").build();
			}
		}
		else {
			try {
				songLists = songListDao.getPublicSongLists(userId);
			} catch (PersistenceException e) {
				return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Database error. We fucked up, sorry").build();
			}
		}

		if (songLists == null) return Response.status(Response.Status.NOT_FOUND).entity("ID not found").build();
		return Response.status(Response.Status.OK).entity(songLists).build();
	}

	public String getUserIdByAuthorizationToken(String token) {
		Set<User> userSet = authenticator.getTokenMap().keySet();
		for (User user : userSet) {
			if (token.equals(authenticator.getTokenMap().get(user))) return user.getUserId();
		}
		return null;
	}
}
