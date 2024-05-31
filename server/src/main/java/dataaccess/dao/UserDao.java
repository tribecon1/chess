package dataaccess.dao;

import dataaccess.DataAccessException;
import model.UserData;

import java.sql.SQLException;

public interface UserDao {

    void createUser(String username, String password, String email) throws DataAccessException, SQLException;

    UserData getUser(String username) throws DataAccessException;

    void clearUser() throws DataAccessException;

    int getDatabaseSize() throws DataAccessException;
}
