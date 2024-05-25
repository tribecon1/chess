package server;

import dataaccess.dao.memoryDao.MemoryAuthDao;
import dataaccess.dao.memoryDao.MemoryGameDao;
import dataaccess.dao.memoryDao.MemoryUserDao;
import model.AuthData;
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

public class Server {
    private final UserDao userDao;
    private final GameDao gameDao;
    private final AuthDao authDao;

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

    private static String ErrorCreator(Response res, ResponseType serviceResponse){
        if (serviceResponse instanceof ErrorResponse) {
            res.status( ((ErrorResponse) serviceResponse).statusCode() );
            return SerializerDeserializer.ConvertToJSON(serviceResponse);
        }
        else {
            res.status(500);
            return "Another error";
        }
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
            ResponseType response = gameService.listGames(authToken);
            if (response instanceof ListGamesResponse){
                return SuccessResponse(res, response);
            }
            else {
                return ErrorCreator(res, response);
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
                    response = gameService.createGame(authToken, newGameRequest);
                    if (response instanceof CreateGameResponse) {
                        return SuccessResponse(res, response);
                    }
                    else{
                        return ErrorCreator(res, response);
                    }
                case "session":
                    LoginRequest currUser = SerializerDeserializer.ConvertFromJSON(req.body(), LoginRequest.class);
                    response = userService.login(currUser);
                    if (response instanceof AuthData){
                        return SuccessResponse(res, response);
                    }
                    else{
                        return ErrorCreator(res, response);
                    }
                case "user":
                    UserData newUser = SerializerDeserializer.ConvertFromJSON(req.body(), UserData.class);
                    response = userService.register(newUser);
                    if (response instanceof AuthData){
                        return SuccessResponse(res, response);
                    }
                    else {
                        return ErrorCreator(res, response);
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
            ResponseType response = gameService.joinGame(authToken, joinGameRequest);
            if (response == null){
                return BlankSuccessResponse(res);
            }
            else {
                return ErrorCreator(res, response);
            }
        });

        //DELETE clear all databases/DAOs or logout
        Spark.delete("/:givenPath", (req, res) -> {
            String givenPath = req.params(":givenPath");

            ResponseType response;

            switch (givenPath) {
                case "db":
                    SystemService systemService = new SystemService(this.gameDao, this.authDao, this.userDao);
                    response = systemService.clear();
                    if (response == null){
                        return BlankSuccessResponse(res);
                    }
                    else{
                        res.status(500);
                        res.body( "Another error" );
                        return res;
                    }
                case "session":
                    String authToken = req.headers("authorization");
                    UserService userService = new UserService(this.userDao, this.authDao);
                    response = userService.logout(authToken);
                    if (response == null){
                        return BlankSuccessResponse(res);
                    }
                    else{
                        return ErrorCreator(res, response);
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
