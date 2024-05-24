package service;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDao;
import dataaccess.dao.UserDao;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import response.ErrorResponse;
import response.ResponseType;

public class UserService {
    private final UserDao userDaoUS;
    private final AuthDao authDaoUS;

    public UserService(UserDao givenUserDao, AuthDao givenAuthDao){
        this.userDaoUS = givenUserDao;
        this.authDaoUS = givenAuthDao;
    }


    public ResponseType register(UserData user) throws DataAccessException {
        if (userDaoUS.getUser(user.username()) == null){
            userDaoUS.createUser(user.username(), user.password(), user.email());
            return authDaoUS.createAuth(user.username());
        }
        return new ErrorResponse(403, "Error: already taken");
    }

    public ResponseType login(LoginRequest loginRequest) throws DataAccessException {
        if (userDaoUS.getUser(loginRequest.username()) != null){
            return authDaoUS.createAuth(loginRequest.username());
        }
        return new ErrorResponse(401, "Error: unauthorized");
    }


    public ResponseType logout(String authToken) throws DataAccessException {
        if (authDaoUS.getAuth(authToken) != null){
            authDaoUS.deleteAuth(authToken);
            return null;
        }
        return new ErrorResponse(401, "Error: unauthorized");
    }



}
