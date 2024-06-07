package client;

import com.google.gson.JsonSyntaxException;
import dataaccess.DataAccessException;
import dataaccess.dao.AuthDao;
import dataaccess.dao.GameDao;
import dataaccess.dao.UserDao;
import dataaccess.dao.sqldao.SqlAuthDao;
import dataaccess.dao.sqldao.SqlGameDao;
import dataaccess.dao.sqldao.SqlUserDao;
import model.UserData;
import net.ServerFacade;
import org.junit.jupiter.api.*;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import response.ListGamesResponse;
import server.SerializerDeserializer;
import server.Server;
import service.SystemService;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static final UserDao USER_DAO = new SqlUserDao();
    private static final AuthDao AUTH_DAO = new SqlAuthDao();
    private static final GameDao GAME_DAO = new SqlGameDao();
    private static final SystemService SYSTEM_SERVICE = new SystemService(GAME_DAO, AUTH_DAO, USER_DAO);
    private static ServerFacade serverFacade;


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void cleanup() throws DataAccessException {
        SYSTEM_SERVICE.clear();
    }


    @Test
    void registerSuccess() throws Exception {
        var authToken = serverFacade.register(new UserData("player1", "password", "p1@email.com"));
        assertTrue(!authToken.contains("Error") && authToken.length() > 10);
    }

    @Test
    void registerFailure() throws Exception {
        var responseText = serverFacade.register(new UserData(null, "useless", "p1@email.com"));
        assertEquals("Error: bad request", responseText);
    }


    @Test
    void logoutSuccess() throws Exception {
        USER_DAO.createUser("toBeRemoved", "password", "p1@email.com");
        AUTH_DAO.createAuth("toBeRemoved", "authToken");
        assert (AUTH_DAO.getDatabaseSize() == 1);
        serverFacade.logout("authToken");
        assert (AUTH_DAO.getDatabaseSize() == 0);
    }

    @Test
    void logoutFailure() throws Exception {
        USER_DAO.createUser("toBeRemoved", "password", "p1@email.com");
        AUTH_DAO.createAuth("toBeRemoved", "authToken");
        assert (AUTH_DAO.getDatabaseSize() == 1);
        assertEquals("Error: unauthorized", serverFacade.logout("wrongAuthToken"));
        assert (AUTH_DAO.getDatabaseSize() == 1);
    }

    @Test
    void logoutFailure2() throws Exception {
        assert (AUTH_DAO.getDatabaseSize() == 0);
        assertEquals("Error: unauthorized", serverFacade.logout("uselessAuthToken"));
        assert (AUTH_DAO.getDatabaseSize() == 0);
    }


    @Test
    void loginSuccess() throws Exception {
        var oldAuthToken = serverFacade.register(new UserData("player1", "password", "p1@email.com"));
        serverFacade.logout(oldAuthToken);//needed w/ regards to encrypted passwords
        assert (AUTH_DAO.getDatabaseSize() == 0);
        String newAuthToken = serverFacade.login(new LoginRequest("player1","password"));
        assertTrue(!newAuthToken.contains("Error") && newAuthToken.length() > 10);
    }

    @Test
    void loginFailure() throws Exception {
        var oldAuthToken = serverFacade.register(new UserData("player1", "password", "p1@email.com"));
        serverFacade.logout(oldAuthToken);
        assert (AUTH_DAO.getDatabaseSize() == 0);
        String errorText = serverFacade.login(new LoginRequest("player1","wrongpasswordpal!"));
        assertTrue(errorText.contains("Error: unauthorized"));
    }

    @Test
    void loginFailure2() throws Exception {
        assert (USER_DAO.getDatabaseSize() == 0);
        assert (AUTH_DAO.getDatabaseSize() == 0);
        String errorText = serverFacade.login(new LoginRequest("nonExistent","futile"));
        assertTrue(errorText.contains("Error: unauthorized"));
    }

    @Test
    void createGameSuccess() throws Exception {
        var authData = AUTH_DAO.createAuth("temp", "authToken");
        String responseText = serverFacade.createGame(new CreateGameRequest("Brand New Game!"), authData.authToken());
        assertFalse(responseText.contains("Error"));
        Assertions.assertDoesNotThrow(() -> Integer.parseInt(responseText));
    }

    @Test
    void createGameFailure() throws Exception {
        String responseText = serverFacade.createGame(new CreateGameRequest("Brand New Game!"), "notRealToken");
        assertEquals("Error: unauthorized", responseText);
        Assertions.assertThrows(NumberFormatException.class, () -> Integer.parseInt(responseText));
    }

    @Test
    void listGamesSuccess() throws Exception {
        var authData = AUTH_DAO.createAuth("temp", "authToken");
        GAME_DAO.createGame("Game#1");
        GAME_DAO.createGame("Game#2");
        GAME_DAO.createGame("Game#3");
        String responseText = serverFacade.listGames(authData.authToken());
        assertFalse(responseText.contains("Error"));
        Assertions.assertDoesNotThrow(() -> SerializerDeserializer.convertFromJSON(responseText, ListGamesResponse.class));
        ListGamesResponse returnedList = SerializerDeserializer.convertFromJSON(responseText, ListGamesResponse.class);
        assert (returnedList.games().size() == 3);
    }

    @Test
    void listGamesFailure() throws Exception {
        GAME_DAO.createGame("Game#1");
        GAME_DAO.createGame("Game#2");
        GAME_DAO.createGame("Game#3");
        String responseText = serverFacade.listGames("falseAuthToken");
        assertTrue(responseText.contains("Error"));
        Assertions.assertThrows(JsonSyntaxException.class, () -> SerializerDeserializer.convertFromJSON(responseText, ListGamesResponse.class));
    }

    @Test
    void joinGameSuccess() throws Exception {
        var authToken = AUTH_DAO.createAuth("temp", "authToken").authToken();
        int gameID = GAME_DAO.createGame("newGame!").gameID();
        String responseText = serverFacade.joinGame(new JoinGameRequest("WHITE", gameID), authToken);
        assertFalse(responseText.contains("Error"));
        assertTrue(responseText.contains(String.valueOf(gameID)) && responseText.contains("successfully"));
    }

    @Test
    void joinGameFailure() throws Exception {
        var authToken = AUTH_DAO.createAuth("temp", "authToken").authToken();
        GAME_DAO.createGame("newGame!");
        String responseText = serverFacade.joinGame(new JoinGameRequest("WHITE", 502345), authToken);
        assertEquals("Error: bad request", responseText);
    }

    @Test
    void joinGameFailure2() throws Exception {
        var authToken = AUTH_DAO.createAuth("temp", "authToken").authToken();
        int gameID = GAME_DAO.createGame("newGame!").gameID();
        serverFacade.joinGame(new JoinGameRequest("WHITE", gameID), authToken);
        var secondAuthToken = AUTH_DAO.createAuth("NewGuy", "diffAuth").authToken();
        String responseText = serverFacade.joinGame(new JoinGameRequest("WHITE", gameID), secondAuthToken);
        assertEquals("Error: already taken", responseText);
    }


    @Test
    void observeGameSuccess() throws Exception {
        var authToken = AUTH_DAO.createAuth("temp", "authToken").authToken();
        int gameID = GAME_DAO.createGame("newGame!").gameID();
        String responseText = serverFacade.observeGame(String.valueOf(gameID), authToken);
        assertFalse(responseText.contains("Error"));
        assertTrue(responseText.contains(String.valueOf(gameID)) && responseText.contains("observing"));
    }

    @Test
    void observeGameFailure() throws Exception {
        var authToken = AUTH_DAO.createAuth("temp", "authToken").authToken();
        String responseText = serverFacade.observeGame(String.valueOf(40024), authToken);
        assertTrue(responseText.contains("Error") && responseText.contains("existing"));
    }

    @Test
    void observeGameFailure2() throws Exception {
        var authToken = AUTH_DAO.createAuth("temp", "authToken").authToken();
        int gameID = GAME_DAO.createGame("newGame!").gameID();
        String responseText = serverFacade.observeGame(String.valueOf(24), authToken);
        assertTrue(responseText.contains("Error") && responseText.contains("existing"));
    }

}
