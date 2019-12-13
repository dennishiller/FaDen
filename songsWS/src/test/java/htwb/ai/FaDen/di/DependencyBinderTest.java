package htwb.ai.FaDen.di;

import htwb.ai.FaDen.dao.ISongDao;
import htwb.ai.FaDen.dao.InMemorySongDao;
import org.glassfish.hk2.utilities.binding.AbstractBinder;


public class DependencyBinderTest extends AbstractBinder {

    @Override
    protected void configure() {
        bind(InMemorySongDao.class).to(ISongDao.class);
        //bind(Authenticator.class).to(IAuthenticator.class).in(Singleton.class);
    }
}
