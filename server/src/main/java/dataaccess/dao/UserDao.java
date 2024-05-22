package dataaccess.dao;

import model.UserData;

public interface UserDao {

    UserData createUser(String username, String password, String email);

    UserData getUser(String username);

    void clearUser();

}
