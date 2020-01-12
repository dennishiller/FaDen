package htwb.ai.FaDen.dao;

import htwb.ai.FaDen.bean.User;

import java.util.Collection;

public interface IUserDao {

    User getUser(String id, String key);
    Collection<User> getAllUser();
}
