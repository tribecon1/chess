package dataaccess.dao;

import model.AuthData;

public interface AuthDao {

    AuthData createAuth(String username);
    //will generate authToken w/in overridden method call
    //necessary to return AuthData object, or just void?

    AuthData getAuth(String authToken);

    void deleteAuth(String authToken);

    void clearAuth();

}
