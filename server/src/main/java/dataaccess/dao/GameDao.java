package dataaccess.dao;

import dataaccess.DataAccessException;
import model.GameData;

import java.util.Collection;

public interface GameDao {

    GameData createGame(String gameName) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    void updateGame(GameData currGame, GameData updatedGame) throws DataAccessException;

    void clearGame() throws DataAccessException;

    boolean getGameByName(String gameName) throws DataAccessException;

    int getDatabaseSize();
}
