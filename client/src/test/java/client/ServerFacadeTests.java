package client;

import dataaccess.DataAccessException;
import dataaccess.dao.AuthDao;
import dataaccess.dao.GameDao;
import dataaccess.dao.UserDao;
import dataaccess.dao.sqldao.SqlAuthDao;
import dataaccess.dao.sqldao.SqlGameDao;
import dataaccess.dao.sqldao.SqlUserDao;
import net.ClientCommunicator;
import net.ServerFacade;
import org.junit.jupiter.api.*;
import server.Server;
import service.SystemService;


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
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}
