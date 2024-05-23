package server;

import dataaccess.dao.memoryDao.MemoryAuthDao;
import dataaccess.dao.memoryDao.MemoryGameDao;
import dataaccess.dao.memoryDao.MemoryUserDao;
import model.UserData;
import request.JoinGameRequest;
import response.ErrorResponse;
import response.ListGamesResponse;
import response.RegisterResponse;
import response.ResponseType;
import service.GameService;
import service.UserService;
import spark.*;
import dataaccess.dao.*;

public class Server {
    private UserDao userDao;
    private GameDao gameDao;
    private AuthDao authDao;

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

    private static Response CustomErrorCreator(ResponseType serviceResponse, Response res){
        res.status( ((ErrorResponse) serviceResponse).statusCode() );
        res.body( ((ErrorResponse) serviceResponse).message() );
        return res;
    }

    private void createRoutes() {
        //GET list of games
        Spark.get("/game", (req, res) -> {
            String authToken = req.headers("authorization");
            GameService gameService = new GameService(this.gameDao, this.authDao);
            ResponseType response = gameService.listGames(authToken);
            if (response instanceof ListGamesResponse){
                res.status(200);
            }
            else if (response instanceof ErrorResponse) {
                res.status(401);
            }
            else {
                res.status(500);
            }
            res.body(SerializerDeserializer.ConvertToJSON(response));
            return res;
        });

        //POST register, login, or creating a new game
        Spark.post("/:givenPath", (req, res) -> {
            String givenPath = req.params(":givenPath");

            switch (givenPath) {
                case "/game":
                    createGameHandler(req, res);
                    return;
                case "/session":
                    loginHandler(req,res);
                    return;
                case "/user":
                    if (BadRequestChecker(req, res) != null) {
                        return BadRequestChecker(req, res);
                    }
                    UserData newUser = SerializerDeserializer.ConvertFromJSON(req.body(), UserData.class);
                    UserService userService = new UserService(this.userDao, this.authDao);
                    ResponseType response = userService.register(newUser);
                    if (response instanceof RegisterResponse){
                        res.status(200);
                        res.body(SerializerDeserializer.ConvertToJSON(response));
                        return res;
                    }
                    else if (response instanceof ErrorResponse) {
                        return CustomErrorCreator(response,res);
                    }
                    else {
                        res.status(500);
                        res.body( "Another error" );
                        return res;
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
                res.status(200);
                return res;
            }
            else if (response instanceof ErrorResponse){
                return CustomErrorCreator(response,res);
            }
            else {
                res.status(500);
                res.body( "Another error" );
                return res;
            }
        });

        Spark.delete("/:givenPath", (req, res) -> {
            String givenPath = req.params(":givenPath");
            if (givenPath == null) {
                res.status(404);
                return "Path option not found";
            }
            switch (givenPath) {
                case "/db":
                    clearApplicationHandler(req, res);
                    return;
                case "/session":
                    logoutHandler(req, res);
                    return;
                default:
                    res.status(500);
                    return;
            }

        });
    }



    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
