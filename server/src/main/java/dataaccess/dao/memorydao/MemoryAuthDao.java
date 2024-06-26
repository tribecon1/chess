package dataaccess.dao.memorydao;


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

    @Override
    public int getDatabaseSize(){
        return authDataMap.size();
    }

}
