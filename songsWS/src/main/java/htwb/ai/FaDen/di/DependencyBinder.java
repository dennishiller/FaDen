package htwb.ai.FaDen.di;

import htwb.ai.FaDen.authentication.Authenticator;
import htwb.ai.FaDen.authentication.IAuthenticator;
import htwb.ai.FaDen.dao.*;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DependencyBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(Persistence.createEntityManagerFactory("songsWS-PU")).to(EntityManagerFactory.class);
        bind(SongDao.class).to(ISongDao.class);
        bind(UserDao.class).to(IUserDao.class);
        bind(Authenticator.class).to(IAuthenticator.class).in(Singleton.class);
        bind(SongListDao.class).to(ISongListDao.class);
    }
}
