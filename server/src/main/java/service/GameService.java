package service;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDao;
import dataaccess.dao.GameDao;
import dataaccess.dao.UserDao;
import dataaccess.dao.memoryDao.MemoryAuthDao;
import dataaccess.dao.memoryDao.MemoryGameDao;
import model.AuthData;
import request.JoinGameRequest;
import response.ErrorResponse;
import response.ListGamesResponse;
import response.ResponseType;


public class GameService {

    private final GameDao gameDaoGS;
    private final AuthDao authDaoGS;

    public GameService(GameDao givenGameDao, AuthDao givenAuthDao){
        this.gameDaoGS = givenGameDao;
        this.authDaoGS = givenAuthDao;
    }



    public void joinGame(JoinGameRequest req, String authToken){



    }

    public ResponseType listGames(String authToken) throws DataAccessException {
        AuthData authFound = authDaoGS.getAuth(authToken);
        if (authFound != null) {
            return new ListGamesResponse(gameDaoGS.listGames());
        }
        return new ErrorResponse("Error: unauthorized");
    }



}
