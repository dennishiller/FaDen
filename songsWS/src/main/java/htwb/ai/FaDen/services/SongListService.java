package htwb.ai.FaDen.services;

import htwb.ai.FaDen.authentication.Authenticator;
import htwb.ai.FaDen.authentication.IAuthenticator;
import htwb.ai.FaDen.bean.Song;
import htwb.ai.FaDen.bean.SongList;
import htwb.ai.FaDen.bean.User;
import htwb.ai.FaDen.dao.ISongDao;
import htwb.ai.FaDen.dao.ISongListDao;
import htwb.ai.FaDen.dao.IUserDao;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Path("/songLists")
public class SongListService {

    @Inject
    private ISongListDao songListDao;

    @Inject
    private IAuthenticator authenticator;

    @Inject
    private IUserDao userDao;

    @Inject
    private ISongDao songDao;

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

        if (authenticator.getTokenMap().get(songList.getUser()).equals(authorizationToken)) {
            return Response.status(Response.Status.OK).entity(songList).build();
        } else {
            if (songList.getIsPrivate()) {
                return Response.status(Response.Status.FORBIDDEN).build();
            } else {
                return Response.status(Response.Status.OK).entity(songList).build();
            }
        }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAllSongLists(@QueryParam("userId") String userId, @HeaderParam("Authorization") String authorizationToken) {
        Collection<SongList> songLists = null;
        // TODO neu schreiben ueber den User die Listen holen

        if (userId.equals(getUserIdByAuthorizationToken(authorizationToken))) {
            try {
                songLists = songListDao.getSongLists(userId);
            } catch (PersistenceException e) {
                return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Database error. We fucked up, sorry").build();
            }
        } else {
            if (!isUserRegistered(userId)) return Response.status(Response.Status.NOT_FOUND).build();
            try {
                songLists = songListDao.getPublicSongLists(userId);
            } catch (PersistenceException e) {
                return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Database error. We fucked up, sorry").build();
            }
        }

        if (songLists == null) return Response.status(Response.Status.NOT_FOUND).entity("ID not found").build();

        GenericEntity<Collection<SongList>> entity = new GenericEntity<Collection<SongList>>(songLists) {};
        return Response.status(Response.Status.OK).entity(entity).build();
    }


    @DELETE
    @Path("{songListID}")
    public Response deleteSongList(@PathParam("songListID") Integer songListID, @HeaderParam("Authorization") String authorizationToken) {
        try {
            SongList songList = songListDao.getSongList(songListID);
            if (songList == null) return Response.status(Response.Status.BAD_REQUEST).build();
            if (!(songList.getUser().getUserId().equals(getUserIdByAuthorizationToken(authorizationToken))))
                return Response.status(Response.Status.FORBIDDEN).build();

            songListDao.deleteSongList(songListID);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (PersistenceException e) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Database error. We fucked up, sorry").build();
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response addSongList(SongList songList, @HeaderParam("Authorization") String authorizationToken, @Context UriInfo uriInfo) {
        if (songList == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("No Payload available").build();
        if (songList.getSongs() == null || songList.getSongs().isEmpty())
            return Response.status(Response.Status.BAD_REQUEST).entity("No Songs provided").build();
        if (songList.getIsPrivate() == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Payload incomplete").build();
        if (songList.getName().isEmpty() || songList.getName().isBlank() || songList.getName() == null)
            return Response.status(Response.Status.BAD_REQUEST).entity("Payload incomplete").build();

        Collection<Song> allSongsInPayload = songList.getSongs();
        for (Song song : allSongsInPayload) {
            if (!song.validWithId()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Payload incomplete").build();
            } else {
                Song tempSong = songDao.getSong(song.getId());
                if (tempSong == null)
                    return Response.status(Response.Status.BAD_REQUEST).entity("Song does not exist").build();
                if (!tempSong.hasSameContentAs(song)) {
                    return Response.status(Response.Status.BAD_REQUEST).entity("Song Payload is corrupt").build();
                }
            }
        }
        songList.setId(getNewIndex());
        songList.setUser(getUserByAuthorizationToken(authorizationToken));

        Integer songListID = songListDao.addSongList(songList);
        if (songListID == null) return Response.status(Response.Status.BAD_REQUEST).build();

        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        uriBuilder.path(Integer.toString(songListID));
        return Response.created(uriBuilder.build()).build();
    }

    public String getUserIdByAuthorizationToken(String token) {
        Set<User> userSet = authenticator.getTokenMap().keySet();
        for (User user : userSet) {
            if (token.equals(authenticator.getTokenMap().get(user))) return user.getUserId();
        }
        return null;
    }

    public User getUserByAuthorizationToken(String token) {
        Set<User> userSet = authenticator.getTokenMap().keySet();
        for (User user : userSet) {
            if (token.equals(authenticator.getTokenMap().get(user))) return user;
        }
        return null;
    }

    private Integer getNewIndex() {
        Collection<SongList> songList = songListDao.getAllSongLists();
        Set<Integer> intSet = IntStream.rangeClosed(1, songList.size()+1).boxed().collect(Collectors.toSet());
        Set<Integer> alreadyUsedIds = songList.stream().map(SongList::getId).collect(Collectors.toSet());
        intSet.removeAll(alreadyUsedIds);
        return intSet.stream().findFirst().orElse(-1);
    }

    private boolean isUserRegistered(String userId) {
        Set<User> users = new HashSet<>(userDao.getAllUser());
        User exists = users.stream().filter(user -> user.getUserId().equals(userId)).findFirst().orElse(null);
        return null != exists;
    }
}
