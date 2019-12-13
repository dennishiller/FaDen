package htwb.ai.FaDen.authentication;

import htwb.ai.FaDen.bean.User;

public interface IAuthenticator {

    boolean authenticate(String authToken);
    String createToken(User user);
}
