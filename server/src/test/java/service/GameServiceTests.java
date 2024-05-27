package service;


import dataaccess.DataAccessException;
import dataaccess.dao.AuthDao;
import dataaccess.dao.GameDao;
import dataaccess.dao.memorydao.MemoryAuthDao;
import dataaccess.dao.memorydao.MemoryGameDao;
import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.JoinGameRequest;
import response.CreateGameResponse;


public class GameServiceTests {

    private static GameService gameService;
    private static GameDao givenGameDao;
    private static AuthDao givenAuthDao;
    private static AuthData authorizedTester;
    private static DataAccessException thrownError;


    @BeforeAll
    public static void setUp() throws DataAccessException {
        givenGameDao = new MemoryGameDao();
        givenGameDao.createGame(3, "presetGame");
        givenAuthDao = new MemoryAuthDao();
        authorizedTester = givenAuthDao.createAuth("testUser", "myAuthToken");

        gameService = new GameService(givenGameDao, givenAuthDao);
    }

    @AfterEach
    public void cleanUp() throws DataAccessException {
        setUp();
    }

    //createGame Positive and Negative Tests
    @Test
    public void createGameSuccess() throws DataAccessException {
        assert (gameService.createGame(authorizedTester.authToken(), new CreateGameRequest("MyNewGame!")) != null);
    }
    @Test
    public void createGameFailure1() { //tests an empty game name
        CreateGameRequest badGameReq = new CreateGameRequest(null);
        thrownError = Assertions.assertThrows(DataAccessException.class, () -> gameService.createGame(authorizedTester.authToken(), badGameReq));
        assert (thrownError.getMessage().equals("Error: bad request"));
    }
    @Test
    public void createGameFailure2() {
        CreateGameRequest goodGameReq = new CreateGameRequest("MyOtherNewGame!");
        thrownError = Assertions.assertThrows(DataAccessException.class, () -> gameService.createGame("fakeToken", goodGameReq));
        assert (thrownError.getMessage().equals("Error: unauthorized"));
    }


    //listGames Success and Failure Tests
    @Test
    public void listGamesSuccess() throws DataAccessException {
        gameService.createGame(authorizedTester.authToken(), new CreateGameRequest("MyNewGame!"));
        assert (gameService.listGames(authorizedTester.authToken()) != null);
        assert (givenGameDao.getDatabaseSize() == 2);
    }
    @Test
    public void listGamesFailure() throws DataAccessException {
        gameService.createGame(authorizedTester.authToken(), new CreateGameRequest("MyNewGame!"));
        thrownError = Assertions.assertThrows(DataAccessException.class, () -> gameService.listGames("fakeToken"));
        assert (thrownError.getMessage().equals("Error: unauthorized"));
    }


    //joinGame Success and Failure Tests
    @Test
    public void joinGameSuccess() throws DataAccessException {
        CreateGameResponse newGameResponse = gameService.createGame(authorizedTester.authToken(), new CreateGameRequest("JoinMyGame"));
        Assertions.assertDoesNotThrow(() -> gameService.joinGame(authorizedTester.authToken(), new JoinGameRequest("WHITE", newGameResponse.gameID()) ) );
    }
    @Test
    public void joinGameFailure1() throws DataAccessException {
        CreateGameResponse newGameResponse = gameService.createGame(authorizedTester.authToken(), new CreateGameRequest("JoinMeOrElse"));
        gameService.joinGame(authorizedTester.authToken(), new JoinGameRequest("WHITE", newGameResponse.gameID()));
        thrownError = Assertions.assertThrows(DataAccessException.class, () ->
                gameService.joinGame(authorizedTester.authToken(), new JoinGameRequest("WHITE", newGameResponse.gameID()) ) );
        assert (thrownError.getMessage().equals("Error: already taken"));
    }
    @Test
    public void joinGameFailure2() throws DataAccessException {
        CreateGameResponse newGameResponse = gameService.createGame(authorizedTester.authToken(), new CreateGameRequest("Join"));

        thrownError = Assertions.assertThrows(DataAccessException.class, () ->
                gameService.joinGame("fakeToken", new JoinGameRequest("WHITE", newGameResponse.gameID()) ) );
        assert (thrownError.getMessage().equals("Error: unauthorized"));
    }

    @Test
    public void joinGameFailure3() throws DataAccessException {
        CreateGameResponse newGameResponse = gameService.createGame(authorizedTester.authToken(), new CreateGameRequest("Join"));
        thrownError = Assertions.assertThrows(DataAccessException.class, () ->
                gameService.joinGame(authorizedTester.authToken(), new JoinGameRequest(null, newGameResponse.gameID()) ) );
        assert (thrownError.getMessage().equals("Error: bad request"));
    }

}
