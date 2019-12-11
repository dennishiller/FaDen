package htwb.ai.FaDen;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

//import htwb.ai.TEAMNAME.di.DependencyBinder;

@ApplicationPath("/rest")
public class MyApplication extends ResourceConfig {
    public MyApplication() {
//        register(new DependencyBinder());
        packages("htwb.ai.FaDen.services");
    }

    private static final String PERSISTENCE_UNIT_NAME = "songsWS";
    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();


    }
}

