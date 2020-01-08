package htwb.ai.FaDen.dao;

import htwb.ai.FaDen.bean.Song;
import htwb.ai.FaDen.bean.SongList;

import javax.inject.Inject;
import javax.persistence.*;
import java.util.Collection;

public class SongListDao implements ISongListDAO {

	private EntityManagerFactory emf;
	@Inject
	public SongListDao(EntityManagerFactory emf){ this.emf = emf;}

	@Override
	public Collection<SongList> getSongLists(String userID) {
		EntityManager em = null;
		try {
			em = emf.createEntityManager();
			Query q = em.createQuery("SELECT s FROM SongList s WHERE s.user.id = :userID ")
					.setParameter("userID", userID);
			return q.getResultList();
		} catch (Exception e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	@Override
	public Collection<SongList> getPublicSongLists(String userID) {
		EntityManager em = null;
		try {
			em = emf.createEntityManager();
			Query q = em.createQuery("SELECT s FROM SongList s WHERE s.user.id = :userID AND s.isPrivate = false")
					.setParameter("userID", userID);
			return q.getResultList();
		} catch (Exception e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	@Override
	public SongList getSongList(Integer songListID) {
		EntityManager em = null;
		try {
			em = emf.createEntityManager();
			return em.find(SongList.class, songListID);
		} catch (Exception e) {
			throw new PersistenceException(e.getMessage());
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}




	@Override
	public Integer addSongList(SongList songList) {
		EntityManager em = null;
		EntityTransaction transaction = null;
		try {
			em = emf.createEntityManager();

			Song alreadyExists = em.find(Song.class, songList.getId());
			if (alreadyExists != null) return null;
			transaction = em.getTransaction();

			transaction.begin();
			em.persist(songList);
			transaction.commit();

			return songList.getId();
		} catch (Exception e) {
			if(em != null){
				em.getTransaction().rollback();
			}
			throw new PersistenceException(e.getMessage());
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	@Override
	public SongList deleteSongList(Integer songListID) {
		EntityManager em = null;
		EntityTransaction transaction = null;
		try {
			em = emf.createEntityManager();
			transaction = em.getTransaction();

			transaction.begin();
			SongList songList = em.find(SongList.class, songListID);
			if (songList != null) {
				em.remove(songList);
				transaction.commit();
			}
			return songList;
		} catch (Exception e) {
			throw new PersistenceException("Could not remove entity with id: " + songListID);
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}
}
