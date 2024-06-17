package net;


import chess.ChessMove;
import model.AuthData;
import model.GameData;
import model.UserData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import response.CreateGameResponse;
import response.ErrorResponse;
import response.ListGamesResponse;
import response.ResponseType;

import server.SerializerDeserializer;
import ui.WebSocketCommunicator;
import websocket.commands.ConnectCommand;
import websocket.commands.LeaveCommand;
import websocket.commands.MoveCommand;
import websocket.commands.ResignCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerFacade {
    private final HttpClientCommunicator httpClientCommunicator;
    private final WebSocketCommunicator websocketCommunicator;

    public ServerFacade(int portNum, ServerMessageObserver serverMessageObserver) throws Exception {
        this.httpClientCommunicator = new HttpClientCommunicator(portNum);
        this.websocketCommunicator = new WebSocketCommunicator(serverMessageObserver, portNum);
    }

    public String register(UserData user) throws IOException {
        String urlExtension = "user";
        String registerJSON = SerializerDeserializer.convertToJSON(user);
        ResponseType responseObject = httpClientCommunicator.createHttpPost(registerJSON, null, urlExtension);
        if (responseObject instanceof AuthData){
            return ((AuthData) responseObject).authToken();
        }
        else if (responseObject instanceof ErrorResponse){
            return ((ErrorResponse) responseObject).message();
        }
        else{
            return "Unknown Error. Please contact us.";
        }
    }

    public String login(LoginRequest loginRequest) throws IOException {
        String urlExtension = "session";
        String loginJSON = SerializerDeserializer.convertToJSON(loginRequest);
        ResponseType responseObject = httpClientCommunicator.createHttpPost(loginJSON, null, urlExtension);
        if (responseObject instanceof AuthData){
            return ((AuthData) responseObject).authToken();
        }
        else if (responseObject instanceof ErrorResponse){
            return ((ErrorResponse) responseObject).message();
        }
        else{
            return "Unknown Error. Please contact us.";
        }
    }

    public String createGame(CreateGameRequest createGameRequest, String authToken) throws IOException {
        String urlExtension = "game";
        String createGameJSON = SerializerDeserializer.convertToJSON(createGameRequest);
        ResponseType responseObject = httpClientCommunicator.createHttpPost(createGameJSON, authToken, urlExtension);
        if (responseObject instanceof CreateGameResponse){
            return "Your game was created! Call the \"list\" command to see its number in the list in order to join it!";
        }
        else if (responseObject instanceof ErrorResponse){
            return ((ErrorResponse) responseObject).message();
        }
        else{
            return "Unknown Error. Please contact us.";
        }
    }

    public String logout(String authToken) throws IOException {
        String urlExtension = "session";
        ResponseType responseObject = httpClientCommunicator.createHttpDelete(authToken, urlExtension);
        if (responseObject == null){
            return "Successfully logged out user ";
        }
        else if (responseObject instanceof ErrorResponse){
            return ((ErrorResponse) responseObject).message();
        }
        else{
            return "Unknown Error. Please contact us.";
        }
    }

    public String listGames(String authToken) throws IOException {
        String urlExtension = "game";
        ResponseType responseObject = httpClientCommunicator.createHttpGet(authToken, urlExtension);
        if (responseObject instanceof ListGamesResponse){
            return SerializerDeserializer.convertToJSON(responseObject);
        }
        else if (responseObject instanceof ErrorResponse){
            return ((ErrorResponse) responseObject).message();
        }
        else{
            return "Unknown Error. Please contact us.";
        }
    }

    public String joinGame(JoinGameRequest joinRequest, String authToken) throws Exception {
        String urlExtension = "game";
        List<GameData> listOfGames = new ArrayList<>(SerializerDeserializer.convertFromJSON(listGames(authToken), ListGamesResponse.class).games());
        JoinGameRequest translatedJoinRequest;
        if (listOfGames.isEmpty()){
            return "Error: no games currently exist to join";
        }
        else if(joinRequest.gameID() > listOfGames.size()){
            return "Error: invalid game # given, there is no game at the #: " + joinRequest.gameID();
        }
        else {
            translatedJoinRequest = new JoinGameRequest(joinRequest.playerColor(), listOfGames.get(joinRequest.gameID()-1).gameID());
        }
        String joinGameJSON = SerializerDeserializer.convertToJSON(translatedJoinRequest);
        ResponseType responseObject = httpClientCommunicator.createHttpPut(joinGameJSON, authToken, urlExtension);
        if (responseObject == null){
            return String.valueOf(translatedJoinRequest.gameID());
        }
        else if (responseObject instanceof ErrorResponse){
            return ((ErrorResponse) responseObject).message();
        }
        else{
            return "Unknown Error. Please contact us.";
        }
    }

    public String observeGame(String gameID, String authToken) throws IOException {
        List<GameData> listOfGames = new ArrayList<>(SerializerDeserializer.convertFromJSON(listGames(authToken), ListGamesResponse.class).games());
        if (listOfGames.isEmpty()){
            return "Error: no games currently exist to join";
        }
        else if(Integer.parseInt(gameID) > listOfGames.size()){
            return "Error: The game ID " + gameID + " does not match any existing game!";
        }
        else {
            return String.valueOf(listOfGames.get(Integer.parseInt(gameID)-1).gameID());
        }
    }

    public void connect(int currGameID, String authToken) throws Exception {
        websocketCommunicator.send(new ConnectCommand(authToken, currGameID));
    }

    public void leave(int currGameID, String authToken) throws Exception {
        websocketCommunicator.send(new LeaveCommand(authToken, currGameID));
    }

    public void move(int currGameID, String authToken, ChessMove desiredMove) throws Exception {
        websocketCommunicator.send(new MoveCommand(authToken, currGameID, desiredMove));
    }

    public void resign(int currGameID, String authToken) throws Exception {
        websocketCommunicator.send(new ResignCommand(authToken, currGameID));
    }


}