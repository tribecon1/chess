package service;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDao;
import dataaccess.dao.UserDao;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import request.LoginRequest;

import java.sql.SQLException;
import java.util.UUID;

public class UserService {
    private final UserDao userDaoUS;
    private final AuthDao authDaoUS;

    public UserService(UserDao givenUserDao, AuthDao givenAuthDao){
        this.userDaoUS = givenUserDao;
        this.authDaoUS = givenAuthDao;
    }

    public AuthData register(UserData user) throws DataAccessException {
        if (this.userDaoUS.getUser(user.username()) == null){
            try {
                this.userDaoUS.createUser(user.username(), user.password(), user.email());
            }
            catch (SQLException e){
                throw new DataAccessException(String.format("Database Error: %s", e.getMessage()));
            }
            String authToken = UUID.randomUUID().toString();
            return this.authDaoUS.createAuth(user.username(), authToken);
        }
        throw new DataAccessException("Error: already taken");
    }

    public AuthData login(LoginRequest loginRequest) throws DataAccessException {
        if (loginRequest.username() == null || loginRequest.password() == null){
            throw new DataAccessException("Error: bad request");
        }
        if (this.userDaoUS.getUser(loginRequest.username()) != null){
            if (BCrypt.checkpw(loginRequest.password(), this.userDaoUS.getUser(loginRequest.username()).password())  ){//BCrypt check
                String authToken = UUID.randomUUID().toString();
                return this.authDaoUS.createAuth(loginRequest.username(), authToken);
            }
        }
        throw new DataAccessException("Error: unauthorized");
    }


    public void logout(String authToken) throws DataAccessException {
        if (this.authDaoUS.getAuth(authToken) != null){
            this.authDaoUS.deleteAuth(authToken);
            return;
        }
        throw new DataAccessException("Error: unauthorized");
    }

}
