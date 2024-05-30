package server;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.dao.memorydao.MemoryAuthDao;
import dataaccess.dao.memorydao.MemoryGameDao;
import dataaccess.dao.memorydao.MemoryUserDao;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import response.*;
import service.*;
import spark.*;
import dataaccess.dao.*;

import java.util.HashMap;

public class Server {
    private final UserDao userDao;
    private final GameDao gameDao;
    private final AuthDao authDao;

    private static final HashMap<String, Integer> ERRORCODEMESSAGEMAP = new HashMap<>() {{
        put("Error: bad request", 400);
        put("Error: unauthorized", 401);
        put("Error: already taken", 403);
    }};

    public Server(){
        try{
            DatabaseManager.createDatabase(); //if it doesn't already exist
        }
        catch (DataAccessException e){
            e.printStackTrace();//best way to handle this since there is no "status code"?
        }

        this.userDao = new MemoryUserDao();
        this.gameDao = new MemoryGameDao();
        this.authDao = new MemoryAuthDao();
        //to be replaced w/ SQL in Phase 4
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        createRoutes();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private static Response badRequestChecker(Request req, Response res){
        if (req.body().isEmpty()){
            res.status(400);
            res.body("Error: bad request");
            return res;
        }
        return null;
    }


    private static String errorTranslator(Response res, DataAccessException thrownError){
        res.status(ERRORCODEMESSAGEMAP.get(thrownError.getMessage()));
        return SerializerDeserializer.convertToJSON(thrownError);
    }


    private static String successResponse(Response res, ResponseType serviceResponse){
        res.status(200);
        return SerializerDeserializer.convertToJSON(serviceResponse);
    }

    private static String blankSuccessResponse(Response res){
        res.status(200);
        return "{}";
    }


    private void createRoutes() {
        //GET list of games
        Spark.get("/game", (req, res) -> {
            String authToken = req.headers("authorization");
            GameService gameService = new GameService(this.gameDao, this.authDao);
            try {
                ListGamesResponse response = gameService.listGames(authToken);
                return successResponse(res, response);
            }
            catch (DataAccessException e){
                return errorTranslator(res, e);
            }
        });
        Spark.post("/:givenPath", (req, res) -> {         //POST creating a new game, login, register
            String givenPath = req.params(":givenPath");
            UserService userService = new UserService(this.userDao, this.authDao); //needed objects throughout this branch
            GameService gameService = new GameService(this.gameDao, this.authDao);
            ResponseType response;
            if (badRequestChecker(req, res) != null) {
                return badRequestChecker(req, res);
            }
            switch (givenPath) {
                case "game":
                    String authToken = req.headers("authorization");
                    CreateGameRequest newGameRequest = SerializerDeserializer.convertFromJSON(req.body(), CreateGameRequest.class);
                    try {
                        response = gameService.createGame(authToken, newGameRequest);
                        return successResponse(res, response);
                    }
                    catch (DataAccessException e) {
                        return errorTranslator(res, e);
                    }
                case "session":
                    LoginRequest currUser = SerializerDeserializer.convertFromJSON(req.body(), LoginRequest.class);
                    try {
                        response = userService.login(currUser); //will be compared in UserService
                        return successResponse(res, response);
                    }
                    catch (DataAccessException e) {
                        return errorTranslator(res, e);
                    }
                case "user":
                    UserData newUser = SerializerDeserializer.convertFromJSON(req.body(), UserData.class);
                    try{
                        response = userService.register(new UserData(newUser.username(),BCrypt.hashpw(newUser.password(), BCrypt.gensalt()), newUser.email()) );
                        return successResponse(res, response);
                    }
                    catch (DataAccessException e){
                       return errorTranslator(res, e);
                    }
                default:
                    res.status(404);
                    res.body("Error: Not found");
                    return res;
            }
        });
        Spark.put("/game", (req, res) -> {          //PUT join game
            if (badRequestChecker(req, res) != null) {
                return badRequestChecker(req, res);
            }
            String authToken = req.headers("authorization");
            JoinGameRequest joinGameRequest = SerializerDeserializer.convertFromJSON(req.body(), JoinGameRequest.class);
            GameService gameService = new GameService(this.gameDao, this.authDao);
            try {
                gameService.joinGame(authToken, joinGameRequest);
                return blankSuccessResponse(res);
            }
            catch (DataAccessException e) {
                return errorTranslator(res, e);
            }
        });
        Spark.delete("/:givenPath", (req, res) -> {          //DELETE clear all databases/DAOs or logout
            String givenPath = req.params(":givenPath");
            switch (givenPath) {
                case "db":
                    SystemService systemService = new SystemService(this.gameDao, this.authDao, this.userDao);
                    try{
                        systemService.clear();
                        return blankSuccessResponse(res);
                    }
                    catch (DataAccessException e){
                        return errorTranslator(res, e);
                    }
                case "session":
                    String authToken = req.headers("authorization");
                    UserService userService = new UserService(this.userDao, this.authDao);
                    try{
                        userService.logout(authToken);
                        return blankSuccessResponse(res);
                    }
                    catch (DataAccessException e) {
                        return errorTranslator(res, e);
                    }
                default:
                    res.status(404);
                    res.body("Error: Not found");
                    return res;
            }
        });
    }

    public static void main(String[] args){
        new Server().run(8080);
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
