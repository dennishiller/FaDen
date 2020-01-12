package htwb.ai.FaDen.dao;

import htwb.ai.FaDen.bean.SongList;

import java.util.Collection;

public interface ISongListDao {
	public Collection<SongList> getSongLists(String userID);
	public Collection<SongList> getPublicSongLists(String userID);
	public SongList getSongList(Integer songListID);
	public Integer addSongList(SongList songList);
	public SongList deleteSongList(Integer songListID);
	public Collection<SongList> getAllSongLists();
}
