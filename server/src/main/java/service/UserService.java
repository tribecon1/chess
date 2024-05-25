package service;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDao;
import dataaccess.dao.UserDao;
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
        if (this.userDaoUS.getUser(user.username()) == null){
            this.userDaoUS.createUser(user.username(), user.password(), user.email());
            return this.authDaoUS.createAuth(user.username());
        }
        return new ErrorResponse(403, "Error: already taken");
    }

    public ResponseType login(LoginRequest loginRequest) throws DataAccessException {
        if (this.userDaoUS.getUser(loginRequest.username()) != null){
            return this.authDaoUS.createAuth(loginRequest.username());
        }
        return new ErrorResponse(401, "Error: unauthorized");
    }


    public ResponseType logout(String authToken) throws DataAccessException {
        if (this.authDaoUS.getAuth(authToken) != null){
            this.authDaoUS.deleteAuth(authToken);
            return null;
        }
        return new ErrorResponse(401, "Error: unauthorized");
    }



}
