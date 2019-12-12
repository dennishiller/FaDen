package htwb.ai.FaDen.di;

import htwb.ai.FaDen.dao.ISongDao;
import htwb.ai.FaDen.dao.SongDao;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DependencyBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(Persistence.createEntityManagerFactory("songsWS-PU")).to(EntityManagerFactory.class);
        bind(SongDao.class).to(ISongDao.class);
    }
}
