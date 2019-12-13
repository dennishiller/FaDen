package htwb.ai.FaDen.services;

import htwb.ai.FaDen.authentication.Authenticator;
import htwb.ai.FaDen.authentication.IAuthenticator;
import htwb.ai.FaDen.bean.User;
import htwb.ai.FaDen.dao.IUserDao;

import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/auth")
public class UserService {

    @Inject
    private IUserDao userDao;

    @Inject
    private IAuthenticator authenticator;

    @GET
    @Produces({MediaType.TEXT_PLAIN})
    public Response createToken(@QueryParam("userId") String id, @QueryParam("key") String key) {
        if (id == null || id.isEmpty()) return Response.status(Response.Status.BAD_REQUEST).entity("Could not find QueryParams userId").build();
        if (key == null || key.isEmpty()) return Response.status(Response.Status.BAD_REQUEST).entity("Could not find QueryParam key").build();
        try {
            User user = userDao.getUser(id, key);
            if (user == null) return Response.status(Response.Status.UNAUTHORIZED).build();
            String token = authenticator.createToken(user);
            return Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN_TYPE).entity(token).build();
        } catch (PersistenceException e) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Database error. We fucked up, sorry").build();
        }
    }
}
