package net;


import model.AuthData;
import model.UserData;
import request.CreateGameRequest;
import request.LoginRequest;
import response.CreateGameResponse;
import response.ErrorResponse;
import response.ListGamesResponse;
import response.ResponseType;
import server.SerializerDeserializer;

import java.io.IOException;

public class ServerFacade {

    public static String register(UserData user) throws IOException {
        String urlExtension = "user";
        String registerJSON = SerializerDeserializer.convertToJSON(user);
        ResponseType responseObject = ClientCommunicator.createHttpPost(registerJSON, null, urlExtension);
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

    public static String login(LoginRequest loginRequest) throws IOException {
        String urlExtension = "session";
        String loginJSON = SerializerDeserializer.convertToJSON(loginRequest);
        ResponseType responseObject = ClientCommunicator.createHttpPost(loginJSON, null, urlExtension);
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

    public static String createGame(CreateGameRequest createGameRequest, String authToken) throws IOException {
        String urlExtension = "game";
        String createGameJSON = SerializerDeserializer.convertToJSON(createGameRequest);
        ResponseType responseObject = ClientCommunicator.createHttpPost(createGameJSON, authToken, urlExtension);
        if (responseObject instanceof CreateGameResponse){
            return String.valueOf( ((CreateGameResponse) responseObject).gameID() );
        }
        else if (responseObject instanceof ErrorResponse){
            return ((ErrorResponse) responseObject).message();
        }
        else{
            return "Unknown Error. Please contact us.";
        }
    }

    public static String logout(String authToken) throws IOException {
        String urlExtension = "session";
        ResponseType responseObject = ClientCommunicator.createHttpDelete(authToken, urlExtension);
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

    public static String listGames(String authToken) throws IOException {
        String urlExtension = "game";
        ResponseType responseObject = ClientCommunicator.createHttpGet(authToken, urlExtension);
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



}
