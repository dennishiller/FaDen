package htwb.ai.FaDen.dao;

import htwb.ai.FaDen.bean.Song;
import htwb.ai.FaDen.bean.User;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.Collection;

public class UserDao implements IUserDao {

    private EntityManagerFactory emf;

    @Inject
    public UserDao(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public User getUser(String id, String key) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            User user = em.find(User.class, id);
            if (user == null) return null;
            if (!user.getKey().equals(key)) return null;
            return user;
        } catch (Exception e) {
            throw new PersistenceException(e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public Collection<User> getAllUser() {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            Query q = em.createQuery("SELECT u FROM User u");
            return q.getResultList();
        } catch (Exception e) {
            throw new PersistenceException(e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
