package htwb.ai.FaDen.authentication;

import htwb.ai.FaDen.bean.User;

import java.util.Map;

public interface IAuthenticator {

    boolean authenticate(String authToken);
    String createToken(User user);
    Map<User, String> getTokenMap();
}
