package dataaccess.dao.memoryDao;

import dataaccess.DataAccessException;
import dataaccess.dao.UserDao;
import model.UserData;

import java.util.ArrayList;

public class MemoryUserDao implements UserDao {

    ArrayList<UserData> users;

    public MemoryUserDao() {
        this.users = new ArrayList<>();
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        UserData newUser = new UserData(username, password, email);
        users.add(newUser);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        for (UserData user : users) {
            if (user.username().equals(username)){
                return user;
            }
        }
        return null;
    }

    @Override
    public void clearUser() throws DataAccessException {
        users.clear();
    }
}
