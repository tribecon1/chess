package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import dataaccess.dao.AuthDao;
import dataaccess.dao.GameDao;
import dataaccess.dao.UserDao;
import dataaccess.dao.sqldao.SqlAuthDao;
import dataaccess.dao.sqldao.SqlGameDao;
import dataaccess.dao.sqldao.SqlUserDao;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.SystemService;

import java.util.ArrayList;

public class GameSqlTests {

    private final GameDao gameDao = new SqlGameDao();
    private final AuthDao authDao = new SqlAuthDao();
    private final UserDao userDao = new SqlUserDao();


    @BeforeEach
    void preTest() throws DataAccessException {
        DatabaseManager.createDatabase();
        new SystemService(gameDao, authDao, userDao).clear();
    }

    @Test
    void createGameSuccess() throws DataAccessException {
        assert (gameDao.getDatabaseSize() == 0);
        GameData newGame = gameDao.createGame("MyNewGame!");
        Assertions.assertNotNull(newGame);
        assert (gameDao.getDatabaseSize() == 1);
    }

    @Test
    void createGameNullFailure() throws DataAccessException {
        assert (gameDao.getDatabaseSize() == 0);
        Assertions.assertThrows(DataAccessException.class, () -> gameDao.createGame(null));
        assert (gameDao.getDatabaseSize() == 0);
    }

    @Test
    void createGameSameNameFailure() throws DataAccessException {
        createGameSuccess();
        Assertions.assertThrows(DataAccessException.class, () -> gameDao.createGame("MyNewGame!"));
        assert (gameDao.getDatabaseSize() == 1);
    }

    @Test
    void getGameSuccess() throws DataAccessException {
        GameData newGame = gameDao.createGame("ToBeRetrieved!");
        assert (gameDao.getGame(newGame.gameID()) != null);
    }
    //DO NOT FORGET TO DO A joinGame but with a board you've changed by making a move to prove that they are different! Assert it

    @Test
    void getGameWrongIDFailure() throws DataAccessException {
        Assertions.assertNull(gameDao.getGame(43));
    }

    @Test
    void getGameByNameSuccess() throws DataAccessException {
        GameData newGame = gameDao.createGame("ToBeRetrieved!");
        Assertions.assertTrue(gameDao.getGameByName(newGame.gameName()));
    }

    @Test
    void getGameByNameWrongNameFailure() throws DataAccessException {
        Assertions.assertFalse(gameDao.getGameByName("He Don't Exist!"));
    }

    @Test
    void listGamesSuccess() throws DataAccessException {
        gameDao.createGame("Game1!");
        gameDao.createGame("Game2!");
        gameDao.createGame("Game3!");
        assert (gameDao.listGames() instanceof ArrayList);
        assert (gameDao.listGames().size() == 3);
    }

    @Test
    void listGamesFailure() throws DataAccessException {
        assert (gameDao.listGames().isEmpty());
    }

    @Test
    void updateGameNewTeam() throws DataAccessException {
        GameData newGame = gameDao.createGame("New Game!");
        assert (newGame.blackUsername() == null);
        GameData joinedGame = new GameData(newGame.gameID(), newGame.whiteUsername(), "MyUsername", newGame.gameName(), newGame.game());
        gameDao.updateGame(newGame, joinedGame);
        assert (gameDao.getGame(newGame.gameID()).blackUsername().equals("MyUsername"));
    }

    @Test
    void updateGameChangedGame() throws DataAccessException, InvalidMoveException { //moves white pawn at (Row 2, Col 1) up 1 space as an update
        GameData newGame = gameDao.createGame("New Game!");
        assert (newGame.game() != null);
        ChessGame oldGame = newGame.game();
        newGame.game().makeMove(new ChessMove(new ChessPosition(2,1), new ChessPosition(3,1), null));
        GameData moveMade = new GameData(newGame.gameID(), newGame.whiteUsername(), newGame.blackUsername(), newGame.gameName(), newGame.game());
        gameDao.updateGame(newGame, moveMade);
        assert (!gameDao.getGame(newGame.gameID()).game().equals(oldGame));
    }

    @Test
    void updateGameWrongIDFailure() throws DataAccessException { //feeds updateGame w/ the wrong current game/wrong Game ID to find it
        GameData newGame = gameDao.createGame("New Game!");
        GameData misleadingGame = new GameData(57, null, null, "NeverRead", new ChessGame());
        GameData wastedGame = new GameData(newGame.gameID(), "BigDawg", newGame.blackUsername(), newGame.gameName(), newGame.game());
        gameDao.updateGame(misleadingGame, wastedGame);
        assert (gameDao.getGame(newGame.gameID()).whiteUsername() == null);
    }

    @Test
    void clearGameTable() throws DataAccessException {
        gameDao.createGame("New Game 1!");
        gameDao.createGame("New Game 2!");
        gameDao.createGame("New Game 3!");
        assert (gameDao.getDatabaseSize() == 3);
        gameDao.clearGame();
        DatabaseManager.createTables();
        assert (gameDao.getDatabaseSize() == 0);
    }

    @Test
    void getDatabaseSizeSuccess() throws DataAccessException {
        createGameSuccess();
        gameDao.createGame("New Game 2!");
        assert (gameDao.getDatabaseSize() == 2);
    }

    @Test
    void getDatabaseSizeFailure() throws DataAccessException {
        getDatabaseSizeSuccess();
        gameDao.clearGame();
        Assertions.assertThrows(DataAccessException.class, gameDao::getDatabaseSize);
    }


}
