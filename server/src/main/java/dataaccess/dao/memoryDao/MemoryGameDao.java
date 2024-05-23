package dataaccess.dao.memoryDao;

import chess.ChessGame;
import dataaccess.dao.GameDao;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;


public class MemoryGameDao implements GameDao {

    ArrayList<GameData> gameDataList = new ArrayList<>();

    @Override
    public void createGame(String gameName) {
        GameData newGame = new GameData(gameDataList.size()+1, null, null, gameName, new ChessGame());
        gameDataList.add(newGame);
    }

    @Override
    public GameData getGame(int gameID) {
        for (GameData game : gameDataList) {
            if (game.gameID() == gameID){
                return game;
            }
        }
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        return gameDataList;
    }

    @Override
    public void updateGame(GameData updatedGame) {
        GameData currGame = getGame(updatedGame.gameID());
        if ( (currGame.whiteUsername() == null && updatedGame.whiteUsername() != null) ||
             (currGame.blackUsername() == null && updatedGame.blackUsername() != null) ||
             (!currGame.game().equals(updatedGame.game())) ) {
            gameDataList.remove(currGame);
            gameDataList.add(updatedGame);
            //Checking if A) whiteUsername was given, B) blackUsername was given, or C) a move was made
        }
        //if none of those conditions are satisfied, then how do I raise the error?
    }

    @Override
    public void clearGame() {
        gameDataList.clear();
    }
}
