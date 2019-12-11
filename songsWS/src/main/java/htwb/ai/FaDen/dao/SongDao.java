package htwb.ai.FaDen.dao;

import htwb.ai.FaDen.bean.Song;
import javax.inject.Inject;
import javax.persistence.*;
import java.util.Collection;

public class SongDao implements ISongDao {

    @Inject
    private EntityManagerFactory emf;


    @Override
    public Song getSong(Integer id) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            return em.find(Song.class, id);
        } catch (Exception e) {
            throw new PersistenceException(e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public Collection<Song> getSongs() {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            Query q = em.createQuery("SELECT s FROM Song s");
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
    public Integer addSong(Song song) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();

            transaction.begin();
            em.persist(song);
            transaction.commit();

            return song.getId();
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
    public Song updateSong(Song song) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();

            transaction.begin();
            Song mergedSong = em.merge(song);
            if (mergedSong != null) {
                transaction.commit();
            }
            return mergedSong;
        } catch (Exception e) {
            transaction.rollback();
            throw new PersistenceException("Could not update entity: " + song.toString());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public Song deleteSong(Integer id) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();

            transaction.begin();
            Song song = em.find(Song.class, id);
            if (song != null) {
                em.remove(song);
                transaction.commit();
            }
            return song;
        } catch (Exception e) {
            throw new PersistenceException("Could not remove entity with id: " + id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
