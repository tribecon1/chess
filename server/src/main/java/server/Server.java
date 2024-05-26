package server;

import dataaccess.DataAccessException;
import dataaccess.dao.memoryDao.MemoryAuthDao;
import dataaccess.dao.memoryDao.MemoryGameDao;
import dataaccess.dao.memoryDao.MemoryUserDao;
import model.UserData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import response.*;
import service.GameService;
import service.SystemService;
import service.UserService;
import spark.*;
import dataaccess.dao.*;

import java.util.HashMap;

public class Server {
    private final UserDao userDao;
    private final GameDao gameDao;
    private final AuthDao authDao;

    private static final HashMap<String, Integer> ErrorCodeMessageMap = new HashMap<>() {{
        put("Error: bad request", 400);
        put("Error: unauthorized", 401);
        put("Error: already taken", 403);
    }};

    public Server(){
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

    private static Response BadRequestChecker(Request req, Response res){
        if (req.body().isEmpty()){
            res.status(400);
            res.body("Error: bad request");
            return res;
        }
        return null;
    }


    private static String ErrorTranslator(Response res, DataAccessException thrownError){
        res.status(ErrorCodeMessageMap.get(thrownError.getMessage()));
        return SerializerDeserializer.ConvertToJSON(thrownError);
    }


    private static String SuccessResponse(Response res, ResponseType serviceResponse){
        res.status(200);
        return SerializerDeserializer.ConvertToJSON(serviceResponse);
    }

    private static String BlankSuccessResponse(Response res){
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
                return SuccessResponse(res, response);
            }
            catch (DataAccessException e){
                return ErrorTranslator(res, e);
            }
        });

        //POST creating a new game, login, register
        Spark.post("/:givenPath", (req, res) -> {
            String givenPath = req.params(":givenPath");

            UserService userService = new UserService(this.userDao, this.authDao); //needed objects throughout this branch
            GameService gameService = new GameService(this.gameDao, this.authDao);
            ResponseType response;

            if (BadRequestChecker(req, res) != null) {
                return BadRequestChecker(req, res);
            }
            switch (givenPath) {
                case "game":
                    String authToken = req.headers("authorization");
                    CreateGameRequest newGameRequest = SerializerDeserializer.ConvertFromJSON(req.body(), CreateGameRequest.class);
                    try {
                        response = gameService.createGame(authToken, newGameRequest);
                        return SuccessResponse(res, response);
                    }
                    catch (DataAccessException e) {
                        return ErrorTranslator(res, e);
                    }
                case "session":
                    LoginRequest currUser = SerializerDeserializer.ConvertFromJSON(req.body(), LoginRequest.class);
                    try {
                        response = userService.login(currUser);
                        return SuccessResponse(res, response);
                    }
                    catch (DataAccessException e) {
                        return ErrorTranslator(res, e);
                    }
                case "user":
                    UserData newUser = SerializerDeserializer.ConvertFromJSON(req.body(), UserData.class);
                    try{
                        response = userService.register(newUser);
                        return SuccessResponse(res, response);
                    }
                    catch (DataAccessException e){
                       return ErrorTranslator(res, e);
                    }
                default:
                    res.status(404);
                    res.body("Error: Not found");
                    return res;
            }
        });

        //PUT join game
        Spark.put("/game", (req, res) -> {
            if (BadRequestChecker(req, res) != null) {
                return BadRequestChecker(req, res);
            }
            String authToken = req.headers("authorization");
            JoinGameRequest joinGameRequest = SerializerDeserializer.ConvertFromJSON(req.body(), JoinGameRequest.class);
            GameService gameService = new GameService(this.gameDao, this.authDao);
            try {
                gameService.joinGame(authToken, joinGameRequest);
                return BlankSuccessResponse(res);
            }
            catch (DataAccessException e) {
                return ErrorTranslator(res, e);
            }
        });

        //DELETE clear all databases/DAOs or logout
        Spark.delete("/:givenPath", (req, res) -> {
            String givenPath = req.params(":givenPath");

            ResponseType response;

            switch (givenPath) {
                case "db":
                    SystemService systemService = new SystemService(this.gameDao, this.authDao, this.userDao);
                    try{
                        systemService.clear();
                        return BlankSuccessResponse(res);
                    }
                    catch (DataAccessException e){
                        return ErrorTranslator(res, e);
                    }
                case "session":
                    String authToken = req.headers("authorization");
                    UserService userService = new UserService(this.userDao, this.authDao);

                    try{
                        userService.logout(authToken);
                        return BlankSuccessResponse(res);
                    }
                    catch (DataAccessException e) {
                        return ErrorTranslator(res, e);
                    }
                default:
                    res.status(500);
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
