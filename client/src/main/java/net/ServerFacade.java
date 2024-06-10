package net;


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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerFacade {
    HttpClientCommunicator httpClientCommunicator;

    public ServerFacade(int portNum){
        this.httpClientCommunicator = new HttpClientCommunicator(portNum);
    }

    public String register(UserData user) throws IOException {
        String urlExtension = "user";
        String registerJSON = SerializerDeserializer.convertToJSON(user);
        ResponseType responseObject = HttpClientCommunicator.createHttpPost(registerJSON, null, urlExtension);
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
        ResponseType responseObject = HttpClientCommunicator.createHttpPost(loginJSON, null, urlExtension);
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
        ResponseType responseObject = HttpClientCommunicator.createHttpPost(createGameJSON, authToken, urlExtension);
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
        ResponseType responseObject = HttpClientCommunicator.createHttpDelete(authToken, urlExtension);
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
        ResponseType responseObject = HttpClientCommunicator.createHttpGet(authToken, urlExtension);
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

    public String joinGame(JoinGameRequest joinRequest, String authToken) throws IOException {
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
        ResponseType responseObject = HttpClientCommunicator.createHttpPut(joinGameJSON, authToken, urlExtension);
        if (responseObject == null){
            return " successfully joined the game with ID # " + joinRequest.gameID();
        }
        else if (responseObject instanceof ErrorResponse){
            return ((ErrorResponse) responseObject).message();
        }
        else{
            return "Unknown Error. Please contact us.";
        }
    }

    public String observeGame(String gameID, String authToken) throws IOException {
        ListGamesResponse currGameList = SerializerDeserializer.convertFromJSON(listGames(authToken), ListGamesResponse.class);
        if (currGameList.games().isEmpty()){
            return "Error: The game ID " + gameID + " does not match any existing game!";
        }
        for (GameData gameData : currGameList.games() ){
            if (gameID.equals(String.valueOf(gameData.gameID()))){
                return "Now observing the chess game with ID #: " + gameID;
            }
        }
        return "Error: The game ID " + gameID + " does not match any existing game!";
    }

}
