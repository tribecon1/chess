package service;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDao;
import dataaccess.dao.GameDao;
import dataaccess.dao.UserDao;
import response.ResponseType;

public class SystemService {

    private final GameDao gameDaoSS;
    private final AuthDao authDaoSS;
    private final UserDao userDaoSS;

    public SystemService(GameDao givenGameDao, AuthDao givenAuthDao, UserDao givenUserDao){
        this.gameDaoSS = givenGameDao;
        this.authDaoSS = givenAuthDao;
        this.userDaoSS = givenUserDao;
    }


    public ResponseType clear() throws DataAccessException {
        this.gameDaoSS.clearGame();
        this.authDaoSS.clearAuth();
        this.userDaoSS.clearUser();
        return null;
    }
}
