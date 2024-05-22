package dataaccess.dao;

import model.GameData;

import java.util.Collection;

public interface GameDao {

    GameData createGame(String gameName);

    GameData getGame(int gameID);

    Collection<GameData> listGames();

    void updateGame(GameData game); //should have access to the AuthData obj. w/ username if need be

    void clearGame();

}
