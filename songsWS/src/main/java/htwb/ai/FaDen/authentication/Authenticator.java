package htwb.ai.FaDen.authentication;

import htwb.ai.FaDen.bean.User;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Authenticator implements IAuthenticator {

    private Map<User, String> tokenMap = new HashMap<>();

    public boolean authenticate(String authToken) {
        return tokenMap.containsValue(authToken);
    }

    public String createToken(User user) {
        try {
            String token = Encryptor.getSHA256(user);
            if (!tokenMap.containsKey(user)) this.addTokenToSession(user, token);
            else tokenMap.replace(user, token);
            return token;
        } catch (NoSuchAlgorithmException ignored) {}
        return null;
    }

    private void addTokenToSession(User user, String token) {
        this.tokenMap.put(user, token);
    }

    public Map<User, String> getTokenMap() {
        return tokenMap;
    }
}
