package htwb.ai.FaDen.di;

import htwb.ai.FaDen.authentication.AuthenticationFilter;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/rest")
public class ResourceConfigTest extends ResourceConfig {

    public ResourceConfigTest() {
        register(new DependencyBinderTest());
        packages("htwb.ai.FaDen.services");
        //register(AuthenticationFilter .class);
    }
}
