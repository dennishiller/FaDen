package htwb.ai.FaDen.services;

import htwb.ai.FaDen.dao.ISongListDAO;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path("/songs")
public class SongListService {
	@Inject
	private ISongListDAO songListDAO;

}
