package dataaccess.dao.memoryDao;


import dataaccess.dao.AuthDao;
import model.AuthData;

import java.util.HashMap;


public class MemoryAuthDao implements AuthDao {

    HashMap<String, String> authDataMap;

    public MemoryAuthDao() {
        this.authDataMap = new HashMap<>();
    }


    @Override
    public AuthData createAuth(String username, String authToken) {
        //String authToken = UUID.randomUUID().toString();
        authDataMap.put(authToken, username);
        return new AuthData(username, authToken);
    }

    @Override
    public AuthData getAuth(String authToken) {
        if(authDataMap.containsKey(authToken)){
            return new AuthData(authDataMap.get(authToken), authToken);
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {
        authDataMap.remove(authToken);
    }

    @Override
    public void clearAuth() {
        authDataMap.clear();
    }


}
