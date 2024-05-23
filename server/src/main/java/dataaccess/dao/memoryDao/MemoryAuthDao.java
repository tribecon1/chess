package dataaccess.dao.memoryDao;


import dataaccess.DataAccessException;
import dataaccess.dao.AuthDao;
import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDao implements AuthDao {

    HashMap<String, String> authDataMap;

    public MemoryAuthDao() {
        this.authDataMap = new HashMap<>(); //constructor needed?
    }


    @Override
    public AuthData createAuth(String username) throws DataAccessException{
        String authToken = UUID.randomUUID().toString();
        authDataMap.put(authToken, username);
        return new AuthData(authToken, username);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException{
        if(authDataMap.containsKey(authToken)){
            return new AuthData(authToken, authDataMap.get(authToken));
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException{
        authDataMap.remove(authToken);
    }

    @Override
    public void clearAuth() throws DataAccessException{
        authDataMap.clear();
    }


}
