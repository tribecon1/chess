package dataaccess.dao.memoryDao;


import dataaccess.dao.AuthDao;
import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDao implements AuthDao {

    HashMap<String, String> authDataMap;

    @Override
    public AuthData createAuth(String username){
        String authToken = UUID.randomUUID().toString();
        authDataMap.put(authToken, username);
        return new AuthData(authToken, username);
    }

    @Override
    public AuthData getAuth(String authToken) {
        if(authDataMap.containsKey(authToken)){
            return new AuthData(authToken, authDataMap.get(authToken));
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
