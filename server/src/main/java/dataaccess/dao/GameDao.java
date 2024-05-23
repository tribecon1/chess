package dataaccess.dao;

import dataaccess.DataAccessException;
import model.GameData;
import response.ResponseType;

import java.util.Collection;

public interface GameDao {

    void createGame(String gameName) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    ResponseType updateGame(GameData game) throws DataAccessException;

    void clearGame() throws DataAccessException;

}
