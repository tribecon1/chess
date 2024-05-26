package service;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDao;
import dataaccess.dao.GameDao;
import model.AuthData;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;
import response.ListGamesResponse;


public class GameService {

    private final GameDao gameDaoGS;
    private final AuthDao authDaoGS;
    private static int gameIDCreator = 1;

    public GameService(GameDao givenGameDao, AuthDao givenAuthDao){
        this.gameDaoGS = givenGameDao;
        this.authDaoGS = givenAuthDao;
    }


    private AuthData authCheckerGameService (String authToken) throws DataAccessException { //helper method
        return authDaoGS.getAuth(authToken);
    }

    public void joinGame(String authToken, JoinGameRequest req) throws DataAccessException {
        AuthData authFound = authCheckerGameService(authToken);
        if (authFound != null) {
            GameData currGame = gameDaoGS.getGame(req.gameID());
            if (currGame == null || req.playerColor() == null || req.gameID() == 0) {
                throw new DataAccessException("Error: bad request");
            }
            else {
                if (req.playerColor().equalsIgnoreCase("WHITE")) {
                    if (currGame.whiteUsername() == null){
                        GameData updatedGame = new GameData(req.gameID(), authFound.username(), currGame.blackUsername(), currGame.gameName(), currGame.game());
                        gameDaoGS.updateGame(currGame, updatedGame);
                        return;
                    }
                    else{
                        throw new DataAccessException("Error: already taken");
                    }
                }
                else if (req.playerColor().equalsIgnoreCase("BLACK")) {
                    if (currGame.blackUsername() == null){
                        GameData updatedGame = new GameData(req.gameID(), currGame.whiteUsername(), authFound.username(), currGame.gameName(), currGame.game());
                        gameDaoGS.updateGame(currGame, updatedGame);
                        return;
                    }
                    else{
                        throw new DataAccessException("Error: already taken");
                    }
                }
                else {
                    throw new DataAccessException("Error: bad request");
                }
            }
        }
        throw new DataAccessException("Error: unauthorized");
    }


    public ListGamesResponse listGames(String authToken) throws DataAccessException {
        AuthData authFound = authCheckerGameService(authToken);
        if (authFound != null){
            return new ListGamesResponse(gameDaoGS.listGames());
        }
        throw new DataAccessException("Error: unauthorized");
    }


    public CreateGameResponse createGame(String authToken, CreateGameRequest req) throws DataAccessException {
        if (authCheckerGameService(authToken) != null) {
            if (req.gameName() != null && !gameDaoGS.getGameByName(req.gameName())){
                return new CreateGameResponse(gameDaoGS.createGame(gameIDCreator++, req.gameName()).gameID());
            }
            throw new DataAccessException("Error: bad request");
        }
        throw new DataAccessException("Error: unauthorized");
    }
}
