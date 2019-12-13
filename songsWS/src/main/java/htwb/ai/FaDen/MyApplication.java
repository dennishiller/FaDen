package htwb.ai.FaDen;
import javax.ws.rs.ApplicationPath;

import htwb.ai.FaDen.authentication.AuthenticationFilter;
import htwb.ai.FaDen.di.DependencyBinder;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/rest")
public class MyApplication extends ResourceConfig {

    public MyApplication() {
        register(new DependencyBinder());
        packages("htwb.ai.FaDen.services");
        register(AuthenticationFilter.class);
    }
}

