package service;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDao;
import dataaccess.dao.UserDao;
import model.UserData;
import request.LoginRequest;
import response.ErrorResponse;
import response.ResponseType;

import java.util.UUID;

public class UserService {
    private final UserDao userDaoUS;
    private final AuthDao authDaoUS;

    public UserService(UserDao givenUserDao, AuthDao givenAuthDao){
        this.userDaoUS = givenUserDao;
        this.authDaoUS = givenAuthDao;
    }

    public ResponseType register(UserData user) throws DataAccessException {
        if (user.username() == null || user.username().isEmpty() || user.password() == null){
            //return new ErrorResponse(400, "Error: bad request");
            throw new DataAccessException("Error: bad request");
        }
        if (this.userDaoUS.getUser(user.username()) == null){
            this.userDaoUS.createUser(user.username(), user.password(), user.email());
            String authToken = UUID.randomUUID().toString();
            return this.authDaoUS.createAuth(user.username(), authToken);
        }
        //return new ErrorResponse(403, "Error: already taken");
        throw new DataAccessException("Error: already taken");
    }

    public ResponseType login(LoginRequest loginRequest) throws DataAccessException {
        if (loginRequest.username() == null || loginRequest.password() == null){
            //return new ErrorResponse(400, "Error: bad request");
            throw new DataAccessException("Error: bad request");
        }
        if (this.userDaoUS.getUser(loginRequest.username()) != null){
            if (this.userDaoUS.getUser(loginRequest.username()).password().equals(loginRequest.password())){
                String authToken = UUID.randomUUID().toString();
                return this.authDaoUS.createAuth(loginRequest.username(), authToken);
            }
        }
        //return new ErrorResponse(401, "Error: unauthorized");
        throw new DataAccessException("Error: unauthorized");
    }


    public ResponseType logout(String authToken) throws DataAccessException {
        if (this.authDaoUS.getAuth(authToken) != null){
            this.authDaoUS.deleteAuth(authToken);
            return null;
        }
        //return new ErrorResponse(401, "Error: unauthorized");
        throw new DataAccessException("Error: unauthorized");
    }

}
