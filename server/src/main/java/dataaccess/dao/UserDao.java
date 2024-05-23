package dataaccess.dao;

import dataaccess.DataAccessException;
import model.UserData;

public interface UserDao {

    void createUser(String username, String password, String email) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void clearUser() throws DataAccessException;

}
