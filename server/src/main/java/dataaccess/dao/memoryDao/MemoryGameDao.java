package dataaccess.dao.memoryDao;

import dataaccess.dao.GameDao;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MemoryGameDao implements GameDao {

    HashMap<Integer, GameData> gameDataMap;

    @Override
    public GameData createGame(String gameName) {
        return null;
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }

    @Override
    public void updateGame(GameData game) {

    }

    @Override
    public void clearGame() {

    }
}
