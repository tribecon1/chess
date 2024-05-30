package dataaccess.dao.sqldao;

import dataaccess.DataAccessException;
import dataaccess.dao.GameDao;
import model.GameData;

import java.util.Collection;
import java.util.List;

public class SqlGameDao implements GameDao {


    @Override
    public GameData createGame(int gameID, String gameName) throws DataAccessException {
        return null;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public void updateGame(GameData currGame, GameData updatedGame) throws DataAccessException {

    }

    @Override
    public void clearGame() throws DataAccessException {

    }

    @Override
    public boolean getGameByName(String gameName) throws DataAccessException {
        return false;
    }

    @Override
    public int getDatabaseSize() {
        return 0;
    }
}
