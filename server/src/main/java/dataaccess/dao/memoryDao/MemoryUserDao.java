package dataaccess.dao.memoryDao;

import dataaccess.dao.UserDao;
import model.UserData;

public class MemoryUserDao implements UserDao {
    @Override
    public UserData createUser(String username, String password, String email) {
        return null;
    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void clearUser() {

    }
}
