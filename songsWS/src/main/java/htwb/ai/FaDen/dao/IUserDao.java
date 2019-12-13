package htwb.ai.FaDen.dao;

import htwb.ai.FaDen.bean.User;

public interface IUserDao {

    User getUser(String id, String key);
}
