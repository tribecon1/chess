package dataaccess.dao.memoryDao;

import chess.ChessGame;
import dataaccess.dao.GameDao;
import model.GameData;
import response.ErrorResponse;
import response.ResponseType;

import java.util.ArrayList;
import java.util.Collection;


public class MemoryGameDao implements GameDao {

    ArrayList<GameData> gameDataList;

    public MemoryGameDao(){
        this.gameDataList = new ArrayList<>();
    }

    @Override
    public GameData createGame(String gameName) {
        GameData newGame = new GameData(gameDataList.size()+1, null, null, gameName, new ChessGame());
        gameDataList.add(newGame);
        return newGame;
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
    public boolean getGameByName(String gameName){
        for (GameData game : gameDataList) {
            if (game.gameName().equals(gameName)){
                return true;
            }
        }
        return false;
    }


    @Override
    public Collection<GameData> listGames() {
        Collection<GameData> userFriendlyGameList = new ArrayList<>();
        for (GameData game : gameDataList) {
            userFriendlyGameList.add(new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), null));
        }
        //return gameDataList;
        return userFriendlyGameList;
    }

    @Override
    public ResponseType updateGame(GameData currGame, GameData updatedGame) {
            gameDataList.remove(currGame);
            gameDataList.add(updatedGame);
            return null; //how to account for when a move is made as an update
    }


    @Override
    public void clearGame() {
        gameDataList.clear();
    }

}
