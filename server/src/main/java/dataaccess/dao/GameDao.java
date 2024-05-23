package dataaccess.dao;

import dataaccess.DataAccessException;
import model.GameData;

import java.util.Collection;

public interface GameDao {

    void createGame(String gameName) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    void updateGame(GameData game) throws DataAccessException; //should have access to the AuthData obj. w/ username if need be

    void clearGame() throws DataAccessException;

}
