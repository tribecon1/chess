package service;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDao;
import dataaccess.dao.UserDao;
import model.AuthData;
import model.UserData;
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

        return new ErrorResponse("Error: already taken");
    }

    public AuthData login(UserData user) {



        return null;
    }


    public void logout(UserData user) {}



}
