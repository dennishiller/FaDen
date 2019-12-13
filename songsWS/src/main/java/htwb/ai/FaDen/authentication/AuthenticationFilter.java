package htwb.ai.FaDen.authentication;


import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Provider
public class AuthenticationFilter implements ContainerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Inject
    private IAuthenticator authenticator;

    @Override
    public void filter(ContainerRequestContext containerRequest) throws WebApplicationException {

        String authToken = containerRequest.getHeaderString(AUTHORIZATION_HEADER);

        if (authToken == null || authToken.trim().isEmpty()) {
            // ^^^ bedeutet, dass HTTP-Header "Authorization"
            // nicht mitgeschickt wurde oder keinen Wert hatte
            // Das kann zwei Gruende haben:
            // 1) Client hat noch keinen Token und will zu /auth?userId=...&key=...
            // 2) Client hat keinen Token mitgeschickt und will zu /songs
            // Wenn 1), dann nix machen = durchlassen
            // Wenn 2), dann nicht durchlassen = WebApplicationException werfen
            if (!containerRequest.getUriInfo().getPath().startsWith("auth")) {
                throw new WebApplicationException(Status.UNAUTHORIZED);
            }
        } else {
            if (!authenticator.authenticate(authToken)) { // Token existiert nicht
                throw new WebApplicationException(Status.UNAUTHORIZED);
            }
        }
    }
}
