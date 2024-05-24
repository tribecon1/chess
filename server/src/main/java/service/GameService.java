package service;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDao;
import dataaccess.dao.GameDao;
import model.AuthData;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
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


    private AuthData authCheckerGameService (String authToken) throws DataAccessException { //helper method
        return authDaoGS.getAuth(authToken);
    }

    public ResponseType joinGame(String authToken, JoinGameRequest req) throws DataAccessException {
        AuthData authFound = authCheckerGameService(authToken);
        if (authFound != null) {
            if (req.playerColor().equalsIgnoreCase("WHITE")) {
                GameData updatedGame = new GameData(req.gameID(), authFound.username(), null, null, null);
                return gameDaoGS.updateGame(updatedGame);
            } else if (req.playerColor().equalsIgnoreCase("BLACK")) {
                GameData updatedGame = new GameData(req.gameID(), null, authFound.username(), null, null);
                return gameDaoGS.updateGame(updatedGame);
            } else {
                return new ErrorResponse(400, "Error: bad request");
            }
        }
        return new ErrorResponse(401, "Error: unauthorized");
    }


    public ResponseType listGames(String authToken) throws DataAccessException {
        AuthData authFound = authCheckerGameService(authToken);
        if (authFound != null){
            return new ListGamesResponse(gameDaoGS.listGames());
        }
        return new ErrorResponse(401,"Error: unauthorized");
    }


    public ResponseType createGame(String authToken, CreateGameRequest req) throws DataAccessException {
        if (authCheckerGameService(authToken) != null) {
            if (!gameDaoGS.getGameByName(req.gameName())){
                return new CreateGameResponse(gameDaoGS.createGame(req.gameName()).gameID());
            }
            return new ErrorResponse(400, "Error: bad request");
        }
        return new ErrorResponse(401,"Error: unauthorized");
    }
}