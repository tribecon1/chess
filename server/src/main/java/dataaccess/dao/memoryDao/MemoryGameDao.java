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
        return gameDataList;
    }

    @Override
    public ResponseType updateGame(GameData updatedGame) {
        GameData currGame = getGame(updatedGame.gameID());
        GameData changedGame;
        if (currGame != null) {
            if (updatedGame.whiteUsername() != null) {
                if (currGame.whiteUsername() == null) {
                    changedGame = new GameData(currGame.gameID(), updatedGame.whiteUsername(), currGame.blackUsername(), currGame.gameName(), currGame.game());
                    //for when the white team has a player that joined
                }
                else {
                    return new ErrorResponse(403, "Error: already taken");
                }
            }
            else if (updatedGame.blackUsername() != null) {
                if (currGame.blackUsername() == null) {
                    changedGame = new GameData(currGame.gameID(), currGame.whiteUsername(), updatedGame.blackUsername(), currGame.gameName(), currGame.game());
                    //for when the black team has a player that joined
                }
                else {
                    return new ErrorResponse(403, "Error: already taken");
                }
            }
            else if (updatedGame.game() != null){
                changedGame = new GameData(currGame.gameID(), currGame.whiteUsername(), currGame.blackUsername(), currGame.gameName(), updatedGame.game());
                //for when a move is made. WHEN DOES THIS OCCUR?
            }
            else {
                return new ErrorResponse(400, "Error: bad request");
            }
            gameDataList.remove(currGame);
            gameDataList.add(changedGame);
            return null;
        }
        else{
            return new ErrorResponse(400, "Error: bad request");
        }
    }


    @Override
    public void clearGame() {
        gameDataList.clear();
    }

}
